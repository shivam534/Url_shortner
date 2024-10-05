package url.shortner.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import url.shortner.groups.Logingroup;

@Data
@Entity
@NoArgsConstructor
@Table(name="Urls")
public class Urls {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(name="Username")
    //@NotBlank(message = "Username must be provided" , groups = Logingroup.class)
    private String username;

   // @NotBlank(message = "Long url must be provided")
    @Column(name = "Long url")
  //  @Pattern(regexp = "^(https?|ftp)://[^\\\\s/$.?#].[^\\\\s]*$",message = "Invalid url")
    private String longurl;

  //  @NotBlank(message = "Short url must be provided")
    @Column(name = "Short url")
    private String shorturl;
}
