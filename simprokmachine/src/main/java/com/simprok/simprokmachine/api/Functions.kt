//
//  Functions.kt
//  simprokmachine
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokmachine.api

typealias Handler<T> = (T) -> Unit
typealias Mapper<I, R> = (I) -> R

typealias BiHandler<T1, T2> = (T1, T2) -> Unit
typealias BiMapper<I1, I2, R> = (I1, I2) -> R

typealias TriHandler<T1, T2, T3> = (T1, T2, T3) -> Unit
typealias TriMapper<I1, I2, I3, R> = (I1, I2, I3) -> R

typealias  Supplier<T> = () -> T