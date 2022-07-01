package com.simprok.simprokmachine.api

interface RootCallback<Output> {

    fun onOutput(output: Output)

    fun onError(error: MachineException)
}