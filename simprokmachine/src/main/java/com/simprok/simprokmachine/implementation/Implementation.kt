//
//  Implementation.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.implementation

import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.machines.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


internal fun <Input, Output> Machine<Input, Output>.pair(scope: CoroutineScope): Pair<Flow<Output>, Handler<Input>> {
    when (val machine = this) {
        is ChildMachine<Input, Output> -> {
            val flow1 = MutableSharedFlow<Input?>(replay = 1)
            val flow2 = MutableSharedFlow<Output>(replay = 1)

            val flow = merge(
                flow2,
                flow1.onStart { emit(null) }
                    .onEach {
                        machine.process(it) {
                            scope.launch(Dispatchers.IO) {
                                flow2.emit(it)
                            }
                        }
                    }
                    .flowOn(machine.dispatcher)
                    .map<Input?, Output?> { null }
                    .filter { it != null }
                    .map { it!! }
            )

            return Pair(flow) { input ->
                scope.launch(Dispatchers.IO) {
                    flow1.emit(input)
                }
            }
        }
        is ParentMachine<Input, Output> -> {
            return machine.child.pair(scope)
        }
        is MergeMachine<Input, Output> -> {
            return machine.supplier(scope)
        }
        is InwardMachine<Input, Output> -> {
            return machine.supplier(scope)
        }
        is OutwardMachine<Input, Output> -> {
            return machine.supplier(scope)
        }
        is RedirectMachine<Input, Output> -> {
            return machine.supplier(scope);
        }
    }
}

internal fun <Input, Output> execute(
    scope: CoroutineScope,
    child: Machine<Input, Output>,
    callback: Handler<Output>
) {
    scope.launch(Dispatchers.IO) {
        child.pair(this).first.collect(callback)
    }
}