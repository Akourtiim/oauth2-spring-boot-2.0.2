package com.aak.configuration;

import com.aak.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * Created by ahmed on 21.5.18.
 */
public class JdbcUserDetails implements UserDetailsService{

    @Autowired
    private CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(username)
           .map(credentialRepository::findByName)
           .map(i -> new User(i.getName(),i.getPassword(),i.isEnabled(),true,true,true,i.getAuthorities()))
           .orElseThrow(() -> new UsernameNotFoundException("User"+String.valueOf(username)+"can not be found"));
    }
}
