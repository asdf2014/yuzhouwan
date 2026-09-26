#!/usr/bin/env bash
set -euo pipefail

# Run a Maven command against three disposable Redis Cluster masters.
redis_test_dir=$(mktemp -d)
redis_test_pids=()
cleanup() {
  for pid in "${redis_test_pids[@]}"; do
    kill "$pid" 2>/dev/null || true
  done
  wait || true
  rm -rf -- "$redis_test_dir"
}
trap cleanup EXIT

for port in 17000 17001 17002; do
  redis-server --port "$port" --bind 127.0.0.1 --protected-mode yes \
    --cluster-enabled yes --cluster-config-file "nodes-$port.conf" \
    --cluster-node-timeout 5000 --appendonly no --save '' \
    --dir "$redis_test_dir" > "$redis_test_dir/$port.log" 2>&1 &
  redis_test_pids+=("$!")
  ready=false
  for attempt in {1..100}; do
    if ! kill -0 "${redis_test_pids[-1]}" 2>/dev/null; then
      cat "$redis_test_dir/$port.log"
      exit 1
    fi
    if [[ $(redis-cli -h 127.0.0.1 -p "$port" ping 2>/dev/null) == PONG ]]; then
      ready=true
      break
    fi
    sleep 0.1
  done
  if [[ "$ready" != true ]]; then
    cat "$redis_test_dir/$port.log"
    exit 1
  fi
done

redis-cli --cluster create 127.0.0.1:17000 127.0.0.1:17001 127.0.0.1:17002 \
  --cluster-replicas 0 --cluster-yes

for port in 17000 17001 17002; do
  ready=false
  for attempt in {1..100}; do
    if redis-cli -h 127.0.0.1 -p "$port" cluster info | tr -d '\r' | grep -q '^cluster_state:ok$'; then
      ready=true
      break
    fi
    sleep 0.1
  done
  if [[ "$ready" != true ]]; then
    cat "$redis_test_dir/$port.log"
    exit 1
  fi
done

"$@" -Dredis.cluster.nodes=127.0.0.1:17000,127.0.0.1:17001,127.0.0.1:17002
