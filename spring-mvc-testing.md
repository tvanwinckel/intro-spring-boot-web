# Testing with MockMvc

MockMvc is a part of the Spring Test framework that allows you to test your Spring MVC controllers in a simplified and efficient way without the need to start a full web server. It provides a powerful way to simulate HTTP requests and responses in a Spring application context, enabling you to test the behavior of your controllers and other web components in isolation.

Some key features of MockMvc are:

* **Testing Without a Web Server:**
    * With MockMvc, you can test your Spring MVC controllers in a standalone mode. This means you don't need to deploy your application on a web server like Tomcat or Jetty to test your controllers.
* **Simulating HTTP Requests:**
    * MockMvc allows you to perform various HTTP requests (e.g., GET, POST, PUT, DELETE) and then assert the results. This is particularly useful for unit testing your RESTful endpoints.
* **Full Spring MVC Integration:**
    * It provides integration with the full Spring MVC stack, including request handling, validation, data binding, and more. This ensures that your tests are realistic and closely mimic actual application behavior.
* **Assertions on Responses:**
    * After performing a request, MockMvc allows you to assert various aspects of the response, such as HTTP status codes, response headers, content types, and the response body (e.g., JSON content).
* **Controller and Filter Testing:**
    * You can use MockMvc to test not only controllers but also any filters or interceptors that might be applied in your application.
* **Integration with Testing Frameworks:**
    * MockMvc is typically used in conjunction with JUnit or TestNG, making it a natural fit for writing unit and integration tests in a Spring application.

## Advantages of MockMvc

* `Speed`: Since MockMvc doesn't start a real web server, tests run faster, making it ideal for unit and integration testing.
* `Isolation`: You can test your controllers in isolation from other parts of the application, ensuring that your tests are focused and specific.
* `Comprehensive Testing`: MockMvc allows you to test a wide range of scenarios, including path variables, query parameters, headers, cookies, and more.
* `Realistic Testing`: Despite not starting a real server, MockMvc provides a realistic testing environment that closely mimics actual request handling in a Spring MVC application.

## Getting started

Ensure that your Spring project is set up with the necessary dependencies. The `spring-boot-starter-test` dependency, which contains MockMvc and other testing utilities. The easiest way to get started with MockMvc is with some examples. We start by creating a simple controller `UserController` that returns a `User` object when you access `/api/user/{id}`:

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = new User(id, "John Doe", "johndoe@example.com");
        return ResponseEntity.ok(user);
    }
}
```

Afterwards we can create a (new) testclass that uses MockMvc to test the controller:

```java
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name").value("John Doe"))
               .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }
}
```

* `@RunWith(SpringRunner.class)`: This annotation is used to provide a bridge between Spring Boot test features and JUnit. It helps in loading the Spring context for the test.
* `@WebMvcTest(UserController.class)`: This annotation configures the test class to focus only on Spring MVC components. It will load the `UserController` and its related components but not the entire Spring context. This makes the test faster and more focused.
* `MockMvc`: We use MockMvc to perform HTTP requests in our tests. It is autowired into the test class, allowing us to call methods like `perform()`,` andExpect()`, ...
* `mockMvc.perform(get("/api/user/1"))`: This line performs a GET request to the `/api/user/1` endpoint.
* `andExpect(status().isOk())`: This assertion checks that the HTTP status of the response is 200 (OK).
* `andExpect(content().contentType(MediaType.APPLICATION_JSON))`: This assertion checks that the content type of the response is JSON.
* `andExpect(jsonPath("$.name").value("John Doe"))`: This checks that the `name` field in the JSON response is `"John Doe"`.
* `andExpect(jsonPath("$.email").value("johndoe@example.com"))`: This checks that the `email` field in the JSON response is `"johndoe@example.com"`.

This example also displays the basic workflow of a MockMvc test:
* **Setup**: Initialize MockMvc in your test class, usually with annotations like @WebMvcTest or manually setting it up.
* **Perform**: Simulate an HTTP request (e.g., GET, POST, PUT, DELETE) to a specific endpoint.
* **Expect**: Assert the expected outcome of the request, such as HTTP status code, response content, headers, ...

```java
@Test
public void shouldCreateUser() throws Exception {
    String newUserJson = "{\"name\": \"Jane Doe\", \"email\": \"janedoe@example.com\"}";

    mockMvc.perform(post("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(newUserJson))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.name").value("Jane Doe"))
           .andExpect(jsonPath("$.email").value("janedoe@example.com"));
}
```

```java
@Test
public void shouldUpdateUser() throws Exception {
    String updatedUserJson = "{\"name\": \"Jane Doe\", \"email\": \"janedoe@example.com\"}";

    mockMvc.perform(put("/api/user/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedUserJson))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value("Jane Doe"))
           .andExpect(jsonPath("$.email").value("janedoe@example.com"));
}
```

```java
@Test
public void shouldDeleteUser() throws Exception {
    mockMvc.perform(delete("/api/user/1"))
           .andExpect(status().isNoContent());
}
```

## MockMvc in combination with Service Layers

If your controller interacts with a service layer, you can use Mockito to mock the service and inject it into your controller.

```java
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // @MockBean
    // private UserService userService;  // Mocking the service layer

    @Test
    public void shouldReturnUser() throws Exception {
        User mockUser = new User(1L, "John Doe", "johndoe@example.com");
        final UserService userService = Mockito.mock(UserService.class);
        Mockito.when(userService.findById(1L)).thenReturn(mockUser);

        mockMvc.perform(get("/api/user/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John Doe"))
               .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }
}
```

## Summary

MockMvc is an essential tool for testing Spring MVC applications, allowing you to perform HTTP requests and verify the behavior of your controllers in a fast, isolated manner. By using MockMvc, you can test GET, POST, PUT, and DELETE requests, and ensure your controller methods behave as expected without starting a full web server.

## Exercise

* [Setting up a (custom context path)](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_10_mockmvc.md "Exercise 10")
