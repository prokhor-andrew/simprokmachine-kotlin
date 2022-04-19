//
//  ParentMachine.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.machines

/**
 * An interface that describes an intermediate machine that passes input from its
 * parent to the child, and its output from the child to the parent.
 */
interface ParentMachine<Input, Output> : Machine<Input, Output> {

    /**
     * A child machine that receives input that comes from the parent machine, and emits output.
     */
    val child: Machine<Input, Output>
}