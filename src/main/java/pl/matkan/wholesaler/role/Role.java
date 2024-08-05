package pl.matkan.wholesaler.role;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.matkan.wholesaler.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "role")
    @JsonManagedReference(value = "usersRole")
    private List<User> users = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }

    public void addUser(User user) {
        users.add(user);
        user.setRole(this);
    }
    public void removeUser(User user) {
        users.remove(user);
        user.setRole(null);
    }
}