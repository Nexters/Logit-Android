package com.useai.core.model.error

abstract class RootError: Exception() {
    open val code: Int = 0

    abstract fun createErrorInstances(): Array<RootError> // TODO: KSP를 활용한 자동화
}