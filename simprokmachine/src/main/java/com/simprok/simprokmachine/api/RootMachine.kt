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
import kotlinx.coroutines.launch

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


/**
 * Subscribes `child` machine specified in `RootMachine` and its sub-machines.
 * @param callback - receives machine's output.
 * Not recommended to be used. Exists for edge-cases.
 * Prefer using child machine that receives input and handles it.
 * @return Job - coroutine job that keeps the subscription
 */
fun <Input, Output> RootMachine<Input, Output>.start(
    scope: CoroutineScope,
    callback: Handler<Output>
): Job =
    execute(scope, child, callback)

/**
 * Subscribes `child` machine specified in `RootMachine` and its sub-machines.
 * @param callback - receives machine's output.
 * Not recommended to be used. Exists for edge-cases.
 * Prefer using child machine that receives input and handles it.
 */
suspend fun <Input, Output> RootMachine<Input, Output>.start(
    callback: Handler<Output>
) = coroutineScope {
    execute(this, child, callback)
}