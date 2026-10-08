python - <<'PY'
from vllm.distributed.kv_transfer.kv_connector.v1.base import SupportsHMA
from lmcache_ascend.integration.vllm.lmcache_ascend_connector_v1 import LMCacheAscendConnectorV1Dynamic

print(issubclass(LMCacheAscendConnectorV1Dynamic, SupportsHMA))
PY