package com.example.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient

@Configuration
class WebFluxConfig {

    private val TIMEOUT: Int = 5000
    private val BASE_URL = "https://localhost:8080/"

    @Bean
    fun webClientWithTimeout(): WebClient {
        val tcpCleint = TcpClient.create()
            .noSSL()

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(HttpClient.from(tcpCleint))).build();
    }
}