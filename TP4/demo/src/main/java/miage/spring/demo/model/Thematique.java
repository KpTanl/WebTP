package miage.spring.demo.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "thematiques")
public class Thematique {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idThematique;

    @Column(nullable = false, unique = true)
    private String nomThematique;

    @ManyToMany(mappedBy = "thematiques")
    private Set<Conference> conferences = new HashSet<>();

    public Thematique() {
    }

    public Thematique(String nomThematique) {
        this.nomThematique = nomThematique;
    }

    public Long getIdThematique() {
        return idThematique;
    }

    public void setIdThematique(Long idThematique) {
        this.idThematique = idThematique;
    }

    public String getNomThematique() {
        return nomThematique;
    }

    public void setNomThematique(String nomThematique) {
        this.nomThematique = nomThematique;
    }

    public Set<Conference> getConferences() {
        return conferences;
    }

    public void setConferences(Set<Conference> conferences) {
        this.conferences = conferences;
    }

    @Override
    public String toString() {
        return "Thematique [idThematique=" + idThematique + ", nomThematique=" + nomThematique + "]";
    }
}
