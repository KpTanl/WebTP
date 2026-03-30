package miage.fr.api.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import miage.fr.api.demo.dto.FilmDTO;
import miage.fr.api.demo.dto.FilmPageDTO;

@Service
public class FilmService {
    
    @Autowired
    private RestTemplate restTemplate;

    @Value("${tmdb.api-key}")
    private String apiKey;

    @Value("${tmdb.base-url}")
    private String baseUrl;

    public FilmPageDTO getPopularFilms(int page){
        String url = baseUrl + "/movie/popular?page=" + page 
            + "&api_key=" + apiKey 
            + "&language=fr-FR";
        return restTemplate.getForObject(url, FilmPageDTO.class);
    }
    public FilmDTO getFilmById(Long id){
        String url = baseUrl + "/movie/" + id 
            + "?api_key=" + apiKey 
            + "&language=fr-FR";
        return restTemplate.getForObject(url, FilmDTO.class);
    }
}
