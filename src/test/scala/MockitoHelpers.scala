package com.originate.cheatsheet

import org.mockito.Mockito.mock
import org.mockito.Mockito.RETURNS_SMART_NULLS

import scala.reflect.ClassTag

trait MockitoHelpers {
  def smartMock[T]()(implicit classTag: ClassTag[T]): T =
    mock(classTag.runtimeClass, RETURNS_SMART_NULLS).asInstanceOf[T]
}
