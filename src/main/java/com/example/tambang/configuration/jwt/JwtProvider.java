package com.example.tambang.configuration.jwt;

import com.example.tambang.configuration.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.password}")
    private String secretKey;
    private final UserDetailsService userDetailsService;

    public String createToken(UserDetailsImpl userDetails, Long expiredMs){
        Date now = new Date();
        Date expiration = new Date(System.currentTimeMillis() + expiredMs);
        System.out.println("now = " + now + " exp = " + expiration);

        // 유저 권한 확인해서 ADMIN인지 확인한다.
        boolean isAdmin = userDetails.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Claims claims = Jwts.claims();
        if(isAdmin){
            claims.put("role", "ADMIN");
        }
        else{
            claims.put("role", "USER");
        }
        claims.put("userEmail", userDetails.getUsername());

        // jwt 만들어서 반환 (회원 정보를 포함한다.)
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
    // jwt 보유자의 권한 추출
    public Authentication getAuthentication(String token){
        String userEmail = this.getUserEmail(token);
        System.out.println("userEmail = " + userEmail);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        // UsernamePasswordAuthenticaionToken 만들어 반환
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token){
        // token 이름이 이메일이었다.
        return Jwts.parser().setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token).getBody().getSubject();
    }
    // Bearer. 부분 제거 메서드
    public String BearerRemove(String tokenWithBearer){
        return tokenWithBearer.substring("Bearer ".length());
    }
    public String resolveTokenFromRequest(HttpServletRequest request){
        // Authorization Header에서 토큰을 반환
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
    public boolean isExpired(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration();

        // expiration이 현재 시간보다 이전이면 만료됨
        if(expiration.before(new Date())){
            return true;
        }
        else{
            return false;
        }
    }
}
