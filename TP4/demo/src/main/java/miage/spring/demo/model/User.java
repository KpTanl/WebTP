package miage.spring.demo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email")
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @ManyToOne
    @JoinColumn(name = "codeStatut")
    private Status status;

    @ManyToMany
    @JoinTable(
        name = "participate",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "conference_id")
    )
    private Set<Conference> conferencesParticipated = new HashSet<>();

    @OneToMany(mappedBy = "organizer")
    private List<Conference> conferencesOrganized = new ArrayList<>();

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String password, Status status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Conference> getConferencesParticipated() {
        return conferencesParticipated;
    }

    public void setConferencesParticipated(Set<Conference> conferencesParticipated) {
        this.conferencesParticipated = conferencesParticipated;
    }

    public List<Conference> getConferencesOrganized() {
        return conferencesOrganized;
    }

    public void setConferencesOrganized(List<Conference> conferencesOrganized) {
        this.conferencesOrganized = conferencesOrganized;
    }

    public void addParticipation(Conference conference) {
        conferencesParticipated.add(conference);
    }

    public void removeParticipation(Conference conference) {
        conferencesParticipated.remove(conference);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
    }
}
