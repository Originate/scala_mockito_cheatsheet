package com.originate.cheatsheet

import org.mockito.ArgumentMatcher
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException
import org.mockito.invocation.InvocationOnMock
import org.mockito.Matchers
import org.mockito.Mockito.{times, verify, when}
import org.mockito.stubbing.Answer
import org.scalatest.FreeSpec
import org.scalatest.{Matchers => ScalaTestMatchers}

class ScalaMockitoSpec extends FreeSpec with ScalaTestMatchers with MockitoHelpers {

  class MockClass {
    def modifyString(s: String): String = s + "modified"

    def double(i: Int)(implicit b: String): Int = i * 2

    def tripleString(i: Int): String = i.toString * 3
  }

  "implicits" - {

    "when using matchers" - {

      "does not typecheck without implicits in scope" in new Context {
        def illegal = ShouldNotTypecheck(
          """when(mockedClass.double(Matchers.eq(1)))""",
          """could not find implicit value for parameter b: String"""
        )
      }

      "with an implicit in scope" - {

        implicit val implicitString = "implicit"

        "throws an exception without the implicit being explicitly matched" in new Context {
          an [InvalidUseOfMatchersException] shouldBe thrownBy {
            when(mockedClass.double(Matchers.eq(1))).thenReturn(2)
          }
        }

        "can be matched explicitly" in new Context {
          when(mockedClass.double(Matchers.eq(1))(Matchers.any[String])).thenReturn(2)
          mockedClass.double(1)
          verify(mockedClass).double(1)
        }

      }

    }

    "when using exact values" - {

      "does not typecheck without implicits in scope" in new Context {
        def illegal = ShouldNotTypecheck(
          """when(mockedClass.double(1))""",
          """could not find implicit value for parameter b: String"""
        )
      }

      "can be passed in explicitly" in new Context {
        when(mockedClass.double(1)("explicit")).thenReturn(2)
        mockedClass.double(1)("explicit")
        verify(mockedClass).double(1)("explicit")
      }

      "with an implicit in scope" - {

        implicit val implicitString = "implicit"

        "can be passed in implicitly" in new Context {
          when(mockedClass.double(1)).thenReturn(2)
          mockedClass.double(1)
          verify(mockedClass).double(1)
        }

      }

    }

  }

  "custom matchers" - {

    case class IsDivisibleBy(n: Int) extends ArgumentMatcher[Int] {
      def matches(request: Any): Boolean =
        request match {
          case i: Int if i % n == 0 => true
          case _ => false
        }
    }

    "can match arguments with arbitrary code" in new Context {
      when(mockedClass.tripleString(Matchers.any[Int])).thenReturn("not divisible by 3")
      when(mockedClass.tripleString(Matchers.argThat(IsDivisibleBy(3)))).thenReturn("multiple of 3")
      mockedClass.tripleString(2) shouldBe "not divisible by 3"
      mockedClass.tripleString(3) shouldBe "multiple of 3"
      verify(mockedClass, times(1)).tripleString(3)
    }

    "have precedence by the inverse order of stubbing" in new Context {
      when(mockedClass.tripleString(Matchers.argThat(IsDivisibleBy(3)))).thenReturn("multiple of 3")
      when(mockedClass.tripleString(Matchers.any[Int])).thenReturn("not divisible by 3")
      mockedClass.tripleString(2) shouldBe "not divisible by 3"
      mockedClass.tripleString(3) shouldBe "not divisible by 3"
    }

    "can be used to match in verify" in new Context {
      mockedClass.tripleString(6)
      verify(mockedClass).tripleString(Matchers.argThat(IsDivisibleBy(3)))
    }

  }

  "returning argument dependant values" - {

    case class Multiply(multiplier: Int) extends Answer[String] {
      override def answer(invocation: InvocationOnMock) = {
        val n = invocation.getArguments()(0).asInstanceOf[String]
        n * multiplier
      }
    }

    "when using matchers" - {

      "can depend on a value that was passed into a matcher" in new Context {
        when(mockedClass.modifyString(Matchers.any[String])).thenAnswer(Multiply(5))
        mockedClass.modifyString("2") shouldBe "22222"
      }

    }

  }

  trait Context {
    val mockedClass = smartMock[MockClass]
  }

}
