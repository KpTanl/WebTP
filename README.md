# Web / Spring Boot 项目复习总览

## 项目内容

这个仓库整理了课程里的几个 TP，内容按顺序覆盖了：

- HTML / CSS / 表单
- Spring Boot 项目创建
- 路由、参数接收、表单处理
- Thymeleaf 模板渲染
- SQLite + JPA + CRUD
- 实体关系建模
- 密码加密与登录
- 外部 API 调用
- API 数据入库
- 前端页面展示数据库内容

如果考试是综合题，这个仓库里的内容基本可以串成一条完整流程：

`API -> Spring Boot -> Service -> Database -> Thymeleaf -> 登录/加密`

## 目录结构

```text
.
├── TP1/        HTML / CSS / JS 基础
├── TP2/        Spring Boot 创建、路由、参数、Session、表单处理
├── TP3/        Thymeleaf 模板
├── TP4/        SQLite、JPA、CRUD、实体关系、密码加密
├── TP6/        API、Security、用户功能、电影收藏/待看
├── db/         数据库文件
└── 参考/       课程 PDF
```

## 每个 TP 做了什么

### TP1

目录：[TP1](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP1)

完成内容：

- 静态网页 `index.html`
- 样式文件 `style.css`
- 图片、表格、列表、链接、表单
- 表单提交后的确认页 `confirmation.html`

对应知识点：

- HTML 基本标签
- CSS 基础
- `form`
- `input`
- `select`
- `radio`
- `checkbox`
- `GET` 提交

### TP2

目录：[TP2](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP2)

完成内容：

- 创建 Spring Boot 项目
- 编写 `@Controller` 和 `@RestController`
- 练习 `@GetMapping` / `@PostMapping`
- 练习 `@RequestParam` / `@PathVariable`
- 使用 `HttpSession` 保存简单登录状态
- 处理预约表单并显示结果

代码重点：

- [HelloController.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP2/src/main/java/utcapitole/miage/tp2/controller/HelloController.java)
- [borneController.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP2/src/main/java/utcapitole/miage/tp2/controller/borneController.java)
- [ResaController.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP2/src/main/java/utcapitole/miage/tp2/controller/ResaController.java)
- [Reservation.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP2/src/main/java/utcapitole/miage/tp2/model/Reservation.java)

对应知识点：

- Spring Boot 基础启动
- 路由映射
- 参数接收
- Session
- 表单提交
- 页面跳转

### TP3

目录：[TP3](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP3/tp3)

完成内容：

- 表单输入上下界
- 后端计算数字和平方
- 将结果列表传给 Thymeleaf 页面
- 在模板里循环渲染结果表格

代码重点：

- [NumberController.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP3/tp3/src/main/java/com/example/tp3/controller/NumberController.java)
- [Number.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP3/tp3/src/main/java/com/example/tp3/model/Number.java)
- [index.html](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP3/tp3/src/main/resources/templates/index.html)
- [res.html](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP3/tp3/src/main/resources/templates/res.html)

对应知识点：

- `Model`
- `th:text`
- `th:each`
- 模板渲染
- 后端数据到前端页面

### TP4

目录：[TP4](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP4/demo)

完成内容：

- 配置 SQLite
- 使用 Spring Data JPA
- 建立用户、会议、主题、身份状态等实体
- 完成用户和会议的 CRUD
- 建立实体关系
- 使用 `BCryptPasswordEncoder` 加密密码
- 初始化默认数据
- 基于 Session 做登录后用户空间

主要实体：

- `User`
- `Conference`
- `Status`
- `Thematique`

关系设计：

- `User` -> `Status`：多对一
- `Conference` -> `User`：多对一（组织者）
- `User` <-> `Conference`：多对多（参与）
- `Conference` <-> `Thematique`：多对多

代码重点：

- [User.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP4/demo/src/main/java/miage/spring/demo/model/User.java)
- [Conference.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP4/demo/src/main/java/miage/spring/demo/model/Conference.java)
- [UserService.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP4/demo/src/main/java/miage/spring/demo/model/jpa/UserService.java)
- [ConferenceService.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP4/demo/src/main/java/miage/spring/demo/model/jpa/ConferenceService.java)
- [UserController.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP4/demo/src/main/java/miage/spring/demo/controller/UserController.java)
- [ConferenceController.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP4/demo/src/main/java/miage/spring/demo/controller/ConferenceController.java)
- [AuthController.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP4/demo/src/main/java/miage/spring/demo/controller/AuthController.java)
- [SecurityBeansConfig.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP4/demo/src/main/java/miage/spring/demo/config/SecurityBeansConfig.java)

对应知识点：

- `@Entity`
- `@Id`
- `@GeneratedValue`
- `@Column`
- `@ManyToOne`
- `@OneToMany`
- `@ManyToMany`
- `@JoinColumn`
- `@JoinTable`
- Repository 命名查询
- Service 分层
- CRUD
- 密码加密

### TP6

目录：[TP6](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP6/demo)

完成内容：

- 调用外部电影 API
- 用 DTO 接收 JSON 数据
- 提供 REST 接口
- 提供 MVC 页面展示电影列表和详情
- 保存用户、电影、用户电影关系到 SQLite
- 使用 Spring Security 做登录保护
- 注册用户并加密密码
- 收藏电影、加入待看列表

主要链路：

1. 调用外部 API 获取电影数据
2. 返回 DTO
3. 页面显示电影列表
4. 用户登录后操作收藏/待看
5. 把电影和用户关系保存到数据库
6. 再从数据库读取用户自己的列表

代码重点：

- [FilmService.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP6/demo/src/main/java/miage/fr/api/demo/service/FilmService.java)
- [FilmController.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP6/demo/src/main/java/miage/fr/api/demo/controller/FilmController.java)
- [FilmRestController.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP6/demo/src/main/java/miage/fr/api/demo/controller/FilmRestController.java)
- [UserFilmService.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP6/demo/src/main/java/miage/fr/api/demo/service/UserFilmService.java)
- [SecurityConfig.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP6/demo/src/main/java/miage/fr/api/demo/config/SecurityConfig.java)
- [DataInitializer.java](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/TP6/demo/src/main/java/miage/fr/api/demo/config/DataInitializer.java)

对应知识点：

- `RestTemplate`
- DTO
- JSON 映射
- API 调用
- Spring Security
- `UserDetailsService`
- `SecurityFilterChain`
- `Principal`
- 用户个性化数据持久化

## 综合题可能考的内容

按这个仓库的内容，综合题大概率会落在下面这些组合上：

- 创建 Spring Boot 项目并配置依赖
- 接收 API 数据
- 将数据映射成 DTO
- 保存到数据库
- 建实体关系
- 用 Thymeleaf 显示数据
- 做表单提交
- 做注册 / 登录
- 对密码加密
- 控制登录后页面访问

一个很像考试题的完整流程是：

1. 创建 Spring Boot 项目
2. 配置 `pom.xml`
3. 配置 `application.properties`
4. 创建 Entity / Repository / Service / Controller
5. 调用外部 API
6. 保存数据到 SQLite
7. 创建模板页面显示数据
8. 加入注册和登录
9. 密码加密
10. 处理用户自己的收藏、历史、待办之类的数据

## 资料范围

参考目录：[参考](D:/QKP/Toulouse EDU/M1 - S1 -S2/Web/参考)

范围对应的 PDF：

- `TP1 - Rappels HTML, CSS, JS.pdf`
- `TP2.1 - Création projet SpringBoot.pdf`
- `TP2.2 - Routing avec SpringBoot.pdf`
- `TP3 - Templating - v2.pdf`
- `TP45 - DB.pdf`
- `TP6-7 - Site et API.pdf`

这些资料和当前代码目录基本一一对应。

## 复习顺序

直接按这个顺序复习就行：

1. HTML / CSS / 表单
2. Spring Boot 创建
3. Controller、路由、参数
4. 表单提交
5. Thymeleaf
6. SQLite + JPA
7. Entity 关系
8. CRUD
9. 密码加密
10. 登录与权限控制
11. 外部 API
12. API 数据入库
13. 页面展示数据库内容

## 一句话总结

这个仓库不是几个分散的小作业，而是一条完整的 Web 项目构建链路。考试如果出综合题，重点就是把这些部分连起来。
