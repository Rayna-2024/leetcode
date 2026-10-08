import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Interview412 {

    // ─────────────────────────────────────────────
    // Order request object
    // ─────────────────────────────────────────────
    static class OrderRequest {
        String userId;
        String productId;
        double productPrice;

        OrderRequest(String userId, String productId, double productPrice) {
            this.userId = userId;
            this.productId = productId;
            this.productPrice = productPrice;
        }
    }

    // ─────────────────────────────────────────────
    // Single validation result (one rule)
    // ─────────────────────────────────────────────
    static class ValidationResult {
        boolean valid;
        String message;

        ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        @Override
        public String toString() {
            return (valid ? "[PASS] " : "[FAIL] ") + message;
        }
    }

    // ─────────────────────────────────────────────
    // Aggregated result – collects ALL rule outcomes
    // ─────────────────────────────────────────────
    static class AggregatedResult {
        List<ValidationResult> results = new ArrayList<>();

        void add(ValidationResult r) {
            results.add(r);
        }

        boolean isValid() {
            return results.stream().allMatch(r -> r.valid);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (ValidationResult r : results) {
                sb.append("  ").append(r).append("\n");
            }
            sb.append(isValid()
                    ? "  => Order ACCEPTED"
                    : "  => Order REJECTED");
            return sb.toString();
        }
    }

    // ─────────────────────────────────────────────
    // Validator interface
    // ─────────────────────────────────────────────
    interface Validator {
        ValidationResult validate(OrderRequest order);
    }

    // ─────────────────────────────────────────────
    // Rule 1: User must NOT be in the blacklist
    // ─────────────────────────────────────────────
    static class BlacklistValidator implements Validator {
        private final Set<String> blacklist;

        BlacklistValidator(Set<String> blacklist) {
            this.blacklist = blacklist;
        }

        @Override
        public ValidationResult validate(OrderRequest order) {
            // Simulate a slow check (e.g. querying a remote blacklist service)
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            if (blacklist.contains(order.userId)) {
                return new ValidationResult(false,
                        "User '" + order.userId + "' is blacklisted. [thread=" + Thread.currentThread().getName() + "]");
            }
            return new ValidationResult(true,
                    "User '" + order.userId + "' is not blacklisted. [thread=" + Thread.currentThread().getName() + "]");
        }
    }

    // ─────────────────────────────────────────────
    // Rule 2: Product price must NOT be negative
    // ─────────────────────────────────────────────
    static class PriceValidator implements Validator {
        @Override
        public ValidationResult validate(OrderRequest order) {
            // Simulate a slow check (e.g. querying a pricing service)
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            if (order.productPrice < 0) {
                return new ValidationResult(false,
                        "Price " + order.productPrice + " is invalid. [thread=" + Thread.currentThread().getName() + "]");
            }
            return new ValidationResult(true,
                    "Price " + order.productPrice + " is valid. [thread=" + Thread.currentThread().getName() + "]");
        }
    }

    // ─────────────────────────────────────────────
    // ValidatorGroup – runs ALL validators in PARALLEL
    // using Callable + ExecutorService (java.util.concurrent)
    //
    // Each Validator is wrapped in a Callable<ValidationResult>
    // so it can return a value from its thread.
    // ExecutorService manages the thread pool.
    // Future<ValidationResult> lets us collect results after all threads finish.
    // ─────────────────────────────────────────────
    static class ValidatorGroup {
        private final List<Validator> validators = new ArrayList<>();
        // Thread pool: one thread per validator runs simultaneously
        private final ExecutorService executor;

        ValidatorGroup(int threadPoolSize) {
            this.executor = Executors.newFixedThreadPool(threadPoolSize);
        }

        void addValidator(Validator v) {
            validators.add(v);
        }

        AggregatedResult validateAll(OrderRequest order) throws Exception {
            // Step 1: wrap each validator as a Callable (has return value, unlike Runnable)
            List<Callable<ValidationResult>> tasks = new ArrayList<>();
            for (Validator v : validators) {
                tasks.add(() -> v.validate(order));  // lambda implements Callable.call()
            }

            // Step 2: submit all tasks to the thread pool at once → they run in parallel
            List<Future<ValidationResult>> futures = executor.invokeAll(tasks);

            // Step 3: collect results (Future.get() blocks until that thread finishes)
            AggregatedResult aggregated = new AggregatedResult();
            for (Future<ValidationResult> future : futures) {
                aggregated.add(future.get());
            }
            return aggregated;
        }

        void shutdown() {
            executor.shutdown();
        }
    }

    // ─────────────────────────────────────────────
    // Order Service
    // ─────────────────────────────────────────────
    static class OrderService {
        private final ValidatorGroup group;

        OrderService(Set<String> blacklist) {
            // thread pool size = number of validators (all run at the same time)
            group = new ValidatorGroup(2);
            group.addValidator(new BlacklistValidator(blacklist));
            group.addValidator(new PriceValidator());
        }

        AggregatedResult submitOrder(OrderRequest order) throws Exception {
            System.out.println("\nSubmitting order → userId=" + order.userId
                    + ", productId=" + order.productId
                    + ", price=" + order.productPrice);

            long start = System.currentTimeMillis();
            AggregatedResult result = group.validateAll(order);
            long elapsed = System.currentTimeMillis() - start;

            System.out.println(result);
            // Both validators sleep 100ms; if truly parallel, total ≈ 100ms (not 200ms)
            System.out.println("  (completed in ~" + elapsed + "ms)");
            return result;
        }

        void shutdown() {
            group.shutdown();
        }
    }

    // ─────────────────────────────────────────────
    // Demo / test
    // ─────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        Set<String> blacklist = new HashSet<>(Arrays.asList("user_banned", "user_fraud"));

        OrderService service = new OrderService(blacklist);

        // Case 1: valid order – both validators pass
        service.submitOrder(new OrderRequest("user_alice", "prod_001", 29.99));

        // Case 2: blacklisted user only
        service.submitOrder(new OrderRequest("user_banned", "prod_002", 15.00));

        // Case 3: negative price only
        service.submitOrder(new OrderRequest("user_bob", "prod_003", -5.00));

        // Case 4: BOTH violations – both errors reported simultaneously
        service.submitOrder(new OrderRequest("user_fraud", "prod_004", -10.00));

        // Case 5: zero price (edge case – should be valid)
        service.submitOrder(new OrderRequest("user_carol", "prod_005", 0.00));

        service.shutdown();
    }
}
