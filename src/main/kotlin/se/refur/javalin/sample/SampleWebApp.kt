package se.refur.javalin.sample

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.security.RouteRole
import se.refur.javalin.JavalinAnnotation
import se.refur.javalin.exposeClassEndpoints
import se.refur.javalin.exposePackageEndpoints
import se.refur.javalin.sample.second.SecondExposedClass


enum class SampleRoleEnum : RouteRole { PUBLIC, PROTECTED }

fun main() {
    SampleWebApp().start(9770)
}

/**
 * The purpose of this class is show example of Javalin setup with annotations
 */
class SampleWebApp {
    init {
        JavalinAnnotation.setRoles(SampleRoleEnum.values().associateBy { it.name })
    }

    private val javalin: Javalin = Javalin
        .create()
        .beforeMatched {
            it.header("Access-Control-Allow-Origin", "*")
            it.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
            it.header("Access-Control-Allow-Headers", "Content-Type, Authorization")
        }
        // This is according to Javalin documentation
        .get("/api/sample-path") { ctx: Context ->
            ctx.json(SampleDto())
        }
        .post("/api/sample-path") { ctx: Context ->
            ctx.json(SampleDto())
        }

        // expose by package name
        .exposePackageEndpoints("se.refur.javalin.sample.first")
        // expose by Kotlin class, alternative by Java class (SecondExposedClass::class.java)
        .exposeClassEndpoints(SecondExposedClass::class)

    fun start(port: Int) {
        javalin.start(port)
    }

    fun stop() {
        javalin.stop()
    }
}

data class SampleDto(val name: String = "Magic number", val value: Int = 3)