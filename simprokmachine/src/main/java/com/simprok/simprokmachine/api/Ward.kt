//
//  Ward.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api

/**
 * A type that represents a behavior of `Machine.inward()` and `Machine.outward()` operators.
 */
class Ward<T> private constructor(val values: List<T>) {

    companion object {

        /**
         * Creates a `Ward` object with specified `values` that are sent either to the child or
         * to the root depending on the used operator.
         * @param values - sent values.
         */
        fun <T> set(vararg values: T): Ward<T> = Ward(values.toList())

        /**
         * Creates a `Ward` object with specified `values` that are sent either to the child or
         * to the root depending on the used operator.
         * @param values - sent values.
         */
        fun <T> set(values: List<T>): Ward<T> = Ward(values)

        /**
         * Creates a `Ward` object with specified `values` that are sent either to the child or
         * to the root depending on the used operator.
         * @param values - sent values.
         */
        fun <T> setArray(values: Array<T>): Ward<T> = Ward(values.toList())
    }
}