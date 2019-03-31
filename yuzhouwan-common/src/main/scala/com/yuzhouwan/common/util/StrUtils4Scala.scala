package com.yuzhouwan.common.util

/**
  * Copyright @ 2019 yuzhouwan.com
  * All right reserved.
  * Function：String Utils for Scala
  *
  * @author Benedict Jin
  * @since 2018/5/30
  */
object StrUtils4Scala {

  def superFormat(str: String, detail: String*): String = {
    val holderSize: Int = countSubString(str, "%s")
    val detailSize: Int = detail.size

    var result: String = str
    if (detailSize < holderSize) {
      for (_ <- 0 until (holderSize - detailSize)) {
        result = patchSubString(result, "%s")
      }
    }
    result.format(detail: _*)
  }

  def countSubString(str: String, sub: String): Int = str.sliding(sub.length).count(slide => slide == sub)

  def patchSubString(str: String, sub: String): String = str.patch(str.lastIndexOf(sub), "", sub.length)
}
