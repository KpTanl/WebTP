package miage.spring.demo.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User organizer;

    @ManyToMany
    @JoinTable(
        name = "traiter",
        joinColumns = @JoinColumn(name = "idConf"),
        inverseJoinColumns = @JoinColumn(name = "idThematique")
    )
    private Set<Thematique> thematiques = new HashSet<>();

    public Conference() {
    }

    public Conference(Long idconf, String nom, String titleconf, Integer nbeditionconf,
            String dtstartconf, String dtendconf, String urlwebsiteconf) {
        this.idconf = idconf;
        this.nom = nom;
        this.titleconf = titleconf;
        this.nbeditionconf = nbeditionconf;
        this.dtstartconf = dtstartconf;
        this.dtendconf = dtendconf;
        this.urlwebsiteconf = urlwebsiteconf;
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

    public String getTitleconf() {
        return titleconf;
    }

    public void setTitleconf(String titleconf) {
        this.titleconf = titleconf;
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

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public Set<Thematique> getThematiques() {
        return thematiques;
    }

    public void setThematiques(Set<Thematique> thematiques) {
        this.thematiques = thematiques;
    }

    public void addThematique(Thematique thematique) {
        thematiques.add(thematique);
    }

    public void removeThematique(Thematique thematique) {
        thematiques.remove(thematique);
    }
}
