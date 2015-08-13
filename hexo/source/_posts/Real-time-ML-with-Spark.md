title: Real-time ML with Spark
date: 2015-08-13 19:50:21
tags:
 - Spark
 - ML
categories:
 - Spark
---


## __<font color='blue'>*什么是 Spark?*__

&nbsp;&nbsp; <font size=3> Apache Spark™ is a fast and general engine for large-scale data processing. - - [Official website][1]

<br/>

## __<font color='blue'>*为什么要有 Spark?*__
###分布式
```scala
	具备经济、快速、可靠、易扩充、数据共享、设备共享、通讯方便、灵活等分布式所具备的特性
```

###高层次抽象
```scala
	RDD (Resilient Distributed Datasets) 提供 一个可以被并行计算的 不变、分区的数据集 抽象
```

###快速计算能力
```scala
	内存计算 基于内存的迭代计算框架，能够避免计算结果落地，磁盘 I/O 所带来的瓶颈
	Machine Learning、Data mining 等多需要递归地计算，因此非常适合实现这些算法
```

###高效性能
```scala
	DAG (Directed Acyclic Grap) 利用有向无环图，构建优化任务中 父 RDD 和 子 RDD 的依赖关系  (Like Ooize)
	依赖分为两种，一个为窄依赖，如 map/filter/union 等；另一种为宽依赖，如 groupByKey 等
	在划分依赖时，join 需要额外考虑 "co-partitioner"：
	    1. 如果 RDD 和 cogroup 有相同的 数据结构，将会确定一个 OneToOneDependency
	    2. 反之，则说明 join 的时候，需要 shuffle (ShuffleDependency)
	[建议]:"wide dependencies 只有等到所有 父 partiton 计算完，并传递结束，才能继续进行下一步运算，所以应尽量减少宽依赖，避免失败后 recompute 的成本"
```

###容错性
```scala
	lineage 血统，能在计算失败的时候，将会找寻 最小重新计算损耗的 结点，而不是全部重复计算
```

<br/>

## __<font color='blue'>*Spark 核心组件*__
###Spark SQL
```scala
	同时支持 HiveQL/UDFs/SerDes 等多样性的数据源，并采用 JDBC/ODBC 等标准化连接驱动，保证其通用性
```

###Spark GrapX
```scala
	支持在 graph 或 collection 中查看数据，并提供丰富的 图形处理 API
```

###Spark Streaming
```scala
	将数据流 按 时间间隔 Duration 划分为一组连续的 RDD，这些 RDD 抽象为 DStream
	随后，通过对 DStream 这个 high-level 抽象的操作，实现对底层 标记了 时间间隙 的 RDD 组的操控
```

###Spark MLbase
```scala
	提供了对 Machine Learning 的易用、高效的实现
	总体的结构，基于 Spark，自底向上分别是，MLlib/MLI/ML Optimizer。
	    1. MLlib 这一层，设计了 本地/分布式 的矩阵，对稀疏数据的支持，多模型的训练，提供了 计算模型 和 逻辑的 API
	    2. MLI 主要任务则是 提供 表针采集器 和 逻辑规则，并进一步对 高层次 ML 编程抽象成接口
	    3. ML Optimizer 则是通过自动构建 ML 的 pipe 管道路径实现 ML 优化器的作用。同时，此优化器还解决了一个在 MLI 和 MLlib 中 表征采集器 和 ML 逻辑规则的搜索问题。
```










<br/>

![][3]
![][2]


#### 更多资源：QQ group: (<font color='blue'>Hadoop 253793003</font>)


[1]:http://spark.apache.org/
[2]:/../2015-8-13/Spark.png
[3]:/../2015-8-13/storm.jpg