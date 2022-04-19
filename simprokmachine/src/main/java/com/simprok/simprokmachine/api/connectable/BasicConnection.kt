//
//  BasicConnection.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api.connectable

import com.simprok.simprokmachine.machines.Machine

/**
 * Basic implementation of `Connection` interface.
 */
data class BasicConnection<Input, Output>(
    override val machines: Set<Machine<Input, Output>>
) : Connection<Input, Output> {

    companion object {

        /**
         * Creates `BasicConnection` from an array.
         * @param machines - `Connection`'s interface property.
         */
        fun <Input, Output> create(
            vararg machines: Machine<Input, Output>
        ): BasicConnection<Input, Output> = BasicConnection(machines.toSet())

        /**
         * Creates `BasicConnection` from an array.
         * @param machines - `Connection`'s interface property.
         */
        fun <Input, Output> createArray(
            machines: Array<Machine<Input, Output>>
        ): BasicConnection<Input, Output> = BasicConnection(machines.toSet())

        /**
         * Creates `BasicConnection` from a list.
         * @param machines - `Connection`'s interface property.
         */
        fun <Input, Output> createList(
            machines: List<Machine<Input, Output>>
        ): BasicConnection<Input, Output> = BasicConnection(machines.toSet())
    }
}