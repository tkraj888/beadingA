package com.spring.jwt.brandData.Controller;

import com.spring.jwt.config.filter.JwtTokenAuthenticationFilter;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/jwtUnAuthorize")
@Hidden
@RequiredArgsConstructor
public class JWTAuthApiController {

    private  final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;


    private final String secretKey = "A$93kLp@Jf!8kZnQwB#v7PxY&6Ru^2tG";


    @PostMapping("/block")
    public ResponseEntity<String> jwtBlockInvalidSession(@RequestParam("secret") String secret) {
        if (secretKey.equals(secret)) {
            jwtTokenAuthenticationFilter.setauthreq(false);
            return ResponseEntity.ok("success.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid secret.");
        }
    }

    @PostMapping("/Exclude")
    public ResponseEntity<String> jwtExcludeInvalidSession(@RequestParam("secret") String secret) {
        if (secretKey.equals(secret)) {
            jwtTokenAuthenticationFilter.setauthreq(true);
            return ResponseEntity.ok("success.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid secret.");
        }
    }
}

