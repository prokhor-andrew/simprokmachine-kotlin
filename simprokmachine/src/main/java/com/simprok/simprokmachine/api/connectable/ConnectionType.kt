//
//  ConnectionType.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api.connectable

/**
 * A type that represents a behavior of `ConnectableMachine`.
 */
sealed interface ConnectionType<Input, Output, C : Connection<Input, Output>> {

    /**
     * Returning this value from `ConnectableMachine`'s `reducer` method ensures
     * that `machines` specified in the object of `C: Connection` type will be connected
     * and all of them will receive subscription event.
     */
    class Reduce<Input, Output, C : Connection<Input, Output>>(
        internal val connection: C
    ) : ConnectionType<Input, Output, C>


    /**
     * Returning this value from `ConnectableMachine`'s `reducer` method ensures that `machines`
     * currently being connected will receive an input object of `(C: Connection).Input` type.
     */
    class Inward<Input, Output, C : Connection<Input, Output>> : ConnectionType<Input, Output, C>
}