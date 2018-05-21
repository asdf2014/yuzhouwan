package com.yuzhouwan.hacker.decimal

import java.nio.ByteBuffer

import com.yuzhouwan.hacker.UnitTestStyle
import org.apache.avro.{Conversions, LogicalTypes, Schema}

/**
  * Copyright @ 2018 yuzhouwan.com
  * All right reserved.
  * Function：Double 2 BigDecimal Test
  *
  * @author Benedict Jin
  * @since 2018/5/15
  */
object Double2BigDecimalTest extends UnitTestStyle {

  def main(args: Array[String]): Unit = {

    val decimal: java.math.BigDecimal = java.math.BigDecimal.valueOf(108.1238)
    val schema: Schema = new Schema.Parser().parse(
      """
        |{
        |  "namespace": "com.yuzhouwan.bean",
        |  "type": "record",
        |  "name": "BeanA",
        |  "fields": [
        |    {"name": "id", "type": "int"},
        |    {"name": "price", "type": {"type": "bytes", "logicalType": "decimal", "precision": 8, "scale": 4}}
        |  ]
        |}
      """.stripMargin)
    val logicalType = LogicalTypes.decimal(8, 4)
    val decimalConversion = new Conversions.DecimalConversion
    val buffer = decimalConversion.toBytes(decimal, schema, logicalType)
    buffer.toString should be("java.nio.HeapByteBuffer[pos=0 lim=3 cap=3]")
    val bigDecimal = decimalConversion.fromBytes(buffer, schema, logicalType)
    bigDecimal.toString should be("108.1238")
    bigDecimal.doubleValue() should be(108.1238)

    val multiply = bigDecimal.multiply(java.math.BigDecimal.valueOf(math.pow(10, 4))).longValue
    multiply should be(1081238)

    val bb: ByteBuffer = ByteBuffer.allocate(8).putDouble("666.8".toDouble)
    val dd: Double = ByteBuffer.wrap(bb.array()).getDouble()
    dd should be(666.8)
  }
}
