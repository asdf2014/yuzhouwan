package com.yuzhouwan.bigdata.spark.streaming.connection

import java.io.PrintStream
import java.net.Socket

import org.apache.commons.pool2.impl.{DefaultPooledObject, GenericObjectPool}
import org.apache.commons.pool2.{BasePooledObjectFactory, ObjectPool, PooledObject}
import org.apache.spark.streaming.dstream.DStream

/**
  * Copyright @ 2016 yuzhouwan.com
  * All right reserved.
  * Function: DB Connection
  *
  * @author Benedict Jin
  * @since 2015/9/6
  */
class DBConnection {

  //  spark-streaming and connection pool implementation
  //  http://stackoverflow.com/questions/30450763/spark-streaming-and-connection-pool-implementation
}

class PooledSocketStreamPublisher[T](host: String, port: Int) extends Serializable {

  /**
    * Publish the stream to a socket.
    */
  def publish(stream: DStream[T], callback: (T) => String) =

    stream foreachRDD (rdd =>

      rdd foreachPartition { partition =>

        val pool = PrintStreamPool(host, port)

        partition foreach { event =>
          val s = pool.printStream
          s println callback(event)
        }
        pool.release()
      })
}

class ManagedPrintStream(private val pool: ObjectPool[PrintStream], val printStream: PrintStream) {
  def release() = pool.returnObject(printStream)
}

object PrintStreamPool {

  var hostPortPool: Map[(String, Int), ObjectPool[PrintStream]] = Map()
  sys.addShutdownHook {
    hostPortPool.values.foreach { pool => pool.close() }
  }

  // factory method
  def apply(host: String, port: Int): ManagedPrintStream = {

    val pool = hostPortPool.getOrElse((host, port), {
      val p = new GenericObjectPool[PrintStream](new SocketStreamFactory(host, port))
      hostPortPool += (host, port) -> p
      p
    })

    new ManagedPrintStream(pool, pool.borrowObject())
  }
}

class SocketStreamFactory(host: String, port: Int) extends BasePooledObjectFactory[PrintStream] {
  override def create() = new PrintStream(new Socket(host, port).getOutputStream)

  override def wrap(stream: PrintStream) = new DefaultPooledObject[PrintStream](stream)

  override def validateObject(po: PooledObject[PrintStream]) = !po.getObject.checkError()

  override def destroyObject(po: PooledObject[PrintStream]) = po.getObject.close()

  override def passivateObject(po: PooledObject[PrintStream]) = po.getObject.flush()
}