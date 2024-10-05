package url.shortner.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import url.shortner.groups.Logingroup;
import url.shortner.groups.Registergroup;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Users")
public class Userinfo {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotBlank(message="Name must be provided",groups = Registergroup.class)
    @Size(min=3 , max= 10 , message="length must be between 3 and 10",groups = Registergroup.class)
    private String name;

    @NotBlank(message="Username must be provided",groups = {Logingroup.class, Registergroup.class})
    private String username;

    @NotBlank(message="Email must be provided",groups = { Registergroup.class})
    @Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message="Invalid email !!",groups = {Logingroup.class, Registergroup.class})
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotBlank(message="Password is mandatory",groups = {Logingroup.class, Registergroup.class})
    @Size(min=4,max=10,message="Password length must be in between 4 & 10",groups = {Logingroup.class, Registergroup.class})
    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
