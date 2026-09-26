# Redis integration tests

The Test workflow runs `RedisClusterConnPoolTest` against three temporary local
Redis Cluster masters. It covers string reads/writes, NX and expiry, lists,
sets, and hashes with the configured Jedis version.

With `redis-server` and `redis-cli` installed, run from the repository root:

```sh
bash .github/scripts/test-redis-cluster.sh \
  mvn -pl yuzhouwan-bigdata/yuzhouwan-bigdata-redis -am clean install
```

The script uses ports 17000–17002 and their cluster bus ports 27000–27002,
then stops the processes and removes their temporary data on exit.
Ordinary Maven runs skip this integration test unless `redis.cluster.nodes`
is set; the CI wrapper always supplies it.
