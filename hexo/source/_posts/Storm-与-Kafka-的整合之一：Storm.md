title: Storm 与 Kafka 的整合之一：Storm
date: 2015-04-22 19:09:42
tags:
- Storm
- Kafka
categories:
- Storm
---

## __<font color='blue'>*什么是 Storm?*__

&nbsp;&nbsp; <font size=3> Apache Storm is a free and open source distributed realtime computation system. - - [Official website][1]

## __<font color='blue'>*为什么要有 Storm?*__
###分布式
```clojure
	具备经济、快速、可靠、易扩充、数据共享、设备共享、通讯方便、灵活等分布式所具备的特性
```
###可扩展性
```clojure
	计算在多线程、进程 和 服务器之间并行进行
```
###高可靠性
```clojure
	能管理工作进程 和 节点的故障
	消息处理，能得到一次完成处理的保证
```
###编程模型简单
```clojure
	降低了并行批处理复杂性
```
###高效实时
```clojure
	利用 ZeroMQ 保证了消息的快速处理
```
###支持热部署
```clojure
	加速应用开发
```

## __<font color='blue'>*Storm 工作机制*__
### 一些主要概念
 - Topology（计算拓扑）
 - Stream（消息流）
 - Spout（消息源）
 - Bolt（消息处理者）
 -  grouping（数据的分发方式）
 - Topology（拓扑）
 - Worker（工作进程）
 - Task（执行具体逻辑的任务）
 - Executor（执行 Task 的线程）
 - Configuration（配置）


[1]:https://storm.apache.org/
