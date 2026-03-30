package miage.fr.api.demo.dto;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
public class FilmPageDTO {
    private Integer page;
    private List<FilmDTO> results;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("total_results")
    private Integer totalResults;
    
    public FilmPageDTO() {}
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
    public List<FilmDTO> getResults() { return results; }
    public void setResults(List<FilmDTO> results) { this.results = results; }
}