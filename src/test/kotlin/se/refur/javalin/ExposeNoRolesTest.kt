package se.refur.javalin

import io.javalin.Javalin
import org.assertj.core.api.Assertions.assertThat
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import se.refur.javalin.sample.third.ThirdExposedClass

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExposeNoRolesTest {

    private val testPortNumber: Int = 9777

    private val javalin: Javalin = Javalin
        .create()
        .exposeClassEndpoints(ThirdExposedClass::class)

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
            .connect("http://localhost:$testPortNumber/api/third")
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .followRedirects(false)
            .execute()
        assertThat(jsoupResponse.statusCode()).isEqualTo(200)
    }

    @Test
    fun `API request _ exposed handler _ api response`() {
        val jsoupResponse: Connection.Response = Jsoup
            .connect("http://localhost:$testPortNumber/api/third")
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true)
            .followRedirects(false)
            .execute()
        assertThat(jsoupResponse.body()).isEqualTo("API response for ThirdExposedClass")
    }
}