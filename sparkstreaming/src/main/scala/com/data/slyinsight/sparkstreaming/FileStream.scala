package com.data.slyinsight.sparkstreaming

import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
 * Created by viknesh on 20161211.
 */

object FileStream {
  def main(args: Array[String]) {

    val cf = new SparkConf().setAppName("FileStream")
    val sc = new SparkContext(cf)
    val ssc = new StreamingContext(sc, Seconds(10))
    val lines = ssc.textFileStream("hdfs:///tmp/test")
    val words = lines.flatMap(_.split(" "))
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }

}
