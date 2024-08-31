# Extra SpringMvc features

## Functional Endpoints

Using functional endpoints in a traditional Spring MVC application is not as common as in Spring WebFlux, but it is possible. While Spring WebFlux is designed with functional programming in mind, you can still use the functional style to define routes in a Spring MVC application, though it's somewhat unconventional. You can use it with Spring MVC by defining `RouterFunction` and `HandlerFunction`.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.RequestPredicates;

@Configuration
public class FunctionalRoutingConfig {

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(RequestPredicates.GET("/api/user/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), this::getUser)
                .andRoute(RequestPredicates.POST("/api/user")
                        .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), this::createUser);
    }

    private ServerResponse getUser(ServerRequest request) {
        Long userId = Long.valueOf(request.pathVariable("id"));
        User user = new User(userId, "John Doe", "johndoe@example.com");
        return ServerResponse.ok().body(user);
    }

    private ServerResponse createUser(ServerRequest request) {
        User user = request.body(User.class);
        // Logic to save the user
        return ServerResponse.status(201).body(user);
    }
}
```

In the example above `RouterFunction` is used to define the routes, and `HandlerFunction` is the function that handles the request.` RouterFunctions.route()`: This sets up the routing. The `route()` method maps HTTP requests to handler methods. `RequestPredicates` are used to match requests based on various criteria like HTTP method, path, headers, and more. `ServerRequest` and `ServerResponse` are the request and response objects you work with, similar to `HttpServletRequest` and `HttpServletResponse` in traditional MVC.

Once you’ve set up your functional routing, you can run your Spring MVC application as usual. The functional endpoints will work alongside any traditional annotation-based controllers. And access the endpoints through the usual HTTP requests these routes will be processed by the functions defined in your FunctionalRoutingConfig class:
* GET Request: `/api/user/{id}`
* POST Request: `/api/user`

Some benefits of using Functional Endpoints are:
* Explicit Routing: Routing logic is centralized and clear.
* Flexibility: More control over the routing process, allowing for complex routing logic if needed.
* Lightweight: Less reliance on annotations, potentially improving performance slightly.

## URI Links

### UriComponentsBuilder

`UriComponentsBuilder` is a utility class in Spring MVC used to construct and manipulate URI components like paths, query parameters, and other parts of a URI in a flexible and fluent way. It is particularly useful when you need to build URIs dynamically, such as when creating links in a REST API, constructing URLs in response headers, or assembling query parameters.

`UriComponentsBuilder` allows you to dynamically build URIs by chaining methods together. This is helpful when you need to create URIs based on dynamic values like user input or data from a database. The builder follows a fluent API style, meaning you can chain method calls to incrementally build a URI. The builder alse provides methods to set different parts of a URI, such as the scheme, host, port, path, query parameters, and more.

```java
import org.springframework.web.util.UriComponentsBuilder;

public class UriBuilderExample {

    public String buildUri() {
        String uri = UriComponentsBuilder.fromUriString("http://example.com")
                                         .path("/users/{id}")
                                         .queryParam("name", "John")
                                         .buildAndExpand(123)
                                         .toUriString();
        return uri;
    }
}
```

`fromUriString("http://example.com")` initializes the builder with a base URI. While `path("/users/{id}")` appends a path to the URI, with a placeholder {id}. `queryParam("name", "John")` adds a query parameter `name=John`. And `buildAndExpand(123)` replaces the `{id}` placeholder with the value `123`. Finally `toUriString()` converts the constructed URI to a string. This all will result in: `http://example.com/users/123?name=John`.


### Encoding 

`UriComponentsBuilder` also provides support for encoding URI components to ensure they are valid according to RFC 3986.

```java
UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
    .scheme("http")
    .host("example.com")
    .path("/users/{id}")
    .queryParam("name", "John Doe & Friends");

String uri = builder.buildAndExpand(123).encode().toUriString();
```

Here `encode()` ensures that special characters (like spaces and &) are correctly encoded in the URI. Resulting in the URI becomming: `http://example.com/users/123?name=John%20Doe%20%26%20Friends`.

### Path Variables

Another feature of `UriComponentsBuilder` is that you can dynamically set path variables and query parameters:

```java
UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/products/{category}/{id}")
    .queryParam("sort", "desc")
    .queryParam("limit", 10);

String uri = builder.buildAndExpand("electronics", 42).toUriString();
```

The example above will result in the following URI: `/products/electronics/42?sort=desc&limit=10`.

### Where to use?

* **Creating Links in REST Responses:** When building hypermedia-driven REST APIs, you might use UriComponentsBuilder to construct URIs for resources dynamically.
* **Redirects:** You can use the builder to generate URIs for redirecting users to different parts of your application.
* **Building URIs from Request Context:** Often used in scenarios where the URI needs to be built relative to the current request's base URI.


## Http Cashing

HTTP caching in Spring MVC is a powerful technique that helps improve the performance and scalability of web applications by reducing the need to repeatedly send the same data over the network. It works by storing copies of resources (like HTML pages, images, or API responses) either on the client side (in the browser cache) or on intermediary caches (like CDNs or proxy servers) so that subsequent requests for the same resource can be served from the cache instead of being re-fetched from the server.

Spring MVC leverages HTTP caching headers to instruct clients and intermediaries on how to cache resources:

## Cache-Control Header

The `Cache-Control` header is the most important header for HTTP caching. It specifies directives for caching mechanisms in both requests and responses.

Examples:
* `max-age`: Defines the maximum amount of time a resource is considered fresh.
* `no-cache`: Forces revalidation with the server before using a cached resource.
* `no-store`: Prevents caching altogether.
* `must-revalidate`: Indicates that once a resource becomes stale, it must not be
* `public` or `private`: Indicates whether the response can be cached by any cache (`public`) or only by the browser (`private`).

```java
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class MyController {

    @GetMapping("/api/resource")
    @CacheControl(maxAge = 60, maxAgeUnit = TimeUnit.SECONDS)
    public String getResource() {
        return "Resource Content";
    }
}
```

This sets the `Cache-Control` header to `max-age=60`, meaning the resource can be cached for 60 seconds.

## ETag (Entity Tag) and Last Modified Headers

An `ETag` is a unique identifier assigned by the server to a specific version of a resource. It allows clients to make conditional requests, asking the server if the resource has changed (`If-None-Match` request header). If the resource hasn't changed, the server returns a `304 Not Modified status`, allowing the client to use the cached version.

The `Last-Modified` header indicates the date and time at which the resource was last changed. Clients can use the `If-Modified-Since` request header to check if the resource has been modified since the last time it was cached. If not, the server can respond with a `304 Not Modified` status.

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class MyController {

    @GetMapping("/api/resource")
    public ResponseEntity<String> getResource(@RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        String eTag = "12345"; // Normally generated dynamically based on resource content

        if (eTag.equals(ifNoneMatch)) {
            return ResponseEntity.status(304).build(); // Resource not modified
        }

        return ResponseEntity.ok()
                .eTag(eTag)
                .body("Resource Content");
    }

    @GetMapping("/api/last-modified-resource")
    public ResponseEntity<String> getResourceWithLastModified(@RequestHeader(value = "If-Modified-Since", required = false) String ifModifiedSince) {
        Instant lastModified = Instant.parse("2023-10-21T07:28:00Z");

        if (ifModifiedSince != null && Instant.parse(ifModifiedSince).isAfter(lastModified)) {
            return ResponseEntity.status(304).build(); // Resource not modified
        }

        return ResponseEntity.ok()
                .lastModified(lastModified.toEpochMilli())
                .body("Resource Content");
    }
}
```