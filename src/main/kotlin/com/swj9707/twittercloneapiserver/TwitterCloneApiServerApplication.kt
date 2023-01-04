package com.swj9707.twittercloneapiserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class TwitterCloneApiServerApplication

fun main(args: Array<String>) {
	runApplication<TwitterCloneApiServerApplication>(*args)
}
