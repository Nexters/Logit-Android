package com.useai.core.data.repository

import com.useai.core.network.source.QuestionRemoteDataSource
import javax.inject.Inject

internal class QuestionRepositoryImpl @Inject constructor(
    private val questionRemoteDataSource: QuestionRemoteDataSource
) : QuestionRepository {
}
