package com.data.slyinsight.sparkstreaming

//import com.typesafe.scalalogging.LazyLogging
import java.nio.file.Files
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}

/** To run this example, run Netcat server first: <code>nc -lk 9999</code>. */
//object Windowing extends LazyLogging {
object Windowing{
  
  private val master = "local[2]"
  private val appName = "example-spark-streaming"
  private val stopWords = Set("a", "an", "the")

  private val batchDuration = Seconds(1)
  private val checkpointDir = Files.createTempDirectory(appName).toString
  private val windowDuration = Seconds(30)
  private val slideDuration = Seconds(3)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)

    val ssc = new StreamingContext(conf, batchDuration)
    ssc.checkpoint(checkpointDir)

    val lines = ssc.socketTextStream("localhost", 9999)
    WordCount.count(ssc, lines, windowDuration, slideDuration, stopWords) { (wordsCount: RDD[WordCount], time: Time) =>
      val counts = time + ": " + wordsCount.collect().mkString("[", ", ", "]")
      //logger.info(counts)
    }

    ssc.start()
    ssc.awaitTermination()
  }
}