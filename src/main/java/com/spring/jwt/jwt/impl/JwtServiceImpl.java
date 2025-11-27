package com.spring.jwt.jwt.impl;

import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.InspectorProfile;
import com.spring.jwt.entity.SalesPerson;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.jwt.JwtConfig;
import com.spring.jwt.jwt.JwtService;
import com.spring.jwt.repository.DealerRepository;
import com.spring.jwt.repository.InspectorProfileRepo;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.security.UserDetailsCustom;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service

@Slf4j
public class JwtServiceImpl implements JwtService {
    private final UserRepository userRepository;

    private final JwtConfig jwtConfig;

    private final DealerRepository dealerRepository;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtServiceImpl(@Lazy JwtConfig jwtConfig, UserDetailsService userDetailsService, DealerRepository dealerRepository,
                          UserRepository userRepository) {
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
        this.dealerRepository = dealerRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getKey() {
        byte[] key = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public String generateToken(UserDetailsCustom userDetailsCustom) {
        Instant now = Instant.now();

        List<String> roles = userDetailsCustom.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("Roles: {}", roles);

        String dealerId = null;
        String userId = null;
        String userProfileId = null;
        String inspectorProfileId = null;
        String salesPersonId = null;
        String firstName = userDetailsCustom.getFirstName();

        if (roles.contains("DEALER")) {
            Optional<Dealer> byEmail = dealerRepository.findByEmail(userDetailsCustom.getUsername());
            if (byEmail.isPresent() && !byEmail.get().getStatus()) {
                throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Your Account is Not Active Please Contact The Administrator");
            }
            dealerId = userDetailsCustom.getDealerId();
            userId = userDetailsCustom.getUserId();
        }
        if (roles.contains("USER")) {
            userId = userDetailsCustom.getUserId();
            userProfileId = userDetailsCustom.getUserProfileId();
        }
        if (roles.contains("INSPECTOR")) {
            User Inspector= userRepository.findByEmail(userDetailsCustom.getUsername());
            InspectorProfile inspectorProfile = Inspector.getInspectorProfile();
            if(inspectorProfile.getStatus().toString().equals("false")) {
                throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Your Account is Not Active Please Contact The Administrator");
            }
            userId = userDetailsCustom.getUserId();
            inspectorProfileId = userDetailsCustom.getInspectorProfileId();
        }
        if (roles.contains("SALESPERSON")) {
            User salesPerson = userRepository.findByEmail(userDetailsCustom.getUsername());
            SalesPerson sales = salesPerson.getSalesPerson();
            if (sales.getStatus().toString().equals("false") ){
                throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Your Account is Not Active Please Contact The Administrator");
            }
            userId = userDetailsCustom.getUserId();
            salesPersonId = userDetailsCustom.getSalesPersonId();
        }
        if (roles.contains("ADMIN")) {
            userId = userDetailsCustom.getUserId();
        }

        log.info("firstName: {}", firstName);
        log.info("dealerId: {}", dealerId);
        log.info("userId: {}", userId);
        log.info("userProfileId: {}", userProfileId);
        log.info("inspectorProfileId: {}", inspectorProfileId);
        log.info("salesPersonId: {}", salesPersonId);

        return Jwts.builder()
                .setSubject(userDetailsCustom.getUsername())
                .claim("firstname", firstName)
                .claim("dealerId", dealerId)
                .claim("userId", userId)
                .claim("userProfileId", userProfileId)
                .claim("inspectorProfileId", inspectorProfileId)
                .claim("salesPersonId", salesPersonId)
                .claim("authorities", roles)
                .claim("roles", roles)
                .claim("isEnable", userDetailsCustom.isEnabled())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(jwtConfig.getExpiration())))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    @Override
    public boolean isValidToken(String token) {
        final String username = extractUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return !ObjectUtils.isEmpty(userDetails);
    }

    private String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token){
        Claims claims = null;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Token expiration");
        }catch (UnsupportedJwtException e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Token's not supported");
        }catch (MalformedJwtException e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Invalid format 3 part of token");
        }catch (SignatureException e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "Invalid format token");
        }catch (Exception e){
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getLocalizedMessage());
        }

        return claims;
    }


}

