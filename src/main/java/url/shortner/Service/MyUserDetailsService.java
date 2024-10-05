package url.shortner.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import url.shortner.Entity.UserPrincipal;
import url.shortner.Entity.Userinfo;
import url.shortner.Repository.UserRepository;

import java.security.Principal;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userrepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Userinfo user = userrepo.findByUsername(username);
        if(user==null){
            System.out.println("User Not Found ");
               throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);
    }


}
