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
            <version>1.0.0</version>
        </dependency>
    </dependencies>    
</project>
```

##### Configuration
Role management needs to be setup before endpoints can be exposed
```kotlin
import io.javalin.core.security.RouteRole

// Available roles
enum class MyAccessRoles : RouteRole {
    PUBLIC, ADMIN
}
// Setup roles for endpoints
JavalinAnnotation.setRoles(MyRoles.values().associateBy { it.name })
```

##### Annotations for endpoints
Endpoints to be exposed by Javalin should be annotated with

@Page, for web pages rendering using rendering engine. These methods must return a rendering map (Map<String, Any>).

@Api, for api request. These methods must return a String.

@ApiCookie, for setting cookies. These methods must return map of key/value for cookies to set (Map<String, String>).

```kotlin
package se.refur.example.first

class FirstExample {
    
    @Page(type = HandlerType.GET, path = "/page/first", templatePath = "example/first.ftl", accessRole = "PUBLIC")
    fun pageEndpoint(): Map<String, Any> = emptyMap()

    @Api(type = HandlerType.POST, path = "/api/first", accessRole = "ADMIN")
    fun apiEndpoint(): String = "success"

    @ApiCookie(type = HandlerType.POST, path = "/api/login", accessRole = "PUBLIC")
    fun apiLogin(
        @Param("userName", ParameterType.FORM) loginName: String,
        @Param("userPassword", ParameterType.FORM) loginPwd: String
    ): Map<String, String> = mapOf("authCookie" to loginUser(loginName, loginPwd))
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

    @Api(type = HandlerType.GET, path = "/api/second/{name}/{age}/{date}", accessRole = "ADMIN")
    fun apiRouteEndpoint(
        @Param("name", ParameterType.ROUTE) userName: String,
        @Param("age", ParameterType.ROUTE) userAge: Int,
        @Param("date", ParameterType.ROUTE) requestDate: LocalDate
    ): String = "success"

    @Api(type = HandlerType.GET, path = "/api/second", accessRole = "ADMIN")
    fun apiQueryEndpoint(
        @Param("name", ParameterType.QUERY) userName: String,
        @Param("age", ParameterType.QUERY) userAge: Int,
        @Param("date", ParameterType.QUERY) requestDate: LocalDate
    ): String = "success"

    @Api(type = HandlerType.POST, path = "/api/second", accessRole = "ADMIN")
    fun apiFormEndpoint(
        @Param("name", ParameterType.FORM) userName: String,
        @Param("age", ParameterType.FORM) userAge: Int,
        @Param("date", ParameterType.FORM) requestDate: LocalDate,
        @Param("active", ParameterType.FORM) isActive: Boolean
    ): String = "success"

    @Api(type = HandlerType.PUT, path = "/api/second", accessRole = "ADMIN")
    fun apiCookieEndpoint(
        @Param("auth", ParameterType.COOKIE) userToken: String
    ): String = "success"
}
```

##### Setup of Javalin endpoints using annotations
Setup endpoints from package
```kotlin
Javalin
    // web server configuration
    .create { config: JavalinConfig ->
        // ...
    }
    .exposePackageEndpoints("se.refur.example.first")
    .exposePackageEndpoints("se.refur.example.second")
    
```