//
//  MergeMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.machines

import com.simprok.simprokmachine.api.BiMapper
import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.api.MachineException
import com.simprok.simprokmachine.api.Mapper
import com.simprok.simprokmachine.implementation.pair
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge

internal class MergeMachine<Input, Output>(
    internal val supplier: BiMapper<CoroutineScope, Handler<MachineException>, Pair<Flow<Output>, Handler<Input>>>,
) : Machine<Input, Output> {

    companion object {

        fun <Input, Output> create(machines: Set<Machine<Input, Output>>): Machine<Input, Output> {
            val copy = machines.toSet()
            return MergeMachine { scope, onError ->
                copy.fold(Pair(flowOf()) { }) { state, new ->
                    val item = new.pair(scope, onError)
                    val flow = merge(state.first, item.first)
                    val setter: Handler<Input> = {
                        state.second(it)
                        item.second(it)
                    }
                    Pair(flow, setter)
                }
            }
        }
    }
}