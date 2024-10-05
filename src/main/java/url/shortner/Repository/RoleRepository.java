package url.shortner.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import url.shortner.Entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);

}
