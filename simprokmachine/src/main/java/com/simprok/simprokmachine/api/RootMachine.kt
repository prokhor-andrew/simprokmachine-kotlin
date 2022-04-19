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
import kotlinx.coroutines.cancel

/**
 * An interface that describes the top-level machine of the application with all its children,
 * and manages the start/stop functions of the data flow.
 */
interface RootMachine<Input, Output> {

    /**
     * Top level machine
     */
    val child: Machine<Input, Output>


    /**
     * The coroutine scope which the flow of the app runs on.
     * Cancelling it is equal to calling stop() method.
     */
    val scope: CoroutineScope
}


/**
 * Subscribes `child` machine specified in `RootMachine` and its sub-machines.
 * @param callback - receives machine's output.
 * Not recommended to be used. Exists for edge-cases.
 * Prefer using child machine that receives input and handles it.
 */
fun <Input, Output> RootMachine<Input, Output>.start(callback: Handler<Output>) =
    execute(scope, child, callback)

/**
 * Calls cancel() over scope.
 */
fun <Input, Output> RootMachine<Input, Output>.stop() = scope.cancel()