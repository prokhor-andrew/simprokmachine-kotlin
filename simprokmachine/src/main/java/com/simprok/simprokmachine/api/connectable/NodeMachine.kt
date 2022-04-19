//
//  NodeMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api.connectable

import com.simprok.simprokmachine.api.*
import com.simprok.simprokmachine.machines.Machine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO

internal class NodeMachine<Input, Output>(
    private val machine: Machine<Input, Output>,
    callback: Handler<Output>
) : RootMachine<EmitterInput<Input>, Output> {

    private val emitter: EmitterMachine<Input, Output> = EmitterMachine()
    override val scope: CoroutineScope = CoroutineScope(IO)

    init {
        start(callback)
    }

    fun send(input: Input) {
        emitter.send(input)
    }

    override val child: Machine<EmitterInput<Input>, Output>
        get() = merge(
            emitter,
            machine.outward<Input, Output, EmitterOutput<Input, Output>> {
                Ward.set(EmitterOutput.FromConnected(it))
            }.inward {
                Ward.set(it.value)
            }
        ).redirect {
            when (it) {
                is EmitterOutput.ToConnected<Input, Output> -> {
                    Direction.Back(EmitterInput(it.value))
                }
                is EmitterOutput.FromConnected<Input, Output> -> {
                    Direction.Prop()
                }
            }
        }.outward {
            when (it) {
                is EmitterOutput.ToConnected<Input, Output> -> {
                    Ward.set()
                }
                is EmitterOutput.FromConnected<Input, Output> -> {
                    Ward.set(it.value)
                }
            }
        }
}