# Controllers

A Spring MVC controller is a component in the Spring Framework that handles incoming HTTP requests, processes them, and returns a response. In essence, it acts as the intermediary between the user interface and the business logic of a web application. Controllers in Spring MVC are responsible for directing the flow of the application by interpreting user input and rendering the appropriate response.

Key Concepts of a Spring MVC Controller:
* Controller Class:
    * A controller is a Java class annotated with @Controller, `@RestController`, or even `@Component` (if manually defined in a configuration).
    * The controller class contains methods that handle specific types of requests, typically annotated with `@RequestMapping`, `@GetMapping`, `@PostMapping`, ...
* Request Mapping:
    * Each method in a controller is mapped to a specific URL or HTTP method using annotations like `@RequestMapping`, `@GetMapping`, `@PostMapping`, ...
    * These annotations define how HTTP requests are routed to the appropriate method based on the URL pattern, HTTP method (GET, POST, etc.), request parameters, and more.
* Method Parameters:
    * Controller methods can accept parameters that bind to various parts of the HTTP request, such as query parameters, path variables, form data, headers, and cookies.
    * Common annotations used for method parameters include `@RequestParam`, `@PathVariable`, `@RequestBody`, `@ModelAttribute`, ...
* Returning Responses:
    * A controller method can return different types of responses:
        * View Name: A logical view name (e.g., a JSP or Thymeleaf template) that the ViewResolver will resolve.
        * ModelAndView: An object that contains both model data and view information.
        * Data: If the method returns data directly (e.g., a String, JSON, or XML), it’s typically annotated with @ResponseBody (or uses `@RestController`, which combines `@Controller` and `@ResponseBody`).
    * HTTP Status: The controller can also set the HTTP status code for the response.
* Data Binding and Validation:
    * Spring MVC automatically binds form data to Java objects using the data binding mechanism.
    * Validation annotations like @Valid or @Validated can be used to enforce validation rules on the data before processing.

Types of controllers:
* Standard Controller (`@Controller`): Typically returns views and handles form submissions.
* RESTful Controller (`@RestController`): A specialized version of `@Controller` that combines `@Controller` and `@ResponseBody`, used for creating RESTful web services by returning data directly (e.g., JSON, XML).

```java
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile/{id}")
    public String getUserProfile(@PathVariable("id") Long id, Model model) {
        // Simulate fetching user data from a service or repository
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "userProfile";  // View name (e.g., userProfile.jsp or userProfile.html)
    }

    @PostMapping("/update")
    public String updateUserProfile(User user) {
        // Simulate updating user data
        userService.update(user);
        return "redirect:/user/profile/" + user.getId();  // Redirect to the profile page
    }
}
```

<details>
    <summary>Example explenation</summary>

    Breakdown of the Example:
    * Annotations:
        * `@Controller`: Declares the class as a Spring MVC controller.
        * `@RequestMapping("/user")`: Maps all methods in this controller to URLs starting with /user.
        * `@GetMapping("/profile/{id}")`: Maps the method to handle GET requests for the URL /user/profile/{id}.
        * `@PostMapping("/update")`: Maps the method to handle POST requests for the URL /user/update.
    * Method Parameters:
        * `@PathVariable("id")`: Extracts the id from the URL and binds it to the method parameter.
        * `Model model`: Used to pass data (the user object) to the view.
    * Returning Views:
        * The `getUserProfile` method returns a view name (userProfile), which will be resolved by a view resolver to a specific template.
        * The `updateUserProfile` method returns a redirect instruction, sending the user back to their profile page after the update.
</details>

## Request Mapping

`@RequestMapping` is a fundamental annotation in Spring MVC used to map web requests to specific handler methods or classes. It serves as a way to define the routes or URLs that the application should respond to and allows developers to specify the type of request (GET, POST, etc.), the URL pattern, and additional attributes like headers and parameters.

The key features of this annotation are:
* URL Mapping
* HTTP Method Mapping
* Providing flexible URL Patterns
* Working with Request Parameters and Headers
* Working with Consumes and Produces

### URL Mapping

The primary function of `@RequestMapping` is to associate a URL pattern with a specific method in a controller. It can be used at both the class level and the method level.

```java
@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("/welcome")
    public String welcomePage() {
        return "welcome";  // Returns the view name "welcome"
    }
}
```

The class-level `@RequestMapping("/home")` means all methods in this controller will be mapped under `/home`. The method-level `@RequestMapping("/welcome")` maps /home/welcome to the `welcomePage` method.

There are also HTTP method specific shortcut variants of `@RequestMapping`:

* `@GetMapping`
* `@PostMapping`
* `@PutMapping`
* `@DeleteMapping`
* `@PatchMapping`

Example of class and method level mapping:

```java
@Controller
@RequestMapping("/persons")
class PersonController {

 @GetMapping
 public Person getPerson() {
  // ...
 }

 @PostMapping
 public void add(@RequestBody Person person) {
  // ...
 }
}
```

### Flexible URL Patterns

`@RequestMapping` supports dynamic URL patterns using placeholders like `{}`. These placeholders can be mapped to method parameters using `@PathVariable`.

```java
 @GetMapping("/{itemId}")
 public Item getItem(@PathVariable Long itemId) {
  // ...
 }
```

Note that URI variables can also be declared on class level. URI variables are automatically converted to the appropriate type, or a `TypeMismatchException` is raised. Simple types (`int`, `long`, `Date`, and so on) are supported by default.
You can explicitly name URI variables, but you can leave that detail out if the names are the same.

The example given below shows how an URI variable can be auto-mapped or mapped based on name:

```java
  @GetMapping("/{itemId}")
 public Item getItem(@PathVariable Long itemId) {
  // ...
 }

  @GetMapping("/{obscureVariableName}")
 public Person getItem(@PathVariable("obscureVariableName") Long itemId) {
  // ...
 }
```

You can also use wildcards in URL patterns to handle requests with similar structures. These can requests can be mapped by using the following global patterns and wildcards:

* `?` matches one character
* `*` matches zero or more characters within a path segment
* `**` match zero or more path segments

Example for the `?` pattern:

```java
@GetMapping("/t?st")
public String questionMarkPattern(final HttpServletRequest request, final Model model) {
   model.addAttribute("pattern", "Question mark pattern: " + request.getRequestURI());
   return "pattern";
}

Accepted Patterns:
  - test
  - tost
  - tast
  - tist
  - txst
```

Example for the `*` pattern:

```java
@GetMapping("*")
public String starPattern(final HttpServletRequest request, final Model model) {
   model.addAttribute("pattern", "Star pattern: " + request.getRequestURI());
   return "pattern";
}

Accepted Patterns:
  - /path 
  - /another-Path
  - /file.txt
```

Example for the `**` pattern:

```java
@GetMapping("**")
public String doubleStarPattern(final HttpServletRequest request, final Model model) {
   model.addAttribute("pattern", "Double star pattern: " + request.getRequestURI());
   return "pattern";
}

Accepted Patterns:
  - /path 
  - /another/path
  - /a/very/long/path/with/a/file.txt
```

#### Exercise

* [Request mapping](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_3_request_mapping.md "Exercise 3")

### Parameters and Headers

The `@RequestMapping` annotation can be configured to match requests based on specific parameters or headers using the `params` and `headers` attributes. This allows fine-grained control over which requests a particular method should handle.

In the example below, the method will only be invoked if the request URL includes a `query` parameter, e.g., `/search?query=spring`.

```java
@RequestMapping(value = "/search", params = "query")
public String search(@RequestParam("query") String query, Model model) {
    List<Result> results = searchService.search(query);
    model.addAttribute("results", results);
    return "searchResults";
}
```

```java
@GetMapping(path = "/items/{itemId}", headers = "myHeader") 
public Item getItem(@PathVariable String itemId, String myHeader) {
 // ...
}
```

### Consumes and Produces

The `consumes` attribute specifies the content type (MIME type) that the method can accept. The `produces` attribute defines the content type that the method will return.

The `consumes` attribute also supports negation expressions — for example, `!text/plain` means any content type other than `text/plain`.
You can declare a shared consumes attribute at the class level. However, when used at the class level, a method-level consumes attribute overrides rather than extends the class-level declaration.

> `MediaType` provides constants for commonly used media types, such as `APPLICATION_JSON_VALUE` and `APPLICATION_XML_VALUE`.

Negated expressions are also supported — for example, `!text/plain` means any content type other than `text/plain`.
You can declare a shared `produces` attribute at the class level. However, when used at the class level, a method-level produces attribute overrides rather than extends the class-level declaration.

```java
@PostMapping(path = "/items", consumes = "application/json") 
public void addItem(@RequestBody(name = "item") Item item) {
 // ...
}

@GetMapping(path = "/items/{itemId}", produces = "application/json") 
@ResponseBody
public Item getItem(@PathVariable(name = "itemId") String itemId) {
 // ...
  return Item(...)
}

@RequestMapping(value = "/json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
@ResponseBody
public User processJson(@RequestBody User user) {
    // Process the incoming JSON data and return a User object
    return user;
}
```

For the last example, the method expects a `POST` request with JSON data (`consumes = "application/json"`) and will return a response in JSON format (`produces = "application/json"`).

### Handler Methods

`@RequestMapping` handler methods have a flexible signature and can choose from a range of supported controller method arguments and return values.

**Method Arguments**

| Controller method argument | Description |
| --- | --- |
| @RequestParam | For access to the Servlet request parameters, including multipart files. Parameter values are converted to the declared method argument type. |
| @RequestHeader | For access to request headers. Header values are converted to the declared method argument type. |
| @RequestBody | For access to the HTTP request body. |
| @ModelAttribute | For access to an existing attribute in the model (instantiated if not present) with data binding and validation applied. |
| @PathVariable | For access to URI template variables. |
| HttpMethod | The HTTP method of the request. |

##### @RequestParam

You can use the `@RequestParam` annotation to bind Servlet request parameters (that is, query parameters or form data) to a method argument in a controller.

The following example shows how to do so:

```java
@Controller
@RequestMapping("/items")
public class EditItemForm {

 @GetMapping
 public String setupForm(@RequestParam("myParam") String myParam) { 
  // ...
 }
}
```

By default, method parameters that use this annotation are required, but you can specify that a method parameter is optional by setting the `@RequestParam` annotation’s required flag to false or by declaring the argument with an `java.util.Optional wrapper`.

##### @RequestHeader

You can use the `@RequestHeade`r annotation to bind a request header to a method argument in a controller.

```txt
Host                    localhost:8080
Accept                  text/html,application/xhtml+xml,application/xml;q=0.9
Accept-Language         fr,en-gb;q=0.7,en;q=0.3
Accept-Encoding         gzip,deflate
Accept-Charset          ISO-8859-1,utf-8;q=0.7,*;q=0.7
Keep-Alive              300
```

The following example gets the value of the `Accept-Encoding` and `Keep-Alive` headers:

```java
@GetMapping("/request-header")
public void handle(@RequestHeader("Accept-Encoding") String encoding, 
                   @RequestHeader("Keep-Alive") long keepAlive) { 
 //...
}
```

##### @Requestbody

You can use the `@RequestBody` annotation to have the request body read and deserialized into an Object through an `HttpMessageConverter`. The following example uses a `@RequestBody` argument:

```java
@PostMapping("/request-body")
public void handle(@RequestBody Item item) {
 // ...
}
```

##### @ModelAttribute

You can use the `@ModelAttribute` annotation on a method argument to access an attribute from the model or have it be instantiated if not present. The model attribute is also overlain with values from HTTP Servlet request parameters whose names match to field names. This is referred to as data binding, and it saves you from having to deal with parsing and converting individual query parameters and form fields.

```java
@PostMapping("/owners/{ownerId}/pets/{petId}/edit")
public String processSubmit(@ModelAttribute Pet pet) { 
  // ...
}
```

**Return Values**

| Controller method return value | Description |
| --- | --- |
| @ResponseBody | The return value is converted through HttpMessageConverter implementations and written to the response. |
| HttpEntity<B>, ResponseEntity<B> | The return value that specifies the full response (including HTTP headers and body) |
| HttpHeaders | For returning a response with headers and no body. |
| String | A view name to be resolved with ViewResolver implementations and used together with the implicit model. |
| Model & View (ModelAndView) | The view and model attributes to use and, optionally, a response status. |

##### @ResponseBody

You can use the `@ResponseBody` annotation on a method to have the return serialized to the response body through an `HttpMessageConverter`. The following listing shows an example:

```java
@GetMapping("/accounts/{id}")
@ResponseBody
public Account handle() {
 // ...
}
```

##### HttpEntity

HttpEntity is more or less identical to using `@ResponseBody` but is based on a container object that exposes request headers and body.

```java
@PostMapping("/accounts")
public void handle(HttpEntity<Account> entity) {
 // ...
}
```

#### Exercises

* [Parameters and headers](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_4_parameters_and_headers.md "Exercise 4")
* [Consume and produce](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_5_consume_and_produce.md "Exercise 5")

## Models & Views

### Model

The purpose of a model in Spring MVC is to hold the data that you want to display on the view. It represents the state of the application and is typically populated by the controller. The model is often a `Map` or a `Model` object that stores key-value pairs. The keys are the names that will be accessible in the view, and the values are the objects containing the data.

```java
@Controller
public class UserController {
    
    @GetMapping("/profile")
    public String getUserProfile(Model model) {
        Map<String, String> map = new HashMap<>();
        map.put("spring", "mvc");

        User user = userService.getCurrentUser();
        model.addAttribute("user", user);  // Add user object to the model
        model.mergeAttributes(map); // Map object is added to the model
        return "userProfile";  // Return the view name
    }
}
```

The `UserController` adds a `User` object to the `Model` under the key "user". This data can then be accessed in the view (`userProfile.jsp` or `userProfile.html`) and displayed to the user.

### ModelMap

`ModelMap` is a specialized `Map` implementation that provides convenient methods for adding attributes to the model. It is similar to `Model`, but offers additional capabilities, such as chaining methods.

```java
@Controller
public class UserController {
    
    @GetMapping("/profile")
    public String getUserProfile(ModelMap map) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);  // Add user object to the model
        return "userProfile";  // Return the view name
    }
}
```

### ModelAndView

ModelAndView combines both the model data and the view name into a single object. This allows you to return both from a controller method. The following example shows that the `ModelAndView` object is used to encapsulate the view name (`userProfile`) and the model data (`user`) in a single return value.

```java
@Controller
public class UserController {

    @GetMapping("/profile")
    public ModelAndView getUserProfile() {
        User user = userService.getCurrentUser();
        ModelAndView mav = new ModelAndView("userProfile");  // View name
        mav.addObject("user", user);  // Add object to the model
        return mav;
    }
}
```

### Model Attributes

A controller can have any number of `@ModelAttribute` methods. All such methods are invoked before `@RequestMapping` methods in the same controller. A `@ModelAttribute` method can also be shared across controllers through `@ControllerAdvice`.
`@ModelAttribute` methods have flexible method signatures. They support many of the same arguments as `@RequestMapping` methods, except for `@ModelAttribute` itself or anything related to the request body.

You can use the `@ModelAttribute` annotation:

* On a method argument in `@RequestMapping` methods to create or access an Object from the model
* As a method-level annotation in `@Controller` or `@ControllerAdvice` classes that help to initialize the model prior to any `@RequestMapping` method invocation.
* On a `@RequestMapping` method to mark its return value is a model attribute.

```java
@Controller
public class EmployeeController {

    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST)
    public String submit(@ModelAttribute("employee") Employee employee, ModelMap model) {
        // ...
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("msg", "Hello World!");
    }
}
```

#### Exercise

* [Models](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_6_models.md "Exercise 6")

## Exceptions

### Simple Exception Handling

Exception handling is a solution that allows you to manage errors and unexpected situations in a clean and consistent manner. Spring provides a robust mechanism for handling exceptions through the use of the `@ExceptionHandler` annotation, which allows you to define custom responses for specific exceptions.

The `@ExceptionHandler` annotation is used to define a method that will handle exceptions thrown during request processing. This method can be placed in a controller or a global exception handler class. When an exception is thrown in a controller method, Spring will look for an `@ExceptionHandler` method that matches the exception type and will invoke it to handle the error.

```java
@Controller
public class UserController {

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        model.addAttribute("user", user);
        return "userProfile";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";  // Return error view
    }
}
```

In the example above we see that if the `getUser` method throws a `UserNotFoundException`, the `handleUserNotFound` method will be invoked. And the error message is added to the model, and the `errorPage` view is returned.

### Global Exception Handling

The `@ControllerAdvice` annotation (We will touch this subject in the next topic) is used to define a global exception handler that applies to multiple controllers or the entire application. It centralizes exception handling logic, making it easier to manage and maintain.

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "errorPage";
    }
}
```

### Custom Error Pages

In some cases, you may want to customize the error pages displayed to the user for specific HTTP status codes (e.g., 404, 500). This can be achieved using the `SimpleMappingExceptionResolver`.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public SimpleMappingExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties exceptionMappings = new Properties();
        exceptionMappings.put("UserNotFoundException", "userNotFoundPage");
        exceptionMappings.put("java.lang.Exception", "generalErrorPage");
        resolver.setExceptionMappings(exceptionMappings);
        resolver.setDefaultErrorView("error");
        resolver.setExceptionAttribute("exception");
        return resolver;
    }
}
```

This configuration maps exceptions to specific error views. For example, `UserNotFoundException` will be mapped to `userNotFoundPage`, while other general exceptions will be handled by `generalErrorPage`.

#### Exercise

* [Exceptions](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_7_exceptions.md "Exercise 7")

## Controller Advice

@ControllerAdvice is a specialized annotation used to handle cross-cutting concerns across multiple controllers. It allows you to define global exception handlers and model attributes that apply to all or selected controllers in your application.

### Exception Handling

`@ControllerAdvice` is commonly used to define global exception handlers that can handle exceptions thrown by any controller in the application. You define methods in the `@ControllerAdvice` class that are annotated with `@ExceptionHandler` to catch specific exceptions and return appropriate responses or views.

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";  // Return a specific error view
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

In the example the `handleUserNotFound` method catches `UserNotFoundException` thrown by any controller, adds an error message to the model, and returns the `errorPage` view. And the `handleGeneralException` method handles any other exceptions, returning a generic error message with a `500 Internal Server Error` status.

### Model Attributes across Controllers

Another feature of `@ControllerAdvice` is to define methods that automatically add attributes to the model for all or selected controllers. Methods annotated with `@ModelAttribute` within a `@ControllerAdvice` class add common data to the model for every request handled by the controllers.

```java
@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("commonData")
    public String addCommonData() {
        return "This is common data for all controllers";
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("globalAttr", "Global attribute for all views");
    }
}
```

The `addCommonData` method adds a `commonData` attribute to the model, which will be available in all views rendered by the controllers. While the `addGlobalAttributes` method adds a `globalAttr` attribute to the model, again making it available across all controllers.

### Scoping a Controller Advice

You can limit the scope of a `@ControllerAdvice` to specific controllers, packages, or annotations. This allows you to apply advice selectively rather than globally. By setting attributes like `basePackages`, `assignableTypes`, or `annotations`, you can restrict where the advice is applied.

```java
@ControllerAdvice(basePackages = "com.example.controllers")
public class PackageSpecificAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "errorPage";
    }
}
```

#### Exercise

* [Controller Advice](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_8_controller_advice.md "Exercise 8")

