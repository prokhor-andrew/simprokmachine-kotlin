//
//  Operators.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api

import com.simprok.simprokmachine.machines.*

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every input of the resulting machine is mapped into an array of new inputs and passed to the child.
 * @param mapper - a mapper that receives triggering input and
 * returns `Ward` object with new array of inputs as `values`.
 */
fun <Output, Input, R> Machine<R, Output>.inward(
    mapper: Mapper<Input, Ward<R>>
): Machine<Input, Output> {
    return InwardMachine.create(this, mapper)
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every output of the child machine is mapped into an array of new outputs and passed to the root.
 * @param mapper - a mapper that receives triggering output and
 * returns `Ward` object with new array of outputs as `values`.
 */
fun <Input, Output, R> Machine<Input, Output>.outward(
    mapper: Mapper<Output, Ward<R>>
): Machine<Input, R> {
    return OutwardMachine.create(this, mapper)
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every output of the child machine is either passed further to the root or mapped into an
 * array of new inputs and passed back to the child depending on the `Direction` value returned from `mapper`.
 * @param mapper - a mapper that receives triggering output and returns `Direction` object.
 * If `Direction.prop` returned - output is pushed further to the root.
 * If `Direction.back([Input])` returned - an array of new inputs is passed back to the child.
 */
fun <Input, Output> Machine<Input, Output>.redirect(
    mapper: Mapper<Output, Direction<Input>>
): Machine<Input, Output> {
    return RedirectMachine.create(this, mapper)
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the `machines` array
 * as well as every output of every child is passed into the resulting machine.
 * @param machines - array of machines that are merged.
 */
fun <Input, Output> merge(
    vararg machines: Machine<Input, Output>
): Machine<Input, Output> {
    return mergeArray(machines)
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the `machines` array
 * as well as every output of every child is passed into the resulting machine.
 * @param machines - array of machines that are merged.
 */
fun <Input, Output> mergeArray(machines: Array<out Machine<Input, Output>>): Machine<Input, Output> {
    return mergeSet(machines.toSet())
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the `machines` array
 * as well as every output of every child is passed into the resulting machine.
 * @param machines - list of machines that are merged.
 */
fun <Input, Output> mergeList(machines: List<Machine<Input, Output>>): Machine<Input, Output> {
    return mergeSet(machines.toSet())
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the `machines` array
 * as well as every output of every child is passed into the resulting machine.
 * @param machines - set of machines that are merged.
 */
fun <Input, Output> mergeSet(machines: Set<Machine<Input, Output>>): Machine<Input, Output> {
    return MergeMachine.create(machines)
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the `machines` array
 * as well as every output of every child is passed into the resulting machine.
 * @param machines - array of machines that are merged.
 */
fun <Input, Output> Machine.Companion.merge(
    vararg machines: Machine<Input, Output>
): Machine<Input, Output> {
    return mergeArray(machines)
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the `machines` array
 * as well as every output of every child is passed into the resulting machine.
 * @param machines - array of machines that are merged.
 */
fun <Input, Output> Machine.Companion.mergeArray(machines: Array<out Machine<Input, Output>>): Machine<Input, Output> {
    return mergeSet(machines.toSet())
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the `machines` array
 * as well as every output of every child is passed into the resulting machine.
 * @param machines - list of machines that are merged.
 */
fun <Input, Output> Machine.Companion.mergeList(machines: List<Machine<Input, Output>>): Machine<Input, Output> {
    return mergeSet(machines.toSet())
}

/**
 * Creates a `Machine` instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the `machines` array
 * as well as every output of every child is passed into the resulting machine.
 * @param machines - set of machines that are merged.
 */
fun <Input, Output> Machine.Companion.mergeSet(machines: Set<Machine<Input, Output>>): Machine<Input, Output> {
    return MergeMachine.create(machines)
}
