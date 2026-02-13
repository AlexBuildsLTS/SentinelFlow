package com.sentinel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * SentinelFlow Entry Point.
 * Bootstraps the reactive Netty server and Spring Context.
 */
@SpringBootApplication
class SentinelFlowApplication

fun main(args: Array<String>) {
    runApplication<SentinelFlowApplication>(*args)
}
