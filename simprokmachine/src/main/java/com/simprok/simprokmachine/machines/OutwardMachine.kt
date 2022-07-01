//
//  OutwardMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.machines

import com.simprok.simprokmachine.api.*
import com.simprok.simprokmachine.implementation.pair
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat

internal class OutwardMachine<Input, Output>(
    internal val supplier: BiMapper<CoroutineScope, Handler<MachineException>, Pair<Flow<Output>, Handler<Input>>>
) : Machine<Input, Output> {

    companion object {
        fun <ParentOutput, ChildOutput, Input> create(
            child: Machine<Input, ChildOutput>,
            mapper: Mapper<ChildOutput, Ward<ParentOutput>>,
        ): Machine<Input, ParentOutput> {
            return OutwardMachine { scope, onError ->
                val pair = child.pair(scope, onError)
                val setter = pair.second
                val flow = pair.first.flatMapConcat {
                    try {
                        mapper(it).values.asFlow()
                    } catch (error: Throwable) {
                        onError(MachineException.OutwardException(error))
                        emptyList<ParentOutput>().asFlow()
                    }
                }

                Pair(flow, setter)
            }
        }
    }
}

