/**
 * Copyright (C) 2009-2012 Typesafe Inc. <http://www.typesafe.com>
 */

package akka.camel

import java.io.InputStream

import org.apache.camel.NoTypeConversionAvailableException
import akka.camel.TestSupport.{ SharedCamelSystem, MessageSugar }
import org.scalatest.FunSuite
import org.scalatest.matchers.MustMatchers

class MessageScalaTest extends FunSuite with MustMatchers with SharedCamelSystem with MessageSugar {
  implicit def camelContext = camel.context
  test("mustConvertDoubleBodyToString") {
    Message(1.4).bodyAs[String] must be("1.4")
  }

  test("mustThrowExceptionWhenConvertingDoubleBodyToInputStream") {
    intercept[NoTypeConversionAvailableException] {
      Message(1.4).bodyAs[InputStream]
    }
  }

  test("mustReturnDoubleHeader") {
    val message = Message("test", Map("test" -> 1.4))
    message.header("test").get must be(1.4)
  }

  test("mustConvertDoubleHeaderToString") {
    val message = Message("test", Map("test" -> 1.4))
    message.headerAs[String]("test").get must be("1.4")
  }

  test("mustReturnSubsetOfHeaders") {
    val message = Message("test", Map("A" -> "1", "B" -> "2"))
    message.headers(Set("B")) must be(Map("B" -> "2"))
  }

  test("mustTransformBodyAndPreserveHeaders") {
    Message("a", Map("A" -> "1")).mapBody((body: String) ⇒ body + "b") must be(Message("ab", Map("A" -> "1")))
  }

  test("mustConvertBodyAndPreserveHeaders") {
    Message(1.4, Map("A" -> "1")).withBodyAs[String] must be(Message("1.4", Map("A" -> "1")))
  }

  test("mustSetBodyAndPreserveHeaders") {
    Message("test1", Map("A" -> "1")).withBody("test2") must be(
      Message("test2", Map("A" -> "1")))

  }

  test("mustSetHeadersAndPreserveBody") {
    Message("test1", Map("A" -> "1")).withHeaders(Map("C" -> "3")) must be(
      Message("test1", Map("C" -> "3")))

  }

  test("mustAddHeaderAndPreserveBodyAndHeaders") {
    Message("test1", Map("A" -> "1")).addHeader("B" -> "2") must be(
      Message("test1", Map("A" -> "1", "B" -> "2")))

  }

  test("mustAddHeadersAndPreserveBodyAndHeaders") {
    Message("test1", Map("A" -> "1")).addHeaders(Map("B" -> "2")) must be(
      Message("test1", Map("A" -> "1", "B" -> "2")))

  }

  test("mustRemoveHeadersAndPreserveBodyAndRemainingHeaders") {
    Message("test1", Map("A" -> "1", "B" -> "2")).withoutHeader("B") must be(
      Message("test1", Map("A" -> "1")))

  }
}
