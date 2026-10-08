import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Interview43 {
    static class Edge {
        int to;
        int dist;
        Edge(int to, int dist){
            this.to = to;
            this.dist = dist;
        }
    }

    static Map<Integer, List<Edge>> graph;
    static boolean[] visited;
    static boolean[] res;
    static int k;
    public static void main(String[] args) {

        graph = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        k = scanner.nextInt();
        int[][] inputs = new int[m][3];
        for(int i = 0;i < m;i++){
            inputs[i][0] = scanner.nextInt();
            inputs[i][1] = scanner.nextInt();
            inputs[i][2] = scanner.nextInt();

            graph.putIfAbsent(inputs[i][0], new ArrayList<>());
            graph.putIfAbsent(inputs[i][1], new ArrayList<>());

            graph.get(inputs[i][0]).add(new Edge(inputs[i][1], inputs[i][2]));
            graph.get(inputs[i][1]).add(new Edge(inputs[i][0], inputs[i][2]));
        }

        visited = new boolean[n+1];
        res = new boolean[n+1];

        for(int i = 1;i <= n;i++){
            // visited = new boolean[n+1];
            Arrays.fill(visited, false);
            visited[i] = true;
            dfs(i);
            visited[i] = false;
        }

        int ans = 0;
        StringBuilder sb = new StringBuilder();
        for(int i = 1;i <= n;i++){
            if(res[i] == true){
                ans++;
                sb.append(i);
                sb.append(" ");
            }
        }
        // sb.deleteCharAt();
        System.out.println(ans);
        System.out.println(sb.toString());
    }
    public static void dfs(int start){
        boolean hasNext = false;
        for(Edge e:graph.getOrDefault(start, Collections.emptyList())){
            int v = e.to;
            if(e.dist < k && !visited[v]){
                hasNext = true;
                visited[v] = true;
                dfs(v);
                visited[v] = false;
            }

        }

        if(!hasNext){
            res[start] = true;
        }

    }
}
