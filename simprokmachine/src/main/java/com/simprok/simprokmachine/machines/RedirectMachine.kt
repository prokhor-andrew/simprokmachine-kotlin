//
//  RedirectMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.machines

import com.simprok.simprokmachine.api.Direction
import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.api.Mapper
import com.simprok.simprokmachine.implementation.pair
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

internal class RedirectMachine<Input, Output>(
    internal val supplier: Mapper<CoroutineScope, Pair<Flow<Output>, Handler<Input>>>
) : Machine<Input, Output> {

    companion object {
        fun <Input, Output> create(
            child: Machine<Input, Output>,
            mapper: Mapper<Output, Direction<Input>>,
        ): Machine<Input, Output> {
            return RedirectMachine { scope ->
                val pair = child.pair(scope)
                val setter = pair.second
                val flow = pair.first.map { output ->
                    when (val direction = mapper(output)) {
                        is Direction.Back<Input> -> {
                            direction.values.forEach { setter(it) }
                            Pair(false, output)
                        }
                        is Direction.Prop<Input> -> Pair(true, output)
                    }
                }.filter { it.first }.map { it.second }

                Pair(flow, setter)
            }
        }
    }
}