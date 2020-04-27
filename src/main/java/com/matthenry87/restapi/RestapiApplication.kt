package com.matthenry87.restapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class RestapiApplication {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<RestapiApplication>(*args)
        }
    }
}