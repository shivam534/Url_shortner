package url.shortner.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import url.shortner.Entity.Urls;
import url.shortner.Repository.UrlsRepository;
import url.shortner.Service.UrlService;
import url.shortner.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UrlService urlservice;

    @Autowired
    UserService userService;

    @GetMapping("/all_urls")
    public List<Urls> getallurl(){
        return urlservice.getallurl();
    }

    @GetMapping("/alluser")
    public ResponseEntity<?> getadmins(){
         return ResponseEntity.status(HttpStatus.FOUND).body(userService.getalluser());
    }
}
