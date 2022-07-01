//
//  Implementation.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.implementation

import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.api.MachineException
import com.simprok.simprokmachine.api.RootCallback
import com.simprok.simprokmachine.machines.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


internal fun <Input, Output> Machine<Input, Output>.pair(
    scope: CoroutineScope,
    onError: Handler<MachineException>,
): Pair<Flow<Output>, Handler<Input>> {
    when (val machine = this) {
        is ChildMachine<Input, Output> -> {
            val flow1 = MutableSharedFlow<Input?>(replay = 1)
            val flow2 = MutableSharedFlow<Output>(replay = 1)

            val flow = merge(
                flow2,
                flow1.onStart { emit(null) }
                    .onEach {
                        try {
                            withContext(machine.dispatcher) {
                                machine.process(it) {
                                    scope.launch(Dispatchers.IO) {
                                        flow2.emit(it)
                                    }
                                }
                            }
                        } catch (error: Throwable) {
                            onError(MachineException.ProcessingException(error))
                        }
                    }
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
        is ParentMachine<Input, Output> -> return machine.child.pair(scope, onError)
        is MergeMachine<Input, Output> -> return machine.supplier(scope, onError)
        is InwardMachine<Input, Output> -> return machine.supplier(scope, onError)
        is OutwardMachine<Input, Output> -> return machine.supplier(scope, onError)
        is RedirectMachine<Input, Output> -> return machine.supplier(scope, onError)
    }
}

internal fun <Input, Output> execute(
    scope: CoroutineScope,
    child: Machine<Input, Output>,
    callback: RootCallback<Output>,
): Job = scope.launch(Dispatchers.IO) {
    try {
        child.pair(this, callback::onError).first.collect(callback::onOutput)
    } catch (error: Throwable) {
        callback.onError(MachineException.SubscriptionException(error))
    }
}
