//
//  EmitterOutput.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api.connectable

internal sealed interface EmitterOutput<Input, Output> {

    class ToConnected<Input, Output>(val value: Input) : EmitterOutput<Input, Output>

    class FromConnected<Input, Output>(val value: Output) : EmitterOutput<Input, Output>
}