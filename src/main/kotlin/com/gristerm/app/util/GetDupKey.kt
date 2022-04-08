package com.gristerm.app.util

fun getDupKey(message: String): Map<String, String> {
  val regex = "key: \\{ (.+?): \"(.+?)\"".toRegex()
  val result = regex.find(message)!!.destructured.toList()
  return mapOf(result[0] to "${result[1]} is already taken")
}
