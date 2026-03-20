package miage.spring.demo.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;

   @ManyToMany
    @JoinTable(
        name = "participate",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "conference_id")
    )
    private Set<Conference> conferencesParticipated = new HashSet<>();

    @OneToMany(mappedBy = "organizer")
    private List<Conference> conferencesOrganized;
    
    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + "]";
    }
}
