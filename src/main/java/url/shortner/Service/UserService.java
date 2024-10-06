package url.shortner.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import url.shortner.Entity.RegisterData;
import url.shortner.Entity.Role;
import url.shortner.Entity.Userinfo;
import url.shortner.Repository.RoleRepository;
import url.shortner.Repository.UserRepository;

@Component
public class UserService {
    
   @Autowired
    UserRepository userrepo;

    @Autowired
    private AuthenticationManager authmanger;

    @Autowired
    JwtService jwtService;

    @Autowired
    RoleRepository roleRepository;

   //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

   public boolean adduser(RegisterData registerData){
 
    if(userrepo.findByUsername(registerData.getUsername())!=null)
        return false;

       Userinfo user = new Userinfo();
       user.setPassword(passwordEncoder().encode(registerData.getPassword()));
       user.setEmail(registerData.getEmail());
       user.setUsername(registerData.getUsername());
       user.setName(registerData.getName());
       Set<Role> roles  = new HashSet<>();
       for(String role: registerData.getRoles())
           if(role!=null) roles.add(roleRepository.findByName(role));
       //user.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
       user.setRoles(roles);
       userrepo.save(user);
       return true;
   }



   public Userinfo getuser(String username){
       return userrepo.findByUsername(username);
   }
   public Userinfo getuser(String mail,String password){
      Userinfo user = userrepo.findByEmailAndPassword(mail, password);
      return user;
     }

     public List<Userinfo> getalluser(){
         return userrepo.findAll();
     }

     public PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
     }

    public String verify(Userinfo user) {
        Authentication authentication =
                authmanger.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
     if(authentication.isAuthenticated()){

         return jwtService.generateToke(user.getUsername());

     }


     return "Failed";

   }
}
