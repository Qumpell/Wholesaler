package pl.matkan.wholesaler.role;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.matkan.wholesaler.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name="roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

//    @OneToMany(mappedBy = "role")
//    @JsonManagedReference(value = "usersRole")
//    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    private Collection<User> users = new HashSet<>();

//    public void addUser(User user) {
//        if(!users.contains(user)) {
//            users.add(user);
//            user.addRole(this);
//        }
//    }
//    public void removeUser(User user) {
//        if(users.contains(user)) {
//            users.remove(user);
//            user.removeRole(this);
//        }
//    }


//    public void addUser(User user) {
//        users.add(user);
//        user.setRole(this);
//    }
//    public void removeUser(User user) {
//        users.remove(user);
//        user.setRole(null);
//    }
}