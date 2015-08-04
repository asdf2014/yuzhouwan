title: Storm 与 Kafka 的整合之二：Kafka
date: 2015-05-10 12:43:42
tags:
- Storm
- Kafka
categories:
- Kafka
---

## __<font color='blue'>*什么是 Kafka?*__

&nbsp;&nbsp; <font size=3> Kafka is a distributed, partitioned, replicated commit log service. It provides the functionality of a messaging system, but with a unique design. - - [Official website][1]

## __<font color='blue'>*为什么要有 Kafka?*__
###分布式
```clojure
	具备经济、快速、可靠、易扩充、数据共享、设备共享、通讯方便、灵活等分布式所具备的特性
```
###高吞吐量
```clojure
	同时为发布者和订阅者提高吞吐量
```
###高可靠性
```clojure
	支持多个订阅者，当订阅失败的时候，能够自动均衡订阅者
```
###离线 & 实时性
```clojure
	能将消息持久化，进行批量处理
```

## __<font color='blue'>*Kafka 工作机制*__
### 一些主要概念
 - Topic（主题）
```scala
	A topic is a category or feed name to which messages are published.
```

 - Producers（发布者）
```scala
	Producers publish data to the topics of their choice. The producer is responsible for choosing which message to assign to which partition within the topic.
```

 - Consumers（订阅者）
```scala
	Messaging traditionally has two models: queuing and publish-subscribe. 
	In a queue, a pool of consumers may read from a server and each message goes to one of them; 
	in publish-subscribe the message is broadcast to all consumers.
```

### 示意图
![producers-cluster-consumers][2]

[1]:https://kafka.apache.org/
[2]:/../2015-5-10/kafka.png