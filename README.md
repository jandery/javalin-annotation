# javalin-annotation

The purpose is to allow Javalin routes to be added by annotations.

##### Getting started
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
        <!-- Javalin web server from maven-central -->
        <dependency>
            <groupId>io.javalin</groupId>
            <artifactId>javalin</artifactId>
            <version>4.6.4</version>
        </dependency>
        <!-- Javalin extension for annotations from refur maven-repo -->
        <dependency>
            <groupId>se.refur</groupId>
            <artifactId>javalin</artifactId>
            <version>1.2.2</version>
        </dependency>
    </dependencies>    
</project>
```

##### Annotations for endpoints
Endpoints to be exposed by Javalin should be annotated with

@Page, for web pages rendering using rendering engine. These methods must return a rendering map (Map<String, Any>).

@Api, for api request.

@ApiCookie, for setting cookies. These methods must return map of key/value for cookies to set (Map<String, String>).

@Download, for downloading files. These methods must return file as ByteArray.

@Upload, for uploading files. These methods must contain ByteArray parameter.

```kotlin
package se.refur.example.first

data class MyDataClass(
    val name: String,
    val birthDay: LocalDate,
    val heightCm: Int,
    val isGood: Boolean
)

class FirstExample {
    
    @Page(type = HandlerType.GET, path = "/page/first", templatePath = "example/first.ftl")
    fun pageEndpoint(): Map<String, Any> = emptyMap()

    // Return parameter : String
    @Api(type = HandlerType.GET, path = "/api/second/string", accessRole = "PUBLIC")
    fun apiStringHandler(): String = "API response for SecondExposedClass"

    // Return parameter : Int
    @Api(type = HandlerType.GET, path = "/api/second/int", accessRole = "PUBLIC")
    fun apiIntHandler(): Int = 42

    // Return parameter : Boolean
    @Api(type = HandlerType.GET, path = "/api/second/bool", accessRole = "PUBLIC")
    fun apiBoolHandler(): Boolean = false

    // Return parameter : Data class
    @Api(type = HandlerType.GET, path = "/api/second/obj", accessRole = "PUBLIC")
    fun apiObjectHandler(): MyDataClass = MyDataClass(
        name = "Someone",
        birthDay = LocalDate.parse("1971-01-01"),
        heightCm = 177,
        isGood = true)

    @ApiCookie(type = HandlerType.POST, path = "/api/login")
    fun apiLogin(
        @Param("userName", ParameterType.FORM) loginName: String,
        @Param("userPassword", ParameterType.FORM) loginPwd: String
    ): Map<String, String> = mapOf("authCookie" to loginUser(loginName, loginPwd))

    @Download(type = HandlerType.GET, path = "/file/download", contentType = ContentType.TEXT_CSV,
        downloadAs = "download.csv")
    fun downloadFile(): ByteArray {
        val values = "Column A\tColumn B\tColumn C\tColumn D\r\nRow 1A\tRow 1B\tRow 1C\tRow 1D"
        return values.toByteArray()
    }

    @Upload(path = "/file/upload")
    fun uploadFile(
        @Param(paramName = "fileContent", parameterType = ParameterType.FILE) fileContent: ByteArray,
        @Param(paramName = "fileContent", parameterType = ParameterType.FILE) originalFileName: String
    ): String {
        return "file size is ${fileContent.size} for uploaded file $originalFileName"
    }
}
```

##### Parameters for endpoints
Endpoint parameters should be annotated with @Param

The following types of parameters are supported (see ParameterType enum):

ROUTE, example /api/{PARAM}

QUERY, example /api?{PARAM}=value

FORM, example jQuery.ajax({data:{strValue:"aValue",intValue:42,dateValue:"2022-10-20",boolValue:true}})

COOKIE, stored with @ApiCookie annotated method

```kotlin
package se.refur.example.second

class SecondExample {

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

##### Endpoint access
Role management needs to be setup before endpoints can be exposed
```kotlin
import io.javalin.core.security.RouteRole

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
JavalinAnnotation.setRoles(mapOf("a" to MyAccessRoles.ADMIN, "p" to MyAccessRoles.PUBLIC))
// Access for endpoint
@Api(type = HandlerType.POST, path = "/api/second", accessRole = "a")
fun apiPath(): String = ""
```

##### Setup of Javalin endpoints using annotations
Setup endpoints

```kotlin

import io.javalin.Javalin

// Without Endpoint access
Javalin.create()
// expose endpoints via package
    .exposePackageEndpoints("se.refur.example.first")
    // expose endpoints via class
    .exposeClassEndpoints(SecondExample::class)

// With Endpoint access, and data classes (with local-date) as return objects
Javalin
    // web server configuration
    .create { config: JavalinConfig ->
        // Access handler
        config.accessManager { handler, ctx, _ -> /* Restrict access here */ handler.handle(ctx) }
        // Jackson/Json parser setup
        config.jsonMapper(
            JavalinJackson(
                ObjectMapper()
                    .registerModule(JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)))
    }
    // expose endpoints via package
    .exposePackageEndpoints("se.refur.example.first")
    // expose endpoints via class
    .exposeClassEndpoints(SecondExample::class)

```