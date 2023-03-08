package se.refur.javalin

import io.javalin.Javalin
import io.javalin.core.security.RouteRole
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import se.refur.javalin.sample.second.SecondExposedClass

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExposeClassEndpointsTest {

    private enum class JavalinRoleEnum : RouteRole { PUBLIC }
    private val testPortNumber: Int = 9778

    init {
        JavalinAnnotation.setRoles(JavalinRoleEnum.values().associateBy { it.name })
    }


    private val javalin: Javalin = Javalin
        .create { config ->
            config.accessManager { handler, ctx, _ -> handler.handle(ctx) }
        }
        .exposeClassEndpoints(SecondExposedClass::class)

    @BeforeAll
    fun beforeAll() {
        javalin.start(testPortNumber)
    }

    @AfterAll
    fun afterAll() {
        javalin.stop()
    }

    @Test
    fun `API request exposed handler _ status code _ 200`() {
        val jsoupResponse: Connection.Response = Jsoup
            .connect("http://localhost:$testPortNumber/api/second/string")
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .followRedirects(false)
            .execute()
        assertThat(jsoupResponse.statusCode()).isEqualTo(200)
    }

    @Test
    fun `API request _ exposed string handler _ api response`() {
        val jsoupResponse: Connection.Response = Jsoup
            .connect("http://localhost:$testPortNumber/api/second/string")
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .followRedirects(false)
            .execute()
        assertThat(jsoupResponse.body()).isEqualTo("API response for SecondExposedClass")
    }

    @Test
    fun `API request _ exposed int handler _ api response`() {
        val jsoupResponse: Connection.Response = Jsoup
            .connect("http://localhost:$testPortNumber/api/second/int")
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .followRedirects(false)
            .execute()
        assertThat(jsoupResponse.body()).isEqualTo(42.toString())
    }

    @Test
    fun `API request _ exposed bool handler _ api response`() {
        val jsoupResponse: Connection.Response = Jsoup
            .connect("http://localhost:$testPortNumber/api/second/bool")
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .followRedirects(false)
            .execute()
        assertThat(jsoupResponse.body()).isEqualTo(false.toString())
    }

    @Test
    fun `API request non exposed handler _ status code _ 404`() {
        val jsoupResponse: Connection.Response = Jsoup
            .connect("http://localhost:$testPortNumber/api/first")
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .followRedirects(false)
            .execute()
        assertThat(jsoupResponse.statusCode()).isEqualTo(404)
    }
}