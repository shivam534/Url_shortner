package url.shortner.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import url.shortner.Entity.Userinfo;

@Repository
public interface  UserRepository extends JpaRepository<Userinfo,Long>{
   Userinfo findByEmail(String email);
   Userinfo findByEmailAndPassword(String email,String password);
   Userinfo findByUsername(String username);
}
