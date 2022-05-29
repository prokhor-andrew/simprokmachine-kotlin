# simprokmachine


## Introduction

Every application consists of the classes that refer to the other classes which refer to the other classes etc. It is a tree of classes where each one knows about its children and doesn't know about its parent.

In general, classes communicate with each other by calling methods and properties of their children to pass data "down" or by triggering a callback passing data "up" to the parent.

Moreover, we have many patterns to make this communication easier such as delegate, facade, observable, command, etc.

## Problem

Every time the communication must be organized it is up to us to decide which pattern to use, and how to handle it. This requires attention and can easily result in unexpected bugs.

## Solution

```simprokmachine``` is a framework that automates the communication between the application's components called "machines".

## How to use

Machine - is an instance in your application that receives and processes input data and may emit output. It never exists on its own but rather combined with a root machine instance.

![concept](https://github.com/simprok-dev/simprokmachine-kotlin/blob/main/images/simprokmachine.drawio.png)

To create it use ```ChildMachine``` interface.

```Kotlin
class PrinterMachine : ChildMachine<String, Unit> {

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override fun process(input: String?, callback: Handler<Unit>) {
        println(input)
    }
}
```

To start the flow use ```RootMachine``` interface in your top-level class.

```Kotlin
class MyViewModel: RootMachine<String, Unit> {
    override val machine: Machine<String, Unit>
        get() = PrinterMachine()

    override val scope: CoroutineScope
        get() = viewModelScope
}
```

and don't forget to call ```start()``` to trigger the flow.

```Kotlin
init {
    start()
}
```

This does not print anything but ```null``` because after ```start()``` is called the root is subscribed to the child machine triggering ```process()``` method with ```null``` value.

Use ```callback: Handler<Output>``` to emit output.

```Kotlin
class EmittingMachine : ChildMachine<String, Unit> {

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override fun process(input: String?, callback: Handler<Unit>) {
        if (input != null) {
            println("input: \(input)")
        } else {
            callback(Unit) // Emits output
        }
    }
}
```


To separate machines into classes instead of cluttering them up in the root, use ```ParentMachine``` interface.

```Kotlin
class IntermediateLayer: ParentMachine<String, Unit> {

    override val child: Machine<String, Unit>
        get() = PrinterMachine() 
}
```


To map or ignore input - use ```inward()```.

```Kotlin
... = machine.inward { parentInput ->
    Ward.set(ChildInput(), ChildInput()) // pass zero, one or more inputs.
}
```

To map or ignore output - use ```outward()```.


```Kotlin
... = machine.outward { childOutput ->
    Ward.set(ParentOutput(), ParentOutput()) // pass zero, one or more outputs.
}
```

To send input back to the child when output received - use ```redirect()```.

```Kotlin
... = machine.redirect { childOutput ->
    // Direction.Prop() - for pushing ChildOutput further to the root.
    // Direction.Back([ChildInput]) - for sending child inputs back to the child.
    ...
}
```

To merge more than 1 machine together - use ```merge()```.

```Kotlin
val machine1: Machine<Input, Output> = ...
val machine2: Machine<Input, Output> = ...

Machine.merge(
    machine1,
    machine2
)
```

Check out the [wiki](https://github.com/simprok-dev/simprokmachine-kotlin/wiki) for more information about API and how to use it.


## Killer-features

- Declarative way of describing your application's behavior.
- Automated concurrency management saves from race conditions, deadlocks, and headache.
- Flexible. Every existing component can become a machine.
- Modular. Every machine can be described once and reused easily.
- Cross-platform. [iOS](https://github.com/simprok-dev/simprokmachine-ios) and [Flutter](https://github.com/simprok-dev/simprokmachine-flutter) supported.


## Installation

Add this in your project's gradle file:

```groovy
implementation 'com.github.simprok-dev:simprokmachine-kotlin:1.1.3'
```

and this in your settings.gradle file:

```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

## What to check next

Check out these [tools](https://github.com/simprok-dev/simproktools-kotlin) to see an existing library of useful machines and the [architectural approach](https://github.com/simprok-dev/simprokcore-kotlin) we suggest using.
