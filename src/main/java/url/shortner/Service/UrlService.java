package url.shortner.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import url.shortner.Entity.Urls;
import url.shortner.Repository.UrlsRepository;

@Component
public class UrlService {
    
@Autowired
UrlsRepository urlrepo;

@Autowired
    HttpSession session;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private static final Random random = new Random();
    private static final String endpoint = "http://localhost:8080/";


public String generateShorturl(String longurl){

    //System.out.println(session.getAttribute("username"));
    if(urlrepo.findByUsernameAndLongurl(String.valueOf(session.getAttribute("username")),longurl) !=null){
        return "exist";
    }
    String shorturl=generateRandomString();
    while(getlongurl(shorturl)!=null){
        shorturl=generateRandomString();
    }
    Urls url = new Urls();
    url.setLongurl(longurl);
    url.setShorturl(endpoint+shorturl);
    url.setUsername((String) session.getAttribute("username"));
    urlrepo.save(url);
    return shorturl;
}

public String getlongurl(String shorturl){
    //System.out.print(shorturl);
    Urls url=urlrepo.findByShorturl(endpoint+shorturl);
    if(url==null) return null; 
    return url.getLongurl();
}


public List<Urls> getallurl(){
  return urlrepo.findAll();
}


private String generateRandomString() {
    StringBuilder shortUrl = new StringBuilder(SHORT_URL_LENGTH);
    for (int i = 0; i < SHORT_URL_LENGTH; i++) {
        shortUrl.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
    }
    return shortUrl.toString();
}

    public List<Urls> getlistbyusername(String username) {
       return urlrepo.findAllByUsername(username);
    }
}
