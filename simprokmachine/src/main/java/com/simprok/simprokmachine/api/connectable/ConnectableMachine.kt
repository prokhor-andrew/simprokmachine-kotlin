//
//  ConnectableMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api.connectable

import com.simprok.simprokmachine.api.*
import com.simprok.simprokmachine.machines.ChildMachine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO

/**
 * The machine for dynamic creation and connection of other machines
 *
 * @param initial - initial state. After subscription to the `ConnectableMachine`
 * all the `machines` in `initial` object will be connected and subscribed to.
 * @param reducer - reducer method that is triggered every time input is received by
 * `ConnectableMachine`.
 * Accepts current state, incoming input and returns an object of `ConnectionType<C>` type.
 * Either new `machines` are connected to the `ConnectableMachine` or current `machines`
 * receive input, depending on the returned value.
 */
class ConnectableMachine<Input, Output, C : Connection<Input, Output>>(
    initial: C,
    private val reducer: BiMapper<C, Input, ConnectionType<Input, Output, C>>
) : ChildMachine<Input, Output> {

    private var map: Map<Int, NodeMachine<Input, Output>> = mapOf()

    private var state: C = initial

    /**
     * `ChildMachine` interface property
     */
    override val dispatcher: CoroutineDispatcher
        get() = IO

    /**
     * `ChildMachine` interface method
     */
    override suspend fun process(input: Input?, callback: Handler<Output>) {
        if (input != null) {
            when (val result = reducer(state, input)) {
                is ConnectionType.Reduce<Input, Output, C> -> {
                    state = result.connection
                    connect(callback)
                }
                is ConnectionType.Inward<Input, Output, C> -> send(input)
            }
        } else {
            connect(callback)
        }
    }

    private fun connect(callback: Handler<Output>) {
        map = state.machines.fold(mapOf()) { cur, machine ->
            val id = machine.hashCode()
            if (cur[id] != null) {
                cur
            } else {
                val item = map[id]
                if (item != null) {
                    cur.plus(Pair(id, item))
                } else {
                    cur.plus(Pair(id, NodeMachine(machine, callback)))
                }
            }
        }
    }

    private fun send(input: Input) {
        map.forEach { it.value.send(input) }
    }
}