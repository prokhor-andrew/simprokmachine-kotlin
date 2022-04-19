//
//  Connection.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api.connectable

import com.simprok.simprokmachine.machines.Machine

/**
 * An object that represents state in `ConnectableMachine`'s reducer
 */
interface Connection<Input, Output> {

    /**
     * Machines that are connected in `ConnectableMachine`.
     */
    val machines: Set<Machine<Input, Output>>
}