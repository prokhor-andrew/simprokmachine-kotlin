package com.simprok.simprokmachine.api

sealed class MachineException(val error: Throwable) {

    class InwardException internal constructor(error: Throwable) : MachineException(error)

    class OutwardException internal constructor(error: Throwable) : MachineException(error)

    class RedirectException internal constructor(error: Throwable) : MachineException(error)

    class ProcessingException internal constructor(error: Throwable) : MachineException(error)

    class SubscriptionException internal constructor(error: Throwable) : MachineException(error)
}