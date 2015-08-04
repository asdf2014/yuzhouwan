title: Storm 与 Kafka 的整合之三：Combination
date: 2015-05-10 14:28:08
tags:
 - Storm
 - Kafka
categories:
 - Storm
 - Kafka

---


## __<font color='blue'>*搭建 Storm 和 Kafka 的基础环境*__
### __搭建 [Storm][1] 集群 (本地模式)__
### __搭建 [Kafka][2] 环境__
### __启动 Kafka__

#### - start the zookeeper and kafka server
```shell
	bin/zookeeper-server-start.sh config/zookeeper.properties
```
```shell
	bin/kafka-server-start.sh config/server.properties
```

#### - create a topic
```shell
	bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic my-replicated-topic
```
<font size=2>&nbsp;&nbsp;&nbsp;&nbsp;We can now see that topic if we run the list topic command:
```shell
	bin/kafka-topics.sh --list --zookeeper localhost:2181
```

## __<font color='blue'>*发送 Message 往 Kafka*__
### __编写 SendMessageToKafka__
1. <font size=2> 根据 kafka 中 cluster 的属性，定义好 Producer
2. <font size=2> 利用 Producer.send(KeyedMessage) 方法，将 "topic - message" 发送给 Kafka

```java
public class SendMessageToKafka {

	private static Producer<String, String> producer;

	private static void init() {
		Properties props = new Properties();
		props.put("zk.connect", "192.168.1.201:2181");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("metadata.broker.list", "192.168.1.201:9092");
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<String, String>(config);
	}

	public static void main(String... arg) {

		init();

		KeyedMessage<String, String> data = new KeyedMessage<String, String>(
				"my-replicated-topic", "asdf2015");
		producer.send(data);
		producer.close();

	}

}
```

### __run the main method__
```java
	com.yuzhouwan.hadoop.customer_behaviour_analyse.kafka.SendMessageToKafka
```

### __check out the message that tht broker catched__
```shell
	bin/kafka-console-consumer.sh --zookeeper localhost:2181 --from-beginning --topic my-replicated-topic
```
Then, u will see that the 'asdf2015' message was sent sucessfully.


## __<font color='blue'>*从 Kafka 中获得 Message*__
### __编写 TestMessageScheme__
<font size=2> 在 deserialize(byte[]) 方法中将 message 显示
```java
public class TestMessageScheme implements Scheme {

	...
	
	@Override
	public List<Object> deserialize(byte[] ser) {

		String msg;
		try {
			msg = new String(ser, "UTF-8");
			System.out.println("$$$$$$$$$$$$$$" + msg);
			return new Values(msg);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Can not parse the provide message from bytes.");
			throw new RuntimeException(e);
		}
	}

	...

}
```

### __编写 ShowKafkaMessageBolt__
1. <font size=2>在 prepare(Map, TopologyContext, OutputCollector) 中得到:OutputCollector(emit方法完成 message 的发射)、Context(提供 name/id 之类的属性)
2. <font size=2>在 execute(Tuple) 中完成 message 处理工作
```java
public class ShowKafkaMessageBolt implements IRichBolt {

	private OutputCollector collector;
	private String name;
	private int id;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
		System.out.println("Bolt: " + name + " and Id: " + id
				+ " prepared ##################");

	}

	@Override
	public void execute(Tuple input) {

		if (input != null) {
			String message = input.getString(0);
			collector.emit(new Values(message));
			System.out.println(message);
		}

		collector.ack(input);

	}

	...

}
```

### __编写 BehaviourAnalyse__
1. 基于 zookeeper属性 定义 Broker
2. 整合 broker、topic、zkRoot、spoutId 和 TestMessageScheme 为 SpoutConfig，完成 KafkaSpout 的实例化
3. 利用 LocalCluster 完成 topology 的提交
```java
public class BehaviourAnalyse {

	public static void main(String[] args) {

		BrokerHosts brokerHosts = new ZkHosts("192.168.1.201:2181");

		String topic = "my-replicated-topic";

		/**
		 * We can get the param from the 'config/zookeeper.properties' path.<BR>
		 * # the directory where the snapshot is stored.<BR>
		 * dataDir=/tmp/zookeeper
		 */
		String zkRoot = "/tmp/zookeeper";
		String spoutId = "myKafka";

		SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, topic, zkRoot,
				spoutId);
		spoutConfig.scheme = new SchemeAsMultiScheme(new TestMessageScheme());

		TopologyBuilder builder = new TopologyBuilder();
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		builder.setSpout("kafka-spout", kafkaSpout);

		builder.setBolt("show-message-bolt", new ShowKafkaMessageBolt())
				.shuffleGrouping("kafka-spout");

		Config conf = new Config();
		conf.setDebug(true);
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("Show-Message-From-Kafka", conf,
				builder.createTopology());

	}

}
```

### __run the main method__
```java
	com.yuzhouwan.hadoop.customer_behaviour_analyse.BehaviourAnalyse
```

### __start a kafka's producer__
```shell
	bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-replicated-topic
```
Input a sentence ending, like:
```shell
	This is a message from kafka.
```
You will see the information that be show in console sucessfully :-)


## __<font color='blue'>*Storm 和 Kafka 双向整合已经完成*__
完全的整合，及其运用，见 [here][4]


## __<font color='blue'>*小技巧*__
### __get the value of metadata.broker.list__
```shell
	vim config/producer.properties
```

### 查看 storm 与其他框架的支持与否
[http://mvnrepository.com/artifact/org.apache.storm][3]

<font size=2>__*[[Source]][5]*__

[1]:http://storm.apache.org/documentation/Home.html
[2]:http://kafka.apache.org/documentation.html
[3]:http://mvnrepository.com/artifact/org.apache.storm
[4]:https://github.com/MasteringStorm/shopping-web-site
[5]:https://github.com/MasteringStorm/customer-haviour-analyse
