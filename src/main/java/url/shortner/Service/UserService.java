package url.shortner.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

   public boolean adduser(Userinfo user){
 
    if(userrepo.findByUsername(user.getUsername())!=null)
        return false;

       user.setPassword(passwordEncoder().encode(user.getPassword()));
     
      // String hashedPassword = passwordEncoder.encode(user.getPassword());
      // user.setPassword(hashedPassword);

       userrepo.save(user);
       return true;
   }

   public boolean loginuser(Userinfo user){
      List<Userinfo> userlist = userrepo.findAll();
      for(Userinfo currentuser: userlist){
       if(currentuser.getEmail().equals(user.getEmail()))
         if(currentuser.getPassword().equals(user.getPassword())){
            return true;
         }
      }
    return false;
   }

   public Userinfo getuser(String mail){
    Userinfo user = userrepo.findByEmail(mail);
    return user;
   }
   public Userinfo getuser(String mail,String password){
      Userinfo user = userrepo.findByEmailAndPassword(mail, password);
      return user;
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
