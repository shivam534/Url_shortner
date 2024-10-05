package url.shortner.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import url.shortner.Entity.Urls;

import java.util.List;

@Repository
public interface  UrlsRepository extends JpaRepository<Urls,Long>{
    Urls findByShorturl(String shorturl);
    List<Urls> findAllByShorturl(String shorturl);
    List<Urls> findAllByUsername(String username);
    List<Urls> findAllByLongurl(String longurl);
    Urls findByUsernameAndLongurl(String username , String longurl);
}
