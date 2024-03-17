package se.refur.javalin.params

import io.javalin.http.Context
import io.javalin.http.UploadedFile
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.Part
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ParamParserTest {
    private val uploadedFile = "This is an uploaded file"

    private fun generatePart(): Part = mockk<Part>().also {
        every { it.inputStream } returns uploadedFile.toByteArray().inputStream()
        every { it.contentType } returns "txt"
        every { it.submittedFileName } returns "sample.txt"
        every { it.size } returns uploadedFile.toByteArray().size.toLong()
    }

    @Test
    fun routeParamAsDate_validDateAsString_isLocalDate() {
        val ctx = mockk<Context>().also {
            every { it.pathParam("date") } returns "2020-10-20"
        }
        val localDate = RouteParamAsLocalDate.getTypedValue(ctx, "date")
        assertThat(localDate).isInstanceOf(LocalDate::class.java)
    }

    @Test
    fun routeParamAsDate_invalidDateAsString_exceptionIsThrown() {
        val ctx = mockk<Context>().also {
            every { it.pathParam("date") } returns "julgran"
        }
        assertThatThrownBy {
            RouteParamAsLocalDate.getTypedValue(ctx, "date")
        }
            .isExactlyInstanceOf(Exception::class.java)
            .hasMessage("Could not parse 'date' to 'LocalDate'")
    }

    @Test
    fun queryParamAsDate_validDateAsString_isLocalDate() {
        val ctx = mockk<Context>().also {
            every { it.queryParam("date") } returns "2020-10-20"
        }
        val localDate = QueryParamAsLocalDate.getTypedValue(ctx, "date")
        assertThat(localDate).isInstanceOf(LocalDate::class.java)
    }

    @Test
    fun queryParamAsDate_invalidDateAsString_exceptionIsThrown() {
        val ctx = mockk<Context>().also {
            every { it.queryParam("date") } returns "julgran"
        }
        assertThatThrownBy {
            QueryParamAsLocalDate.getTypedValue(ctx, "date")
        }
            .isExactlyInstanceOf(Exception::class.java)
            .hasMessage("Could not parse 'date' to 'LocalDate'")
    }

    @Test
    fun formParamAsDate_validDateAsString_isLocalDate() {
        val ctx = mockk<Context>().also {
            every { it.formParam("date") } returns "2020-10-20"
        }
        val localDate = FormParamAsLocalDate.getTypedValue(ctx, "date")
        assertThat(localDate).isInstanceOf(LocalDate::class.java)
    }

    @Test
    fun formParamAsDate_invalidDateAsString_exceptionIsThrown() {
        val ctx = mockk<Context>().also {
            every { it.formParam("date") } returns "julgran"
        }
        assertThatThrownBy {
            FormParamAsLocalDate.getTypedValue(ctx, "date")
        }
            .isExactlyInstanceOf(Exception::class.java)
            .hasMessage("Could not parse 'date' to 'LocalDate'")
    }

    @Test
    fun fileParamAsByteArray_validAsByteArray_isByteArray() {
        val ctx = mockk<Context>().also {
            every { it.uploadedFile("theFile") } returns
                    UploadedFile(generatePart())
        }
        val uploadedFile = FileParamAsByteArray.getTypedValue(ctx, "theFile")
        assertThat(uploadedFile).isInstanceOf(ByteArray::class.java)
    }

    @Test
    fun fileParamAsByteArray_isNull_exceptionIsThrown() {
        val ctx = mockk<Context>().also {
            every { it.uploadedFile("theFile") } returns null
        }
        assertThatThrownBy {
            FileParamAsByteArray.getTypedValue(ctx, "theFile")
        }
            .isExactlyInstanceOf(Exception::class.java)
            .hasMessage("Could not parse 'theFile' to 'byte[]'")
    }

    @Test
    fun fileParamAsString_validAsString_isString() {
        val ctx = mockk<Context>().also {
            every { it.uploadedFile("theFile") } returns
                    UploadedFile(generatePart())
        }
        val uploadedFile = FileParamAsString.getTypedValue(ctx, "theFile")
        assertThat(uploadedFile).isInstanceOf(String::class.java)
    }

    @Test
    fun fileParamAsString_isNull_exceptionIsThrown() {
        val ctx = mockk<Context>().also {
            every { it.uploadedFile("theFile") } returns null
        }
        assertThatThrownBy {
            FileParamAsString.getTypedValue(ctx, "theFile")
        }
            .isExactlyInstanceOf(Exception::class.java)
            .hasMessage("Could not parse 'theFile' to 'String'")
    }
}