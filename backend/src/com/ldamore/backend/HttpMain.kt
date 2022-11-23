package com.ldamore.backend

import com.nbottarini.asimov.environment.Env
import io.javalin.Javalin
import io.javalin.http.Context
import org.slf4j.LoggerFactory

fun main() {
    val httpServer = HttpServer(Env["PORT"]!!.toInt())
    httpServer.start()
}

class HttpServer(private val port: Int) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val app: Javalin

    init {
        app = Javalin.create { config ->
            config.showJavalinBanner = false
            config.requestLogger(::logRequest)
            config.enableCorsForAllOrigins()
        }
        registerRoutes()
    }

    private fun registerRoutes() {
        app.get("/hello", ::helloHandler)
    }

    private fun helloHandler(ctx: Context) {
        ctx.contentType("application/json")
        val jsonResult = """{"greeting":"hello"}"""
        ctx.result(jsonResult)
    }

    fun start() {
        app.start(port)
    }

    private fun logRequest(ctx: Context, executionTimeMs: Float) {
        val sb = StringBuilder()
        sb.append(ctx.req.method)
        sb.append(" " + ctx.fullUrl())
        sb.append(" Response: " + ctx.res.status)
        sb.append(" - " + ctx.res.getHeader("content-type"))
        sb.append(" (" + executionTimeMs + "ms)")
        if (ctx.res.status >= 300 || ctx.res.status < 200) {
            sb.appendLine()
            sb.append("Request Body: " + ctx.body())
            sb.appendLine()
            sb.append("Response Body: " + ctx.resultString())
            logger.error(sb.toString())
            return
        }
        logger.info(sb.toString())
    }
}
