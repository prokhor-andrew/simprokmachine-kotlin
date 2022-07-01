//
//  InwardMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.machines

import com.simprok.simprokmachine.api.*
import com.simprok.simprokmachine.implementation.pair
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

internal class InwardMachine<Input, Output>(
    internal val supplier: BiMapper<CoroutineScope, Handler<MachineException>, Pair<Flow<Output>, Handler<Input>>>,
) : Machine<Input, Output> {

    companion object {
        fun <ParentInput, ChildInput, Output> create(
            child: Machine<ChildInput, Output>,
            mapper: Mapper<ParentInput, Ward<ChildInput>>,
        ): Machine<ParentInput, Output> {
            return InwardMachine { scope, onError ->
                val pair = child.pair(scope, onError)
                val flow = pair.first
                val setter = pair.second

                Pair(flow) { parentInput ->
                    try {
                        mapper(parentInput).values.forEach {
                            setter(it)
                        }
                    } catch (error: Throwable) {
                        onError(MachineException.InwardException(error))
                    }
                }
            }
        }
    }
}
