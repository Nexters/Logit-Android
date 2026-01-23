package com.useai.core.network

import com.launchdarkly.eventsource.background.BackgroundEventHandler
import com.launchdarkly.eventsource.background.BackgroundEventSource
import com.useai.core.network.request.StartChattingStreamRequest

internal fun interface ChattingEventSourceFactory {
    fun create(handler: BackgroundEventHandler, request: StartChattingStreamRequest): BackgroundEventSource
}
