
package com.spring.jwt.security;

import com.spring.jwt.entity.Dealer;
import com.spring.jwt.entity.InspectorProfile;
import com.spring.jwt.entity.SalesPerson;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsServiceCustom implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetailsCustom userDetailsCustom = getUserDetails(username);

        if(ObjectUtils.isEmpty(userDetailsCustom)){
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid username or password!" );
        }

        return userDetailsCustom;
    }

    private UserDetailsCustom getUserDetails(String username) {
        User user = userRepository.findByEmail(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid username or password!");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        String firstName = null;
        String dealerId = null;
        String userId = String.valueOf(user.getId());
        String userProfileId = null;
        String inspectorProfileId = null;
        String salesPersonId = null;

        if (authorities.contains(new SimpleGrantedAuthority("DEALER"))) {
            Dealer dealer = user.getDealer();
            if (dealer != null) {
                firstName = dealer.getFirstname();
                dealerId = String.valueOf(dealer.getId());
            }
        } else if (authorities.contains(new SimpleGrantedAuthority("USER"))) {
            if (user.getProfile() != null) {
                firstName = user.getProfile().getFirstName();
                userProfileId = String.valueOf(user.getProfile().getId());
            }
        } else if (authorities.contains(new SimpleGrantedAuthority("INSPECTOR"))) {
            InspectorProfile inspectorProfile = user.getInspectorProfile();
            if (inspectorProfile != null) {
                firstName = inspectorProfile.getFirstName();
                inspectorProfileId = String.valueOf(inspectorProfile.getId());
            }
        } else if (authorities.contains(new SimpleGrantedAuthority("SALESPERSON"))) {
            SalesPerson salesPerson = user.getSalesPerson();
            if (salesPerson != null) {
                firstName = salesPerson.getFirstName();
                salesPersonId = String.valueOf(salesPerson.getSalesPersonId());
            }
        }

        return new UserDetailsCustom(
                user.getEmail(),
                user.getPassword(),
                firstName,
                dealerId,
                userId,
                userProfileId,
                inspectorProfileId,
                salesPersonId,
                authorities
        );
    }

}