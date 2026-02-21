package com.useai.core.network.source

import android.util.Log
import com.launchdarkly.eventsource.MessageEvent
import com.launchdarkly.eventsource.background.BackgroundEventHandler
import com.useai.core.network.ChattingEventSourceFactory
import com.useai.core.network.api.ChattingApi
import com.useai.core.network.request.StartChattingStreamRequest
import com.useai.core.network.request.UpdateLetterRequest
import com.useai.core.network.response.ChattingHistoryResponse
import com.useai.core.network.response.ChattingStreamingResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class ChattingRemoteDataSourceImpl @Inject constructor(
    private val chattingApi: ChattingApi,
    private val chattingEventSourceFactory: ChattingEventSourceFactory,
    private val json: Json
) : ChattingRemoteDataSource {

    override fun startChattingStream(request: StartChattingStreamRequest): Flow<ChattingStreamingResponse> {
        return callbackFlow {
            class ChatEventHandler : BackgroundEventHandler {
                override fun onOpen() {
                    Log.d("Chatting", "Steaming open")
                }

                override fun onClosed() {
                    close()
                }

                override fun onMessage(
                    event: String?,
                    messageEvent: MessageEvent?
                ) {
                    Log.d("Chatting", "Streaming ${messageEvent?.data}")
                    messageEvent?.data?.let { jsonString ->
                        val response = json.decodeFromString<ChattingStreamingResponse>(jsonString)
                        trySend(response)
                    }
                }

                override fun onComment(comment: String?) {

                }

                override fun onError(t: Throwable?) {
                    Log.d("Chatting", "Streaming ${t?.message}")
                    close()
                }
            }

            val eventSource = chattingEventSourceFactory.create(ChatEventHandler(), request)
            eventSource.start()

            awaitClose {
                eventSource.close()
            }
        }
    }

    override suspend fun getChatHistory(questionId: String): ChattingHistoryResponse {
        return chattingApi.getChattingHistory(questionId)
    }

    override suspend fun updateLetter(chattingId: String, request: UpdateLetterRequest) {
        chattingApi.updateLetter(chattingId, request)
    }
}
