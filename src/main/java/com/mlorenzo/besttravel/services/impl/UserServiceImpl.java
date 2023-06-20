package com.mlorenzo.besttravel.services.impl;

import com.mlorenzo.besttravel.domain.documents.User;
import com.mlorenzo.besttravel.exceptions.NotFoundException;
import com.mlorenzo.besttravel.repositories.UserRepository;
import com.mlorenzo.besttravel.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public Map<String, Boolean> enableOrDisable(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    user.setEnabled(!user.getEnabled());
                    return Map.of("enabled", userRepository.save(user).getEnabled());
                })
                .orElseThrow(() -> new NotFoundException(String.format("User with username %s not found", username)));
    }

    @Override
    public Map<String, Set<String>> addRole(String username, String role) {
        final Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty())
            throw new NotFoundException(String.format("User with username %s not found", username));
        final User user = optionalUser.get();
        user.getRoles().add(role);
        return Collections.singletonMap("roles", userRepository.save(user).getRoles());
    }

    @Override
    public Map<String, Set<String>> deleteRole(String username, String role) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    user.getRoles().remove(role);
                    return Collections.singletonMap("roles", userRepository.save(user).getRoles());
                })
                .orElseThrow(() -> new NotFoundException(String.format("User with username %s not found", username)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String
                        .format("User with username %s not found", username)));
        final Set<GrantedAuthority> authorities = user.getRoles().stream()
                // Versión simplificada de la expresión "role -> new SimpleGrantedAuthority(role)"
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getEnabled(), true, true, true, authorities);
    }
}
