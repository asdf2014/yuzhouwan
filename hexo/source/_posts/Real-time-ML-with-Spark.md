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

###Spark GraphX
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
	    3. ML Optimizer 则是通过自动构建 ML 的 pipe 管道路径实现 ML 优化器的作用。
	       同时，此优化器还解决了一个在 MLI 和 MLlib 中 表征采集器 和 ML 逻辑规则的搜索问题
```

<br/>

## __<font color='blue'>*Spark 实时机器学习*__
###什么是[机器学习][4]？
```scala
	Wikipedia 给出的定义是，一个计算机科学的子领域，由 模式识别 和 人工智能中的计算机学习理论 演变而来
	探索 结构化的、可学习的规则引擎，如何用来对数据 进行训练 和 预测
```

###什么又是 Real-time 机器学习呢？
```scala
	一般性的 机器学习 的对象 是一堆 offline 的训练集，通过对这些数据的学习，来确立模型
	如果数据是快速变化的，这时就需要将 新数据 分配好权重，加入到目标训练集中；之后，将预测出来的结果，再次反馈到 数据模型中去
```

###Real-time 和 No Real-time 的本质区别在哪儿？
```scala
	因为 实时模型 是动态更新的，实现算法上，比 非实时的 ML 需要考虑，如何避免依赖 将 新数据 和 旧数据 整合在一块再计算所带来的性能问题
	更多时候，长期积累的数据，是很难再做到全量计算 (Like 多项式贝叶斯 Multinomial naive bayes, 用于处理 dataset 过大，而内存不足的情况)
```

<br/>

## __<font color='blue'>*利用 Spark 实现 Real-time ML*__
###源数据流
&nbsp;&nbsp; <font size=2> I.	利用 java.util.Random 产生满足高斯分布的随机数据，再通过 [breeze][5] 放入到 vector 中，作为 特征值
&nbsp;&nbsp; <font size=2> II.	在 generateNoisyData 中，将这个 vector 做 inner product, 并加入一点噪声数据，作为 label
```scala
    val MaxEvents = 100
    val NumFeatures = 100
    val random = new Random()

    def generateRandomArray(n: Int) = Array.tabulate(n)(_ => random.nextGaussian())

    val w = new DenseVector(generateRandomArray(NumFeatures))
    val intercept = random.nextGaussian() * 10

    def generateNoisyData(n: Int) = {
      (1 to n).map { i =>
        val x = new DenseVector(generateRandomArray(NumFeatures))

        // inner product
        val y: Double = w.dot(x)
        val noisy = y + intercept
        (noisy, x)
      }
    }
```
&nbsp;&nbsp; <font size=2> III.	通过 socket 将数据 发送到指定端口
```scala
    if (args.length != 2) {
      System.err.println("Usage: <port> <millisecond>")
      System.exit(1)
    }

    val listener = new ServerSocket(args(0).toInt)

    while (true) {
      val socket = listener.accept()

      new Thread() {

        override def run = {

          println("Got client connected from: " + socket.getInetAddress)

          val out = new PrintWriter(socket.getOutputStream(), true)
          while (true) {
            Thread.sleep(args(1).toLong)

            val num = random.nextInt(MaxEvents)
            val data = generateNoisyData(num)

            data.foreach { case (y, x) =>
              val xStr = x.data.mkString(",")
              val content = s"$y\t$xStr"

              println(content)

              out.write(content)
              out.write("\n")
            }
          }
          socket.close()
        }
      }.start()
    }
```

<br/>

###实时 Machine Learning 模型
&nbsp;&nbsp; <font size=2> I.	指定 spark-master/interval 等参数，创建 StreamingContext（此处可以利用 <font color='blue'>local\[n\]</font> 快速开发）
```scala
    if (args.length < 4) {
      System.err.println("Usage: WindowCounter <master> <hostname> <port> <interval> \n" +
        "In local mode, <master> should be 'local[n]' with n > 1")
      System.exit(1)
    }

    val ssc = new StreamingContext(args(0), "ML Analysis", Seconds(args(3).toInt))
```

&nbsp;&nbsp; <font size=2> II.	获取到发送过来的 源数据
```scala
	val stream = ssc.socketTextStream(args(1), args(2).toInt, StorageLevel.MEMORY_ONLY_SER)
```

&nbsp;&nbsp; <font size=2> III.	利用 DenseVector.zeros\[Double\] 创建全零的初始矩阵
&nbsp;&nbsp; <font size=2> IV.	使用 StreamingLinearRegressionWithSGD 创建 流式随机递归下降的线性回归 模型
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font size=2>	目前 MLlib 只支持 Streaming ( KMeans / LinearRegression / LinearRegressionWithSGD ) in [Spark 1.4.1][6]
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font size=2>	Streaming MLlib 和普通的 MLlib 没有本质上的区别，只是输入的训练集是 DStream，需要使用 foreachRDD/map 进行 训练/预测 
```scala
    val NumFeatures = 100
    val zeroVector = DenseVector.zeros[Double](NumFeatures)
    val model = new StreamingLinearRegressionWithSGD()
      .setInitialWeights(Vectors.dense(zeroVector.data))
      .setNumIterations(1)
      .setStepSize(0.01)

    val labeledStream = stream.map { event =>
      val split = event.split("\t")
      val y = split(0).toDouble
      val features = split(1).split(",").map(_.toDouble)
      LabeledPoint(label = y, features = Vectors.dense(features))
    }
```

&nbsp;&nbsp; <font size=2> V.	利用 模型 进行 train / predict
```scala
	model.trainOn(labeledStream)

    val predictAndTrue = labeledStream.transform { rdd =>
      val latest = model.latestModel()
      rdd.map { point =>
        val predict = latest.predict(point.features)
        (predict - point.label)
      }
    }
```

&nbsp;&nbsp; <font size=2> VI.	通过 MSE（Mean Squared Error） 均方差 和 RMSE（Root Mean Squared Error） 均方根误差 对模型的性能进行评估  (这里也可以使用 RegressionMetrics 来实现)
```scala
    predictAndTrue.foreachRDD { (rdd, time) =>
      val mse = rdd.map { case (err) => err * err }.mean()
      val rmse = math.sqrt(mse)
      println( s"""
                  |-------------------------------------------
                  |Time: $time
          |-------------------------------------------
                      """.stripMargin)
      println(s"MSE current batch: Model : $mse")
      println(s"RMSE current batch: Model : $rmse")
      println("...\n")
    }
```

&nbsp;&nbsp; <font size=2> VII.	启动 Spark 上下文
```scala
    ssc.start()
    ssc.awaitTermination()
```

<br/>

## __<font color='blue'>*一劳永逸了？Not at all*__
&nbsp;&nbsp;&nbsp;&nbsp; <font size=3> 一个优秀的 ML 模型，是要结合具体业务，从对数据流入的清洗，特征值维度的考量，模型类型的选择，到最终的性能的评估、监控、持续优化，都需要仔细地考究，最终才能打造出高效、稳定、精准的数据模型

<br/>

###数据

&nbsp;&nbsp; <font size=2> 对目标数据集进行处理之前，首先就是对数据的类型进行归类，是数值型、类别型、文本型，还是其他一些多媒体、地理信息等
&nbsp;&nbsp; <font size=2> 针对不同的数据，分别采取不同的处理手段，对于类别型常用 1-of-k encoding 对每个类别进行编码
&nbsp;&nbsp; <font size=2> 对于文本型，则会采用 分词、移除 stop words (的、这、地; the/and/but ...)、向量化、标准化 (避免度量单位的影响) like:


```python
	import numpy as np

	np.random.seed(42)
	x = np.random.randn(10)
	norm_x_2 = np.linalg.norm(x)
	normalized_x = x / norm_x_2
	
	print "x:\n%s" % x
	print "2-Norm of x: %2.4f" % norm_x_2
	print "Normalized x:\n%s" % normalized_x
	print "2-Norm of normalized_x: %2.4f" % np.linalg.norm(normalized_x)
```

&nbsp;&nbsp; <font size=2> 还有还多常用的数据处理方式，如 平均值、中位数、总和、方差、差值、最大值、最小值
&nbsp;&nbsp; <font size=2> 对 time 的处理的方式，还有可以加上 "时间戳"

```python
	def assign_tod(hr):
    	times_of_day = {
        	'morning' : range(7, 12),
        	'lunch' : range(12, 14),
        	'afternoon' : range(14, 18),
        	'evening' : range(18, 23),
        	'night' : range(23, 7)
    	}
    	for k, v in times_of_day.iteritems():
    	if hr in v:
		    return k

	time_of_day = hour_of_day.map(lambda hr: assign_tod(hr))
```

<br/>

###特征维度
&nbsp;&nbsp; <font size=2> 常见的一个影响模型的因素，便是没有对特征 进行标准化
```java
	(element-wise - the preceding mean vector from the feature vector) / the vector of feature standard deviations
```
&nbsp;&nbsp; <font size=2> 利用 StandarScaler 完成标准化工作
```scala
	import org.apache.spark.mllib.feature.StandardScaler

	val scaler = new StandardScaler(withMean = true, withStd = true).fit(vectors)
	val scaledData = data.map(lp => LabeledPoint(lp.label, scaler.transform(lp.features)))
```


<br/>

###调整模型

&nbsp;&nbsp; <font size=2> 首先需要在众多的模型 和 对应的算法 中找到最为适用的选择
&nbsp;&nbsp; <font size=2> 模型的类别主要有，推荐引擎、分类模型、回归模型、聚类模型 等
&nbsp;&nbsp; <font size=2> 相应的实现算法，又有（线性/逻辑/多元）回归、（随机森林）决策树、（朴素/高斯/多项式/伯努利/信念网络）贝叶斯 等
&nbsp;&nbsp; <font size=2> 在选择的时候，更多会考虑 特征值是否多维（可以尝试降维），目标类别是 multiclass，binary，还是 probability（连续值）

&nbsp;&nbsp; <font size=2> 根据 数据集的 稀疏程度 对正则化 (Regularizer) 进行调整:
```scala
	zero: 没有任何正规化操作
	L1:   SGD（Stochastic gradient descent，随机梯度下降）
	L2:   LBFGS（Limited-memory BFGS，受限的 BFGS）
	
	L2 相比 L1 更为平滑（同样，L1 可以让 稀疏的数据集 得到更 直观的模型）
	
	还有其它 求最优解 的方法，如 求全局最优解的 BGD（Batch gradient descent，批量梯度下降）
	但是，由于每次迭代都需要依据训练集中所有的数据，所以速度很慢；
	以及 CG（Conjugate gradient，共轭梯度法），但还没有被 Spark MLlib 所支持，可以在 Breeze 中找到它
```

&nbsp;&nbsp; <font size=2> 可以通过 setUpdater 将模型的 规则化算法 设置为 L1（默认为 L2）
```scala
	import org.apache.spark.mllib.optimization.L1Updater

	val svmAlg = new SVMWithSGD()
	svmAlg.optimizer.
	  setNumIterations(200).
	  setRegParam(0.1).
	  setUpdater(new L1Updater)
	val modelL1 = svmAlg.run(training)
```

&nbsp;&nbsp; <font size=2> 当然，还有举不胜举的优化方式 sorry for the limit of article's lenght :-)

<br/>


###性能评估指标

&nbsp;&nbsp; <font size=2> I. 针对不同的业务，对性能评测的手段，也需要相应取舍，毕竟有些 "宁可错杀一千" 的变态 防护系统，就需要对 recall 有较高的要求，而 precision 则相对宽松些
&nbsp;&nbsp; <font size=2> 这是便可采用 ROC（receiver operating characteristic）curve 评测引擎:
```scala
	// binary classification
	val metrics = Seq(lrModel, svmModel).map { model =>
	  val scoreAndLabels = data.map { point =>
	    (model.predict(point.features), point.label)
	  }
	  val metrics = new BinaryClassificationMetrics(scoreAndLabels)
	  (model.getClass.getSimpleName, metrics.areaUnderPR, metrics.areaUnderROC)
	}
```

&nbsp;&nbsp; <font size=2> 如果是 贝叶斯/决策树 的数据模型，则可以用 0.5 对其进行划分，转换为 binary
```scala
	// naive bayes
	val nbMetrics = Seq(nbModel).map { model =>
	  val scoreAndLabels = nbData.map { point =>
	    val score = model.predict(point.features)
	    (if (score > 0.5) 1.0 else 0.0, point.label)
	  }
	  val metrics = new BinaryClassificationMetrics(scoreAndLabels)
	  (model.getClass.getSimpleName, metrics.areaUnderPR, metrics.areaUnderROC)
	}
	
	// decision tree
	val dtMetrics = Seq(dtModel).map { model =>
	  val scoreAndLabels = data.map { point =>
	    val score = model.predict(point.features)
	    (if (score > 0.5) 1.0 else 0.0, point.label)
	  }
	  val metrics = new BinaryClassificationMetrics(scoreAndLabels)
	  (model.getClass.getSimpleName, metrics.areaUnderPR, metrics.areaUnderROC)
	}
	
	val allMetrics = metrics ++ nbMetrics ++ dtMetrics
	allMetrics.foreach { case (m, pr, roc) =>
	  println(f"$m, Area under PR: ${pr * 100.0}%2.4f%%, Area under ROC: ${roc * 100.0}%2.4f%%")
	}
```

&nbsp;&nbsp; <font size=2> II. 然而，如果是一些推荐系统，更多的希望能够了解到 大体的预测精度，则可以采用 MAP（Mean Average Precision） 平均精度均值 进行评估
```java
	MAP 同时还解决了 precision，recall，F-measure 的单点值局限性
```

<br/>

__<font size=2> *至此，相信你已经对 Spark 这个生态圈有了大致轮廓了，下面就是一步一步地 在 实践 和 深入学习中，体验大数据的乐趣啦 O(∩_∩)O~~*__

<br/>

![][3]
![][2]


#### 更多资源：QQ group: (<font color='blue'>Hadoop 253793003</font>)


[1]:http://spark.apache.org/
[2]:/../2015-8-13/Spark.png
[3]:/../2015-8-13/storm.jpg
[4]:https://en.wikipedia.org/wiki/Machine_learning
[5]:http://www.scalanlp.org/
[6]:https://github.com/apache/spark/
