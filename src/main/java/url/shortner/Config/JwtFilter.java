package url.shortner.Config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.filter.OncePerRequestFilter;
import url.shortner.Service.JwtService;
import url.shortner.Service.MyUserDetailsService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String token  = null;

        if(authHeader!=null && authHeader.startsWith("Bearer")){
           token = authHeader.substring(7);
           try {
               username = jwtService.getUsernameFromToken(token);
               if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                   UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
                   if(jwtService.isValidToken(token,userDetails)){
                       UsernamePasswordAuthenticationToken authtoken =
                               new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                       // just getting the details of the request object
                       authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                       // Setting to the context that yes this token is already authenticated used in line 40
                       SecurityContextHolder.getContext().setAuthentication(authtoken);
                   }
               }
           }catch (ExpiredJwtException e){
               response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
               response.getWriter().write("Token has expired");
               return;

           }catch (JwtException e) {
               response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
               response.getWriter().write("Unauthorized: Invalid Token");
               return;
           }

        }
        filterChain.doFilter(request,response);

    }
}
