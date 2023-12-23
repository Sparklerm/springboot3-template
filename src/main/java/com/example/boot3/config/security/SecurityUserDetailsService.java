package com.example.boot3.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 06:17
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUserDetails securityUserDetails = new SecurityUserDetails();
        securityUserDetails.setUsername("user");
        securityUserDetails.setPassword("G7EeTPnuvSU41T68qsuc/g==");

        return securityUserDetails;
    }
}
