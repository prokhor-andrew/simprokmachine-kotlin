//
//  EmitterMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api.connectable

import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.machines.ChildMachine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO

internal class EmitterMachine<Input, Output>(
    private var callback: Handler<EmitterOutput<Input, Output>>? = null
) :
    ChildMachine<EmitterInput<Input>, EmitterOutput<Input, Output>> {

    override val dispatcher: CoroutineDispatcher
        get() = IO

    override suspend fun process(
        input: EmitterInput<Input>?,
        callback: Handler<EmitterOutput<Input, Output>>
    ) {
        this.callback = callback
    }

    fun send(value: Input) {
        callback?.invoke(EmitterOutput.ToConnected(value))
    }
}