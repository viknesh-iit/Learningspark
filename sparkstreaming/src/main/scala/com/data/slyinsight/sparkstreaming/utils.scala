package com.data.slyinsight.sparkstreaming

object example {
  
  implicit class StringUtils(val value: String) {
    def strip(stripChars: String): String = value.stripPrefix(stripChars).stripSuffix(stripChars)
  }

  
}
