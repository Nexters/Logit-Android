package com.useai.core.common.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthClient
