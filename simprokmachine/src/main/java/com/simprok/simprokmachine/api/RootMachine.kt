//
//  RootMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api

import com.simprok.simprokmachine.implementation.execute
import com.simprok.simprokmachine.machines.Machine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope

/**
 * An interface that describes the top-level machine of the application with all its children,
 * and manages the start/stop functions of the data flow.
 */
interface RootMachine<Input, Output> {

    /**
     * Top level machine
     */
    val child: Machine<Input, Output>
}


fun <Input, Output> RootMachine<Input, Output>.start(
    scope: CoroutineScope,
    callback: RootCallback<Output> = object : RootCallback<Output> {
        override fun onOutput(output: Output) {}

        override fun onError(error: MachineException) {}
    },
): Job {
    return execute(scope, child, callback)
}

fun <Input, Output> RootMachine<Input, Output>.startOnError(
    scope: CoroutineScope,
    callback: Handler<MachineException>,
): Job =
    execute(scope, child, object : RootCallback<Output> {
        override fun onOutput(output: Output) {}

        override fun onError(error: MachineException) = callback(error)
    })

fun <Input, Output> RootMachine<Input, Output>.startOnOutput(
    scope: CoroutineScope,
    callback: Handler<Output>,
): Job =
    execute(scope, child, object : RootCallback<Output> {
        override fun onOutput(output: Output) = callback(output)

        override fun onError(error: MachineException) {}
    })

suspend fun <Input, Output> RootMachine<Input, Output>.start(
    callback: RootCallback<Output> = object : RootCallback<Output> {
        override fun onOutput(output: Output) {}

        override fun onError(error: MachineException) {}
    },
) = coroutineScope {
    execute(this, child, callback)
}


suspend fun <Input, Output> RootMachine<Input, Output>.startOnError(
    callback: Handler<MachineException>,
) = coroutineScope {
    execute(this, child, object : RootCallback<Output> {
        override fun onOutput(output: Output) {}

        override fun onError(error: MachineException) = callback(error)
    })
}

suspend fun <Input, Output> RootMachine<Input, Output>.startOnOutput(
    callback: Handler<Output>,
) = coroutineScope {
    execute(this, child, object : RootCallback<Output> {
        override fun onOutput(output: Output) = callback(output)

        override fun onError(error: MachineException) {}
    })
}