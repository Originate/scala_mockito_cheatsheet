package com.originate.cheatsheet

import org.mockito.Mockito.RETURNS_SMART_NULLS
import org.scalatest.mockito.MockitoSugar

import scala.reflect.ClassTag

trait MockitoHelpers extends MockitoSugar {
  def smartMock[T <: AnyRef : ClassTag]: T = mock(RETURNS_SMART_NULLS)
}
