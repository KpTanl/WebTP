# 05 API、DTO、入库、页面展示

## 18. 外部 API 调用

```java
@Configuration
public class HttpClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

```java
public class RemoteMovieDTO {
    private Long id;
    private String title;
    private String release_date;
    private Double vote_average;
    private String overview;
}
```

```java
public class RemoteMoviePageDTO {
    private Integer page;
    private Integer total_pages;
    private List<RemoteMovieDTO> results;
}
```

```java
@Service
public class RemoteMovieService {

    private final RestTemplate restTemplate;

    @Value("${movie.api.base-url}")
    private String movieApiBaseUrl;

    @Value("${movie.api-key}")
    private String movieApiKey;

    public RemoteMovieService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RemoteMovieDTO getMovieById(Long movieId) {
        String url = movieApiBaseUrl + "/movie/" + movieId
                + "?api_key=" + movieApiKey
                + "&language=fr-FR";
        return restTemplate.getForObject(url, RemoteMovieDTO.class);
    }
}
```

## 19. `JSON -> DTO -> Entity -> 数据库` 的完整链路

```java
@Entity
@Table(name = "stored_movie")
public class StoredMovieEntity {
    @Id
    private Long id;
    private String title;
    private String releaseDate;
    private Double voteAverage;
    @Column(length = 3000)
    private String overview;
}
```

```java
public StoredMovieEntity convertToEntity(RemoteMovieDTO remoteMovieDTO) {
    StoredMovieEntity storedMovie = new StoredMovieEntity();
    storedMovie.setId(remoteMovieDTO.getId());
    storedMovie.setTitle(remoteMovieDTO.getTitle());
    storedMovie.setReleaseDate(remoteMovieDTO.getRelease_date());
    storedMovie.setVoteAverage(remoteMovieDTO.getVote_average());
    storedMovie.setOverview(remoteMovieDTO.getOverview());
    return storedMovie;
}
```

```java
@Service
public class StoredMovieService {

    private final StoredMovieRepository storedMovieRepository;
    private final RemoteMovieService remoteMovieService;

    public StoredMovieService(
            StoredMovieRepository storedMovieRepository,
            RemoteMovieService remoteMovieService) {
        this.storedMovieRepository = storedMovieRepository;
        this.remoteMovieService = remoteMovieService;
    }

    public StoredMovieEntity saveMovieFromApi(Long movieId) {
        RemoteMovieDTO remoteMovieDTO = remoteMovieService.getMovieById(movieId);
        StoredMovieEntity storedMovie = convertToEntity(remoteMovieDTO);
        return storedMovieRepository.save(storedMovie);
    }
}
```

## 20. 前端模板展示数据库数据

```java
@Controller
@RequestMapping("/stored-movies")
public class StoredMovieController {

    private final StoredMovieRepository storedMovieRepository;

    public StoredMovieController(StoredMovieRepository storedMovieRepository) {
        this.storedMovieRepository = storedMovieRepository;
    }

    @GetMapping
    public String allStoredMovies(Model model) {
        model.addAttribute("storedMovies", storedMovieRepository.findAll());
        return "stored-movie-list";
    }
}
```

```html
<tr th:each="storedMovie : ${storedMovies}">
    <td th:text="${storedMovie.id}"></td>
    <td th:text="${storedMovie.title}"></td>
    <td th:text="${storedMovie.releaseDate}"></td>
</tr>
```
