//
//  ProcessMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api

import com.simprok.simprokmachine.machines.ChildMachine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * A class that describes a machine with an injectable processing
 * behavior over the injected object. Exists for convenience.
 */
class ProcessMachine<Input, Output> private constructor(
    private val processor: suspend (Input?, Handler<Output>) -> Unit
) : ChildMachine<Input, Output> {

    companion object {

        /**
         * @param _object - object that is passed into the injected `processor()` function.
         * @param processor - triggered when `process()` method is triggered with
         * an injected `object` passed in as the first parameter.
         */
        fun <O, Input, Output> create(
            _object: O,
            processor: suspend (O, Input?, Handler<Output>) -> Unit
        ) = ProcessMachine<Input, Output> { input, callback ->
            processor(_object, input, callback)
        }
    }

    /**
     * `ChildMachine` interface property
     */
    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    /**
     * `ChildMachine` interface method
     */
    override suspend fun process(input: Input?, callback: Handler<Output>) {
        processor(input, callback)
    }
}