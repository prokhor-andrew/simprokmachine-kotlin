//
//  ChildInput.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.machines

import com.simprok.simprokmachine.api.Handler
import kotlinx.coroutines.CoroutineDispatcher

/**
 * An interface that describes a machine with a customizable handling of input,
 * and emitting of output.
 */
interface ChildMachine<Input, Output> : Machine<Input, Output> {

    /**
     * Dispatcher which process() method runs on.
     */
    val dispatcher: CoroutineDispatcher

    /**
    * Triggered after the subscription to the machine and every time input is received.
    * @param input - a received input. `null` if triggered after subscription.
    * @param callback - a callback used for emitting output.
     */
    fun process(input: Input?, callback: Handler<Output>)
}