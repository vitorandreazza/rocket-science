package com.example.rocketscience.data.dispatchers

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: RocketScienceDispatchers)

enum class RocketScienceDispatchers {
    Default,
    IO
}