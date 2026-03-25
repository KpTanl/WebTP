package miage.spring.demo.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codeStatut;

    @Column(nullable = false, unique = true)
    private String nomStatut;

    @OneToMany(mappedBy = "status")
    private Set<User> users = new HashSet<>();

    public Status() {
    }

    public Status(String nomStatut) {
        this.nomStatut = nomStatut;
    }

    public Long getCodeStatut() {
        return codeStatut;
    }

    public void setCodeStatut(Long codeStatut) {
        this.codeStatut = codeStatut;
    }

    public String getNomStatut() {
        return nomStatut;
    }

    public void setNomStatut(String nomStatut) {
        this.nomStatut = nomStatut;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Status [codeStatut=" + codeStatut + ", nomStatut=" + nomStatut + "]";
    }
}
