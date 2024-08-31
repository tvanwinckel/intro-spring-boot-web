# The Dispatcher Servlet

Spring MVC, as many other web frameworks, is designed around the *Front Controller* pattern where a central *Servlet*, the *DispatcherServlet*, provides a shared algorithm for request processing / handling while actual work is performed by configurable components. 

The key components of a servlet are:
* Request Handling: Servlets handle HTTP requests from clients (usually web browsers) and generate responses (usually HTML pages).
* Lifecycle Methods: 
    * **Init():** Called once when the servlet is first loaded into memory. Used for initialization.
    * **service():** Called for each request. It determines the request type (GET, POST, etc.) and calls the appropriate method.
    * **destroy():** Called once when the servlet is taken out of service, allowing for cleanup activities.

Benefits of using servlets are:
* **Portability:** Since servlets are written in Java, they are platform-independent and can run on any server that supports the Java Servlet API.
* **Performance:** Servlets are efficient as they are loaded once and can handle multiple requests using multiple threads, avoiding the overhead of process creation.
* **Security:** Servlets can leverage the security features provided by the Java platform, including authentication, authorization, and secure communication.

A few well known servlet containers are:
* Apache Tomcat
* Jetty
* JBoss
* GlassFish

## The FrontController pattern

The Front Controller pattern is a design pattern used in web application frameworks to centralize and manage the processing of incoming requests. In the context of the Spring Framework, the Front Controller is implemented by the DispatcherServlet class.

Key concepts of the FrontController pattern are:
* Centralized Request Handling:
    * The Front Controller pattern involves a single controller (in this case, DispatcherServlet) that handles all incoming HTTP requests.
    * Instead of each request being handled by a specific controller directly, all requests pass through the front controller first.
* Decoupling Request Processing:
    * The pattern decouples the request handling logic from the actual processing logic, enabling a more modular and organized structure.
    * It allows the front controller to delegate requests to appropriate handlers (such as controllers) based on the URL, request type, etc.
* Common Processing:
    * Common tasks such as logging, security checks, and setting up request parameters can be handled in one place, reducing duplication of code.

**How the FrontController pattern works in Spring:**
* Incoming Request: When a user sends a request (e.g., by clicking a link or submitting a form), the request is intercepted by the DispatcherServlet.
* Handler Mapping: The DispatcherServlet consults a set of handler mappings to determine which controller method should handle the request. These mappings are typically based on the URL pattern.
* Controller Execution: The identified controller method is executed, processing the request and generating a response, usually in the form of a view (like a JSP page) or data (like JSON).
* View Resolution: If a view is to be rendered, the DispatcherServlet uses a view resolver to determine which view (e.g., JSP, Thymeleaf) should be used.
* Rendering the View: The chosen view is rendered, and the response is sent back to the client.

![Front Controller Pattern](https://github.com/tvanwinckel/intro-spring-boot-web/blob/main/images/SpringMVCFrontController.jpg?raw=true "Front Controller Pattern")

1. An incoming request is sent to the **Front Controller** (the Servlet)
2. The **Front Controller** decides to whom it has to hand over the request, based on the request headers.
3. The **controller** that took the request will process the request by sending it to a suitable service class.
4. After all processing is done, the **controller** creates the **model** from the Service or Data Acces layer response.
5. The **controller** sends the model to the **Front Controller**.
6. The **Dispatcher Servlet** finds the view template, using a view resolver and sends the model to it.
7. Using the **View Template** and **model**, a view page is build and sent back to the Front Controller.
8. The **Front Controller** sends out the constructed view page back to the the one who originally requested it.

## Servlet Configuration

### Java and xml

<details>
    <summary>Java config and xml examples.</summary>

    The DispatcherServlet, can to be declared and mapped according to the Servlet specification by using Java configuration or in web.xml. In turn, the DispatcherServlet uses Spring configuration to discover the delegate components it needs for request mapping, view resolution, exception handling, and more.

    The following example registers and initializes the DispatcherServlet which is auto-detected by the Servlet container. Alternatively one could exted the AbstractAnnotationConfigDispatcherServletInitializer and override specific methods:

    ```java
        public class MyWebApplicationInitializer implements WebApplicationInitializer {

        @Override
        public void onStartup(ServletContext servletCxt) {

        // Load Spring web application configuration
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.register(AppConfig.class);
        ac.refresh();

        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(ac);
        ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/app/*");
        }
        }
    ```

    ```xml
        <web-app>

        <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>

        <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/app-context.xml</param-value>
        </context-param>

        <servlet>
        <servlet-name>app</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value></param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
        </servlet>

        <servlet-mapping>
        <servlet-name>app</servlet-name>
        <url-pattern>/app/*</url-pattern>
        </servlet-mapping>

        </web-app>
    ```
</details>

### Spring Boot

Spring Boot allows us to configure the Servlet a lot easier than the methods we've seen above. It allows you to use:

* Java configuration
* Commandline arguments
* Java System properties
* OS environment variables
* Applicaiton properties

The above mentioned options are sorted by priority Spring Boot uses to select the effective configuration. By default content is served on the root context path `/`.

### Examples

#### Java Configuration

Spring Boot version 1:

```java
@Bean
public EmbeddedServletContainerCustomizer
  embeddedServletContainerCustomizer() {
    return container -> container.setContextPath("/spring-web-mvc");
}
```

Spring Boot version 2:

```java
@Bean
public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
    return factory -> factory.setContextPath("/spring-web-mvc");
}
```

#### Commandline arguments

```bash
java -jar app.jar --server.servlet.context-path=/spring-mvc-intro-context-path
```

#### Java System properties

```java
public static void main(String[] args) {
    System.setProperty("server.servlet.context-path", "/spring-web-mvc");
    SpringApplication.run(Application.class, args);
}
```

#### OS Environmetn variables

```bash
Unix:
$ export SERVER_SERVLET_CONTEXT_PATH=/spring-web-mvc

Windows:
$ set SERVER_SERVLET_CONTEXT_PATH=/spring-web-mvc
```

#### Application properties

```yml
server.servlet.context-path=/spring-web-mvc
```

## Special Bean Types

In Spring MVC, several special bean types are used to handle various aspects of the framework’s configuration and request processing. These beans play specific roles in the lifecycle of a Spring MVC application. An overview of the key special bean types used in in Spring MVC:

* **HandlerMapping**
    * Purpose: Maps HTTP requests to handler methods in your controllers based on URL patterns.
    * Role: When a request comes in, the DispatcherServlet consults the HandlerMapping to determine which controller method should handle the request.
    * Common implementations:
        * RequestMappingHandlerMapping: The most common implementation that maps requests to methods annotated with @RequestMapping.
        * SimpleUrlHandlerMapping: Maps requests to controllers based on simple URL patterns.
* **HandlerAdapter**
    * Purpose: Executes the appropriate handler method based on the request. It acts as a bridge between the DispatcherServlet and the handler method.
    * Role: After the HandlerMapping identifies the appropriate handler, the HandlerAdapter is responsible for invoking that handler’s method.
    * Common imple;emtations:
        * RequestMappingHandlerAdapter: Adapts handler methods annotated with @RequestMapping.
* **HandlerInterceptor**
    * Purpose: Allows pre-processing and post-processing of requests before and after the handler method is invoked.
    * Role: Interceptors can be used for tasks like logging, authentication checks, or modifying the response.
    * Common implementations:
        * HandlerInterceptorAdapter: A convenience class to implement only necessary methods.
* **ViewResolver**
    * Purpose: Resolves the logical view name returned by a controller into an actual view (e.g., a JSP, Thymeleaf template).
    * Role: After the controller returns a view name, the ViewResolver determines the exact view that should be rendered.
    * Common implementations:
        * InternalResourceViewResolver: Resolves view names to JSP files under a specific directory.
        * ThymeleafViewResolver: Resolves view names to Thymeleaf templates.

### HandlerMapping & HandlerAdapter

![HandlerMapping and HandlerAdapter](https://github.com/tvanwinckel/intro-spring-boot-web/blob/main/images/SpringMVCMappingAndAdapter.jpg?raw=true "HandlerMapping and HandlerAdapter")

### Interceptions

All HandlerMapping implementations support handler interceptors that are useful when you want to apply specific functionality to certain requests. Interceptors must implement *HandlerInterceptor* from the `org.springframework.web.servlet` package with three methods that should provide enough flexibility to do all kinds of pre- and post-processing:

* `preHandle`, before the actual handler is executed
* `postHandle`, after the handler is executed
* `afterCompletion`, after the complete request has finished

The `prehandle(..)` method returns a boolean value. You can use this method to break or continue the processing of the execution chain. When this method returns true, the handler execution chain continues. When it returns false, the DispatcherServlet assumes the interceptor itself has taken care of requests (and, for example, rendered an appropriate view) and does not continue executing the other interceptors and the actual handler in the execution chain

![Request Interception](https://github.com/tvanwinckel/intro-spring-boot-web/blob/main/images/SpringMVCInterceptions.jpg?raw=true "Request Interception")

Note that `postHandle(..)` is less useful with `@ResponseBody` and `ResponseEntity` methods for which the response is written and committed within the `HandlerAdapter` and before `postHandle(..)` (we will touch upon this subject later on). That means it is too late to make any changes to the response, such as adding an extra header.

#### Interception Examples

Example for preHandle

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // Some code execution
    return true;
}
```

Example for postHandle

```java
@Override
public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // Some code execution
}
```

Example for afterCompletion

```java
@Override
public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,Exception ex) {
    // Some code execution
}
```

### Exception Handling

If an exception occurs during request mapping or is thrown from a request handler the `DispatcherServlet` delegates to a chain of `HandlerExceptionResolver` beans to resolve the exception and provide alternative handling, which is typically an error response.

The contract of HandlerExceptionResolver specifies that it can return:

* A `ModelAndView` that points to an error view.
* An empty `ModelAndView` if the exception was handled within the resolver.
* `null` if the exception remains unresolved, for subsequent resolvers to try, and, if the exception remains at the end, it is allowed to bubble up to the Servlet container.

### View Resolution

Spring MVC defines the `ViewResolver` and `View` interfaces that let you render models in a browser without tying you to a specific view technology. ViewResolver provides a mapping between view names and actual views. View addresses the preparation of data before handing over to a specific view technology.

![View Resolution](https://github.com/tvanwinckel/intro-spring-boot-web/blob/main/images/SpringMVCViewResolution.jpg?raw=true "View Resolution")

## Summary

In Spring MVC, the Front Controller pattern is implemented by the DispatcherServlet, which acts as the central controller for processing all incoming requests. It manages the flow of the request, delegating to controllers and other components, making it a crucial part of the framework’s architecture. This pattern helps in organizing the application, making it more maintainable and scalable. Spring MVC offers a variaty of special beans. These special beans are crucial in the architecture of a Spring MVC application, helping manage the flow of requests, resolving views, processing file uploads, handling locales, and more. By configuring or customizing these beans, developers can fine-tune the behavior of their Spring MVC applications to meet specific needs.

![Front Controller Pattern](https://github.com/tvanwinckel/intro-spring-boot-web/blob/main/images/SpringMVCFrontController.jpg?raw=true "Front Controller Pattern")

## Exercises

* [Setting up a (custom context path)](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_1_context_path.md "Exercise 1")
* [Intercepting requests](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_2_intercepting_requests.md "Exercise 2")