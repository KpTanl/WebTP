# 02 Spring MVC、表单、Thymeleaf

## 5. Spring MVC 基础

MVC：

- `Model`：页面数据
- `View`：页面
- `Controller`：接请求、调逻辑、返回页面或数据

### `@Controller`

```java
@Controller
public class HomePageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
```

### `@RestController`

```java
@RestController
@RequestMapping("/api")
public class MovieApiController {

    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }
}
```

## 6. 路由与参数接收

- `@RequestMapping`
- `@GetMapping`
- `@PostMapping`
- `@RequestParam`
- `@PathVariable`

### 查询参数

```java
@GetMapping("/movies/search")
public String searchMovie(@RequestParam("title") String title, Model model) {
    model.addAttribute("keyword", title);
    return "movie-search";
}
```

### 路径参数

```java
@GetMapping("/movies/{movieId}")
public String movieDetail(@PathVariable Long movieId, Model model) {
    model.addAttribute("movieId", movieId);
    return "movie-detail";
}
```

## 7. 表单提交与页面跳转

### 表单基础

```html
<form action="/members/add" method="post">
    <label for="memberName">Nom</label>
    <input type="text" id="memberName" name="memberName" required>

    <label for="memberEmail">Email</label>
    <input type="email" id="memberEmail" name="memberEmail" required>

    <button type="submit">Ajouter</button>
</form>
```

### 后端接收表单

```java
@PostMapping("/members/add")
public String addMember(
        @RequestParam("memberName") String memberName,
        @RequestParam("memberEmail") String memberEmail,
        Model model) {
    model.addAttribute("message", "Membre ajoute");
    return "member-result";
}
```

### `redirect:` 和 `forward:`

先分清 3 个概念：

- 视图名：比如 `member-result`
- 路径：比如 `/members/all`、`/form.html`
- 模板文件：比如 `templates/member-result.html`

#### 1. 直接返回字符串

```java
return "member-result";
```

这是返回视图名，不是 URL。

如果使用 Thymeleaf，Spring 通常会去找：

```text
src/main/resources/templates/member-result.html
```

#### 2. `redirect:`

```java
return "redirect:/members/all";
```

这不是找模板文件，而是告诉浏览器重新请求 `/members/all`。

所以：

- `redirect:/members/all` 里的 `/members/all` 是路径
- 它一般对应某个 `@GetMapping("/members/all")`
- 最终显示哪个页面，要看这个新请求对应的 Controller 又返回了什么

#### 3. `forward:`

```java
return "forward:/form.html";
```

这也不是找模板文件，而是服务器内部把当前请求转发到 `/form.html`。

所以：

- `forward:/form.html` 里的 `/form.html` 是路径
- 它常常对应 `static/form.html`
- 浏览器地址栏通常不会变

#### 最容易混的地方

```java
return "form";
```

这通常表示视图名，Spring 会去找：

```text
templates/form.html
```

而下面这个：

```java
return "forward:/form.html";
```

表示转发到路径 `/form.html`，更像是在访问：

```text
static/form.html
```

#### 一句话判断

- 没有前缀：大概率是视图名，去找模板
- `redirect:`：重定向到路径
- `forward:`：服务器内部转发到路径

## 8. Thymeleaf 模板语法速查

### 显示变量

```html
<p th:text="${pageTitle}"></p>
```

### 遍历列表

```html
<tr th:each="storedMovie : ${storedMovies}">
    <td th:text="${storedMovie.title}"></td>
    <td th:text="${storedMovie.releaseDate}"></td>
</tr>
```

### 条件显示

```html
<div th:if="${isFavorite}">
    <button>Retirer des favoris</button>
</div>
```

### 链接

```html
<a th:href="@{/movies/{movieId}(movieId=${storedMovie.id})}">Detail</a>
```

### 表单 action

```html
<form th:action="@{/members/add}" method="post">
```

### Security 下的 CSRF

```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
```
