//
//  Functions.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api

typealias Handler<T> = (T) -> Unit
typealias Mapper<I, R> = (I) -> R

typealias BiMapper<T1, T2, R> = (T1, T2) -> R
typealias BiHandler<T1, T2> = (T1, T2) -> Unit

typealias SuspendBiHandler<T1, T2> = suspend (T1, T2) -> Unit

typealias  Supplier<T> = () -> T