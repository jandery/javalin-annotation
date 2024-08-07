# javalin-annotation

The purpose is to allow Javalin routes to be added by annotations.

## Getting started
Include the following in your POM:
```xml
<project xmlns="...">
    <repositories>
        <repository>
            <id>maven-central</id>
            <url>https://repo.maven.apache.org/maven2</url>
        </repository>
        <repository>
            <id>maven-repo.refur.se</id>
            <url>https://s3-eu-west-1.amazonaws.com/maven-repo.refur.se/release</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- Javalin extension for annotations from refur maven-repo -->
        <dependency>
            <groupId>se.refur</groupId>
            <artifactId>javalin</artifactId>
            <version>2.0.1</version>
        </dependency>
    </dependencies>    
</project>
```

### Annotations for endpoints
Endpoints to be exposed by Javalin should be annotated with

@Page, for web pages rendering using rendering engine. These methods must return a rendering map (Map<String, Any>).

@Api, for api request.

@ApiCookie, for setting cookies. These methods must return map of key/value for cookies to set (Map<String, String>).

@Download, for downloading files. These methods must return file as ByteArray.

@Upload, for uploading files. These methods must contain ByteArray parameter.

```kotlin
package se.refur.examples

data class MyDataClass(
    val name: String,
    val birthDay: LocalDate,
    val heightCm: Int,
    val isGood: Boolean
)

class PageEndpointExample {
    @Page(type = HandlerType.GET, path = "/page/first", templatePath = "example/first.ftl")
    fun pageEndpoint(): Map<String, Any> = emptyMap()
}

class ApiEndpointExample {
    // Return parameter : String
    @Api(type = HandlerType.GET, path = "/api/second/string", accessRole = "PUBLIC")
    fun apiStringHandler(): String = "API response for SecondExposedClass"

    // Return parameter : Data class
    @Api(type = HandlerType.GET, path = "/api/second/obj", accessRole = "PUBLIC")
    fun apiObjectHandler(): MyDataClass = MyDataClass(
            name = "Someone",
            birthDay = LocalDate.parse("1971-01-01"),
            heightCm = 177,
            isGood = true)
}

class ApiCookieEndpointExample {
    @ApiCookie(type = HandlerType.POST, path = "/api/login")
    fun apiLogin(
            @Param("userName", ParameterType.FORM) loginName: String,
            @Param("userPassword", ParameterType.FORM) loginPwd: String
    ): Map<String, String> = mapOf("authCookie" to loginUser(loginName, loginPwd))    
}

class DownloadEndpointExample {
    @Download(type = HandlerType.GET, path = "/file/download", contentType = ContentType.TEXT_CSV,
            downloadAs = "download.csv")
    fun downloadFile(): ByteArray {
        val values = "Column A\tColumn B\tColumn C\tColumn D\r\nRow 1A\tRow 1B\tRow 1C\tRow 1D"
        return values.toByteArray()
    }   
}

class UploadEndpointExample {
    @Upload(path = "/file/upload")
    fun uploadFile(
        @Param(paramName = "fileContent", parameterType = ParameterType.FILE) fileContent: ByteArray,
        @Param(paramName = "fileContent", parameterType = ParameterType.FILE) originalFileName: String
    ): String {
        return "file size is ${fileContent.size} for uploaded file $originalFileName"
    }
}

class CssEndpointExample {
    @Css(path = "/resources/styles.css")
    fun cssFile(): String {
        return Companion::class.java.getResourceAsStream("/css.css")?.readAllBytes()?.let { String(it) } ?: ""
    }
}

class JsEndpointExample {
    @Js(path = "/resources/main.js")
    fun jsFile(): String {
        return "setTimeout({ window.console.log('Delayed logging'); }, 200);"
    }
}
```

### Parameters for endpoints
Endpoint parameters should be annotated with @Param.
When unable to parse param to requested type, an AnnotationException will be thrown.

```javascript
// Using jQuery
$.get('/api/date', (data) => { /**/ });
$.get('/api/route/ROUTE-PARAM', (data) => { /**/ });
$.post('/api/form', { data: { FORM-PARAM: "param-value" }}).then(response => { /**/ });
$.post('/api/body', { data: 'BODY-PARAM' }).then(response => { /**/ });

// Using fetch
fetch('/api/date').then(response => response.json())then(data => { /**/ });
fetch('/api/route/ROUTE-PARAM').then(response => response.json()).then(data => { /**/ });
let form = new FormData();
form.append('FORM-PARAM', 'param-value');
fetch('/api/form', { method: 'POST', body: form }).then(response => response.json()).then(data => { /**/ });
fetch('/api/body', { method: 'POST', body: 'BODY-PARAM' }).then(response => response.json()).then(data => { /**/ });
```

```kotlin
package se.refur.examples

class ParameterExample {

    @Api(type = HandlerType.GET, path = "/api/second/{name}/{age}/{date}")
    fun apiRouteEndpoint(
        @Param("name", ParameterType.ROUTE) userName: String,
        @Param("age", ParameterType.ROUTE) userAge: Int,
        @Param("date", ParameterType.ROUTE) requestDate: LocalDate
    ): String = "success"

    @Api(type = HandlerType.GET, path = "/api/second")
    fun apiQueryEndpoint(
        @Param("name", ParameterType.QUERY) userName: String,
        @Param("age", ParameterType.QUERY) userAge: Int,
        @Param("date", ParameterType.QUERY) requestDate: LocalDate
    ): String = "success"

    @Api(type = HandlerType.POST, path = "/api/second")
    fun apiFormEndpoint(
        @Param("name", ParameterType.FORM) userName: String,
        @Param("age", ParameterType.FORM) userAge: Int,
        @Param("date", ParameterType.FORM) requestDate: LocalDate,
        @Param("active", ParameterType.FORM) isActive: Boolean
    ): String = "success"

    @Api(type = HandlerType.PUT, path = "/api/second")
    fun apiCookieEndpoint(
        @Param("auth", ParameterType.COOKIE) userToken: String
    ): String = "success"
}
```

### Endpoint access
Role management needs to be setup before endpoints can be exposed
```kotlin
import io.javalin.security.RouteRole

// Available roles
enum class MyAccessRoles : RouteRole {
    PUBLIC, ADMIN
}

// Setup roles for endpoints
JavalinAnnotation.setRoles(MyRoles.values())
// Access for endpoint
@Api(type = HandlerType.POST, path = "/api/second", accessRole = "ADMIN")
fun apiPath(): String = ""

// OR

// Setup roles for endpoints
JavalinAnnotation.setRoles(mapOf("adminRole" to MyAccessRoles.ADMIN, "publicRole" to MyAccessRoles.PUBLIC))
// Access for endpoint
@Api(type = HandlerType.POST, path = "/api/second", accessRole = "adminRole")
fun apiPath(): String = ""
```

### Setup of Javalin endpoints using annotations
Setup endpoints

```kotlin

import io.javalin.Javalin
import io.javalin.http.Handler

// Without Endpoint access
Javalin.create()
    // expose endpoints via package
    .exposePackageEndpoints("se.refur.examples")
    // expose endpoints via class
    .exposeClassEndpoints(SecondExample::class)
    // expose endpoints via Java class
    .exposeClassEndpoints(SecondExample::class.java)

// With Endpoint access, and data classes (with local-date) as return objects
Javalin
    // web server configuration
    .create { config: JavalinConfig ->
        // Access handler
        config.accessManager { handler: Handler, ctx: Context, _ -> /* Restrict access here */ handler.handle(ctx) }
        // Jackson/Json parser setup
        config.jsonMapper(
            JavalinJackson(
                ObjectMapper()
                    .registerModule(JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            )
        )
    }
    // expose endpoints via package
    .exposePackageEndpoints("se.refur.examples")
    // expose endpoints via Kotlin class
    .exposeClassEndpoints(ParameterExample::class)
    // expose endpoints via Java class
    .exposeClassEndpoints(SecondExample::class.java)


```