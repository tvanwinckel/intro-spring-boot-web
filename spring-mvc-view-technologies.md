# View Technologies

In Spring MVC, view technologies are responsible for rendering the output of a web application and presenting it to the user. Spring MVC separates the model (data), view (presentation), and controller (processing logic) in a way that makes the application easier to manage and maintain. Views are a crucial part of this, as they define how the response data is presented to the end-user.

Common used View Technologies are:
* JSP (JavaServer Pages)
* Thymeleaf
* FreeMarker
* Groovy Markup Templates
* PDF, Excel, and Other Binary Views
* JSON and XML Views

## Thymeleaf

Thymeleaf is a modern server-side Java template engine designed for web and standalone environments. It's known for its natural templating capability, meaning templates can be directly opened in a browser without running the application. Thymeleaf processes HTML templates and generates dynamic content by evaluating expressions and processing template tags and is integrated into Spring MVC using the `ThymeleafViewResolver`. Templates are typically stored in the `resources/templates` directory. The controller returns a view name corresponding to the template file.

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
 <h1>Form</h1>
     <form action="#" th:action="@{/add-form-item}" th:object="${item}" method="post">
      <p>Name: <input type="text" th:field="*{name}" /></p>
      <p>Quality: <input type="text" th:field="*{quality}" /></p>
      <p>Durability: <input type="text" th:field="*{durability}" /></p>
      <p><input type="submit" value="Submit" /></p>
     </form>
</body>
</html>
```

To be able to use Thymeleaf templates in your Spring Boot project, make sure to add the following dependency: `'org.springframework.boot:spring-boot-starter-thymeleaf'`

## JSON and XML views

 For RESTful APIs, Spring MVC can render responses in JSON or XML format. This is typically used in REST controllers where the response body is serialized into JSON or XML. These views serialize Java objects into JSON or XML using libraries like `Jackson` or `JAXB`. When using `@RestController` or `@ResponseBody` on your `@Controller`s, Spring automatically handles the conversion of objects to JSON or XML using `MappingJackson2HttpMessageConverter` for JSON and `Jaxb2RootElementHttpMessageConverter` for XML.

 ```java
 @GetMapping("/user/{id}")
public User getUser(@PathVariable Long id) {
    return new User(id, "John Doe", "johndoe@example.com");
}
 ```

 ### Exercise

* [Thymeleaf form](https://github.com/tvanwinckel/intro-spring-boot-web/tree/main/exercises/exercise_9_thymeleaf.md "Exercise 9")