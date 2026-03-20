package miage.spring.demo.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "conference")
public class Conference {
    @Id

    @GeneratedValue(strategy = GenerationType.AUTO) 
    private Long idconf;
    
    private String nom;
    private String titleconf;
    private Integer nbeditionconf;
    private String dtstartconf;
    private String dtendconf;
    private String urlwebsiteconf;


    @ManyToMany(mappedBy = "conferencesParticipated")
    private Set<User> participants = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User organizer;

    
    public Conference() {
    }

    public Conference(Long idconf, String nom, String titleconf, Integer nbeditionconf, String dtstartconf, String dtendconf, String urlwebsiteconf) {
        this.idconf = idconf;
        this.nom = nom;
        this.titleconf = titleconf;
        this.nbeditionconf = nbeditionconf;
        this.dtstartconf = dtstartconf;
        this.dtendconf = dtendconf;
        this.urlwebsiteconf = urlwebsiteconf;
    }
    public String getTitleconf() {
        return titleconf;
    }

    public void setTitleconf(String titleconf) {
        this.titleconf = titleconf;
    }
    public Long getIdconf() {
        return idconf;
    }

    public void setIdconf(Long idconf) {
        this.idconf = idconf;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public Integer getNbeditionconf() {
        return nbeditionconf;
    }

    public void setNbeditionconf(Integer nbeditionconf) {
        this.nbeditionconf = nbeditionconf;
    }
    public String getDtstartconf() {
        return dtstartconf;
    }

    public void setDtstartconf(String dtstartconf) {
        this.dtstartconf = dtstartconf;
    }
    public String getDtendconf() {
        return dtendconf;
    }

    public void setDtendconf(String dtendconf) {
        this.dtendconf = dtendconf;
    }
    public String getUrlwebsiteconf() {
        return urlwebsiteconf;
    }

    public void setUrlwebsiteconf(String urlwebsiteconf) {
        this.urlwebsiteconf = urlwebsiteconf;
    }

} 
