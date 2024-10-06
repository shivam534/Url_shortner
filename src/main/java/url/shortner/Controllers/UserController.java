package url.shortner.Controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import url.shortner.Entity.RegisterData;
import url.shortner.Entity.Urls;
import url.shortner.Entity.Userinfo;
import url.shortner.Service.UrlService;
import url.shortner.Service.UserService;
import url.shortner.groups.Logingroup;
import url.shortner.groups.Registergroup;


@RestController
public class UserController {
    
    @Autowired
    UserService userservice;
    @Autowired
    UrlService urlservice;
    @GetMapping("/getuser")
    public ResponseEntity<Userinfo> getMethodName(@RequestParam String email) {
        Userinfo user = userservice.getuser(email);
        return ResponseEntity.status(HttpStatus.FOUND).body(user);
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@Validated(Registergroup.class) @RequestBody RegisterData data, BindingResult result) {
        if(result.hasErrors()){
             // Create a map to hold the field name and error messages
            Map<String, String> errors = new HashMap<>();

            // Get all the validation errors from BindingResult
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError error : fieldErrors) {
                // Store field name and corresponding error message in the map
                errors.put(error.getField(), error.getDefaultMessage());
            }

            // Return the errors with a bad request (400) status
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

      
        Map<String , String> response = new HashMap<>();
        if(!userservice.adduser(data)){
            response.put("status", HttpStatus.CONFLICT.toString());
            response.put("message", data.getEmail()+" already exist");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        response.put("status", HttpStatus.CREATED+"");
        response.put("message", "User added successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/loginuser")
    public ResponseEntity<?> loginuser(@Validated(Logingroup.class) @RequestBody Userinfo user, BindingResult result, HttpSession session){
              if(result.hasErrors()){
                Map<String,String> errors = new HashMap<>();
                 List<FieldError> errorlist = result.getFieldErrors();
                 for(FieldError error:errorlist){
                      errors.put(error.getField(), error.getDefaultMessage());
                 }
                 return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
              }

//              Userinfo found = userservice.getuser(user.getEmail(),user.getPassword());
//              if(found==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            Map<String,String> response = new HashMap<>();
              response.put("access_token",userservice.verify(user));
              response.put("expires_in","36000");
              session.setAttribute("username",user.getUsername());
             return ResponseEntity.status(HttpStatus.FOUND).body(response);

    }

    @GetMapping("/shorten")
    public ResponseEntity<?> shorturl( @RequestParam String url) {
        
        Map<String,String> response = new HashMap<>();
            String shorturl = null;
        try {
             shorturl = urlservice.generateShorturl(url);
        }catch (Exception e){
            response.put("message","Url already exist");
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
        }
            response.put("Long url", url);
            response.put("Short url", "http://localhost:8080/" + shorturl);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    
     @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable String shortUrl) {
        String longUrl = urlservice.getlongurl(shortUrl);
        if (longUrl != null) {
            return ResponseEntity.status(302).location(URI.create(longUrl)).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/list")
//    public List<Urls> getallurl(){
//        return urlservice.getallurl();
//    }

    @GetMapping("/list/{username}")

    public ResponseEntity<?> listbyusername(@PathVariable String username){

        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(urlservice.getlistbyusername(username));
        }catch (Exception e){
             e.printStackTrace();
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
    }
    
}
