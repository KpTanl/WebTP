# 04 Security、Session、加密

## 16. 登录、Session、Spring Security 基础

### Session 版登录

```java
@Controller
public class SessionLoginController {

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, HttpSession session) {
        session.setAttribute("connectedEmail", email);
        return "redirect:/dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
```

### Spring Security 版登录

```java
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/error").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error")
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
            );

        return http.build();
    }
}
```

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

## 17. 密码加密与 `BCryptPasswordEncoder`

```java
@Configuration
public class AppSecurityBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

```java
String hashedPassword = passwordEncoder.encode(rawPassword);
boolean ok = passwordEncoder.matches(rawPassword, appMember.getPasswordHash());
```

高频坑：

- 不能用 `equals`
- 不能二次加密
- 不能忘记注入 `PasswordEncoder`
