# 03 JPA、Entity、Repository、Service

## 9. Model / DTO / Entity 分别是什么

### `Model`

```java
model.addAttribute("storedMovies", storedMovies);
```

### DTO

```java
public class RemoteMovieDTO {
    private Long id;
    private String title;
    private String release_date;
}
```

### Entity

```java
@Entity
public class StoredMovieEntity {
    @Id
    private Long id;
    private String title;
    private String releaseDate;
}
```

### 区分

- `Model`：页面层
- `DTO`：传输/API 层
- `Entity`：数据库层

## 10. SQLite 与 JPA 基础配置

```properties
spring.datasource.url=jdbc:sqlite:db/examdemo.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

## 11. 实体类怎么写

```java
@Entity
@Table(name = "app_member")
public class AppMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String passwordHash;
}
```

## 12. 一对多、多对一、多对多怎么建

### 多对一

```java
@ManyToOne
@JoinColumn(name = "status_id")
private MemberStatusEntity memberStatus;
```

### 一对多

```java
@OneToMany(mappedBy = "memberStatus")
private Set<AppMemberEntity> members = new HashSet<>();
```

### 多对多

```java
@ManyToMany
@JoinTable(
    name = "member_conference",
    joinColumns = @JoinColumn(name = "member_id"),
    inverseJoinColumns = @JoinColumn(name = "conference_id")
)
private Set<ConferenceEventEntity> joinedConferences = new HashSet<>();
```

## 13. Repository 常用函数命名法

```java
public interface AppMemberRepository extends JpaRepository<AppMemberEntity, Long> {
    AppMemberEntity findByEmail(String email);
    boolean existsByEmail(String email);
    List<AppMemberEntity> findByFullNameContainingIgnoreCase(String fullName);
    List<AppMemberEntity> findByMemberStatus_Id(Long statusId);
}
```

```java
public interface ConferenceEventRepository extends JpaRepository<ConferenceEventEntity, Long> {
    List<ConferenceEventEntity> findByTitleContainingIgnoreCase(String title);
    List<ConferenceEventEntity> findByParticipants_Id(Long memberId);
    List<ConferenceEventEntity> findByOrganizer_Id(Long organizerId);
}
```

## 14. Service 层怎么组织业务逻辑

Controller：

- 接请求
- 调 Service
- 放 `Model`
- 返回页面

Service：

- 参数检查
- 业务规则
- 默认值处理
- 加密
- 调 Repository / API

### 标准模板

```java
@Service
public class AppMemberService {

    private final AppMemberRepository appMemberRepository;
    private final PasswordEncoder passwordEncoder;

    public AppMemberService(
            AppMemberRepository appMemberRepository,
            PasswordEncoder passwordEncoder) {
        this.appMemberRepository = appMemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppMemberEntity> findAll() {
        return appMemberRepository.findAll();
    }

    public AppMemberEntity findById(Long memberId) {
        return appMemberRepository.findById(memberId).orElse(null);
    }

    public AppMemberEntity create(AppMemberEntity appMember) {
        return appMemberRepository.save(appMember);
    }

    public AppMemberEntity update(AppMemberEntity appMember) {
        return appMemberRepository.save(appMember);
    }

    public void deleteById(Long memberId) {
        appMemberRepository.deleteById(memberId);
    }
}
```

## 15. CRUD 标准模板

```java
@Controller
@RequestMapping("/members")
public class AppMemberController {

    private final AppMemberService appMemberService;

    public AppMemberController(AppMemberService appMemberService) {
        this.appMemberService = appMemberService;
    }

    @GetMapping("/all")
    public String allMembers(Model model) {
        model.addAttribute("members", appMemberService.findAll());
        return "member-list";
    }

    @PostMapping("/add")
    public String addMember(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email) {
        AppMemberEntity appMember = new AppMemberEntity();
        appMember.setFullName(fullName);
        appMember.setEmail(email);
        appMemberService.create(appMember);
        return "redirect:/members/all";
    }

    @PostMapping("/delete")
    public String deleteMember(@RequestParam("id") Long id) {
        appMemberService.deleteById(id);
        return "redirect:/members/all";
    }
}
```
