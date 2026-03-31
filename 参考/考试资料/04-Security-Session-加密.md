# 04 Security、Session、加密

## 16. 登录、Session、Spring Security 基础

老师在 `TP4` 里的要求，优先走这条路径：

1. 用 `spring-security-crypto` 提供 `BCryptPasswordEncoder`
2. 用户注册或修改密码时，先 `encode` 再存数据库
3. 登录时用 `passwordEncoder.matches(rawPassword, storedHash)` 校验
4. 登录成功后，把“当前登录用户是谁”存到 `HttpSession`

也就是说：

- `TP4` 默认不是让你上来就写 `SecurityFilterChain`
- `TP4` 的核心是 `BCrypt + 手写登录校验 + Session`
- `Spring Security` 整套表单登录更像 `TP6` 或扩展写法

### Session 版登录

这部分更符合老师对 `TP4` 的要求。

```java
@Controller
public class AuthController {

    public static final String SESSION_USER_ID = "connectedUserId";

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/users/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        User user = userService.authenticate(email, password);
        if (user == null) {
            model.addAttribute("message", "Connexion impossible");
            return "login";
        }

        session.setAttribute(SESSION_USER_ID, user.getId());
        return "redirect:/users/me";
    }

    @GetMapping("/users/me")
    public String mySpace(HttpSession session, Model model) {
        Object userId = session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            model.addAttribute("message", "Veuillez vous connecter");
            return "login";
        }
        model.addAttribute("user", userService.findById((Long) userId));
        return "user-space";
    }

    @PostMapping("/users/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate();
        model.addAttribute("message", "Deconnexion reussie");
        return "login";
    }
}
```

这里要点是：

- 登录表单提交 `email` 和 `password`
- 是否登录成功，靠 `UserService.authenticate(...)` 判断
- 登录成功后，把用户 `id` 存到 `session`
- 后面访问用户空间时，从 `session` 里取出当前用户

### `TP4` 老师要求下的依赖

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

这个依赖只负责密码哈希，不会自动帮你做登录拦截。

### Spring Security 版登录

这一段保留给 `TP6` 或题目明确要求“Spring Security 登录保护”的情况。

```java
@Configuration // 标记这是一个配置类，Spring 启动时会加载
public class SecurityConfig {

    @Bean // 把 SecurityFilterChain 交给 Spring 容器管理
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // 这些路径不需要登录就能访问
                .requestMatchers("/login", "/register", "/error").permitAll()
                // 其他所有请求都必须先认证
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // 指定自定义登录页地址
                .loginPage("/login")
                // 指定登录表单中用户名参数名，这里用 email 作为登录账号
                .usernameParameter("email")
                // 登录成功后跳转到 /dashboard，true 表示总是跳转到这里
                .defaultSuccessUrl("/dashboard", true)
                // 登录失败后跳回登录页，并带上 error 参数
                .failureUrl("/login?error")
            )
            .logout(logout -> logout
                // 退出登录成功后跳转到登录页，并带上 logout 参数
                .logoutSuccessUrl("/login?logout")
            );

        // 构建并返回安全过滤器链
        return http.build();
    }
}
```

```java
@Bean // 把 UserDetailsService 注册为 Spring Bean，供 Spring Security 调用
public UserDetailsService userDetailsService(AppMemberRepository appMemberRepository) {
    // 这里返回一个 lambda，参数 username 就是登录时提交的用户名
    // 由于前面配置了 usernameParameter("email")，这里的 username 实际上就是 email
    return username -> {
        // 根据邮箱去数据库查询用户
        AppMemberEntity appMember = appMemberRepository.findByEmail(username);

        // 如果查不到用户，抛出异常，Spring Security 会认定登录失败
        if (appMember == null) {
            throw new UsernameNotFoundException("Utilisateur introuvable");
        }

        // 把数据库里的用户信息转换成 Spring Security 认识的 UserDetails 对象
        return org.springframework.security.core.userdetails.User
            // 设置登录用户名，这里用邮箱
            .withUsername(appMember.getEmail())
            // 设置加密后的密码，登录时 Spring Security 会自动校验
            .password(appMember.getPasswordHash())
            // 设置角色，这里给当前用户 USER 角色
            .roles("USER")
            // 构建 UserDetails 对象
            .build();
    };
}
```

## 17. 密码加密与 `BCryptPasswordEncoder`

这里也优先按 `TP4` 老师要求来理解：

- 数据库里不能存明文密码
- 要存 `BCrypt` 生成的 hash
- 登录时比对明文输入和数据库中的 hash
- “谁登录了”这个状态，不是存在 `PasswordEncoder` 里，而是存在 `HttpSession` 里

核心原则：

- 数据库里保存的是 hash，不是明文密码
- 注册时用 `passwordEncoder.encode(rawPassword)` 生成 hash
- 登录校验时用 `passwordEncoder.matches(rawPassword, passwordHash)`
- `BCrypt` 是单向 hash，不是可逆“解密”
- 同一个原始密码，多次 `encode` 的结果可能不同，这是正常的

为什么不能直接存明文：

- 数据库泄露时，明文密码会直接暴露
- 用户经常复用密码，风险会扩散到别的网站
- 考试里如果题目提到“密码加密”，默认就是保存 hash，不是自己写字符串加密算法

### `TP4` 老师认可的完整路径

```text
表单明文密码
-> PasswordEncoder.encode(...)
-> 数据库存 hash
-> 登录时 PasswordEncoder.matches(...)
-> 登录成功后 session.setAttribute("connectedUserId", user.getId())
```

### 典型实体字段

```java
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

说明：

- 在 `TP4` 里，字段名可以叫 `password`
- 但数据库里保存的内容必须是 hash，不是明文
- 如果你想写得更清楚，也可以把字段名改成 `passwordHash`

### 配置 `PasswordEncoder` Bean

```java
@Configuration
public class SecurityBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

说明：

- 以后业务层、初始化数据、手写登录校验都可以注入同一个 `PasswordEncoder`
- 常见写法就是返回 `new BCryptPasswordEncoder()`

### 注册时保存加密密码

```java
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email obligatoire");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Mot de passe obligatoire");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email deja utilise");
        }

        // 这里接收到的还是表单传来的明文密码
        // 入库前必须先转换成 bcrypt hash
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // save(...) 才是真正写入数据库的动作
        // 此时数据库字段里存的是 hash，不是明文
        return userRepository.save(user);
    }
}
```

如果题目里还有“修改用户”功能，推荐加这一层保护，避免把已经加密过的密码再加密一次：

```java
private void encodePasswordIfNecessary(User user) {
    if (user.getPassword() == null || user.getPassword().isBlank()) {
        return;
    }
    if (looksLikeBcryptHash(user.getPassword())) {
        return;
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
}

private boolean looksLikeBcryptHash(String value) {
    return value.startsWith("$2a$")
            || value.startsWith("$2b$")
            || value.startsWith("$2y$");
}
```

重点：

- Controller 收到的是明文 `password`
- 存库前立刻 `encode`
- 即使字段名叫 `password`，数据库里放的也必须是 hash

### `TP4` 风格登录校验

```java
public User authenticate(String email, String rawPassword) {
    User user = userRepository.findByEmail(email);

    if (user == null || rawPassword == null || rawPassword.isBlank()) {
        return null;
    }
    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
        return null;
    }
    return user;
}
```

对应的 Session 写法：

```java
User user = userService.authenticate(email, password);
if (user != null) {
    session.setAttribute("connectedUserId", user.getId());
}
```

### 手动登录校验

```java
String hashedPassword = passwordEncoder.encode(rawPassword);
boolean ok = passwordEncoder.matches(rawPassword, user.getPassword());
```

更完整一点的写法：

```java
public User login(String email, String rawPassword) {
    User user = userRepository.findByEmail(email);

    if (user == null) {
        return null;
    }
    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
        return null;
    }
    return user;
}
```

注意：

- 登录时不要写 `passwordEncoder.encode(rawPassword).equals(...)`
- 因为 `BCrypt` 每次生成的 hash 可能不同
- 正确写法永远是 `matches(rawPassword, storedHash)`

### Session 和加密的职责要分开

- `PasswordEncoder` 负责把明文密码转成 hash，并校验密码
- `HttpSession` 负责记住“当前是谁登录了”
- 老师在 `TP4` 里要的就是这两个部分分别实现

### 和 Spring Security 的衔接

如果用 Spring Security 表单登录，`UserDetailsService` 返回的密码应该是数据库里的 hash：

```java
@Bean
public UserDetailsService userDetailsService(AppMemberRepository appMemberRepository) {
    return username -> {
        AppMemberEntity appMember = appMemberRepository.findByEmail(username);
        if (appMember == null) {
            throw new UsernameNotFoundException("Utilisateur introuvable");
        }
        return org.springframework.security.core.userdetails.User
            .withUsername(appMember.getEmail())
            .password(appMember.getPasswordHash())
            .roles("USER")
            .build();
    };
}
```

理解这一段：

- `.password(...)` 放的是已经加密后的 `passwordHash`
- 用户在登录页输入的是明文密码
- Spring Security 会自动调用 `PasswordEncoder` 来比对

### 初始化默认用户时也要加密

```java
@Bean
public CommandLineRunner initializeDefaultUser(
        AppMemberRepository appMemberRepository,
        PasswordEncoder passwordEncoder) {
    return args -> {
        if (!appMemberRepository.existsByEmail("admin@test.com")) {
            AppMemberEntity appMember = new AppMemberEntity();
            appMember.setEmail("admin@test.com");
            appMember.setPasswordHash(passwordEncoder.encode("admin123"));
            appMemberRepository.save(appMember);
        }
    };
}
```

### 考试里可以直接写的最小流程

1. 在 `pom.xml` 里加 `spring-security-crypto`
2. 配一个 `PasswordEncoder` Bean
3. 实体里准备 `password` 或 `passwordHash` 字段
4. 新增/修改用户时 `encode(rawPassword)` 后再保存
5. 登录校验时用 `matches(rawPassword, storedHash)`
6. 登录成功后把当前用户 `id` 放进 `HttpSession`
7. 如果题目明确要求 Spring Security，再补 `starter-security` 和 `UserDetailsService`

### 一句话记忆版

`TP4 = BCrypt 哈希密码 + service 里 matches 校验 + HttpSession 记住当前用户`

### 如果字段名叫 `password`

- 完全可以
- 关键不是字段名，而是里面存的是不是 hash
- 所以 `user.getPassword()` 在数据库里看到的应该是 `$2a$...` 这一类 bcrypt 字符串

### 如果字段名叫 `passwordHash`

- 语义更清楚
- 但不是老师要求的必要条件
- 考试里沿用已有实体字段名通常更稳

### 如何判断字符串像不像 bcrypt hash

- 通常以 `$2a$`、`$2b$` 或 `$2y$` 开头
- 长度通常在 60 个字符左右
- 看到这种格式，基本可以判断它不是明文

高频坑：

- 不能把明文密码直接保存到数据库
- 不能用 `equals` 比较明文和 hash
- 不能用 `passwordEncoder.encode(rawPassword).equals(storedHash)` 做登录判断
- 不能二次加密已经是 hash 的值
- 不能忘记注入 `PasswordEncoder`
- 不能把“密码哈希”和“session 保存当前用户”混成一件事
- `TP4` 默认不需要为了加密去写 `SecurityFilterChain`
