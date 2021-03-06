package no.hvl.dat251.recipeapp.service;

import lombok.extern.slf4j.Slf4j;
import no.hvl.dat251.recipeapp.domain.User;
import no.hvl.dat251.recipeapp.repository.UserRepository;
import no.hvl.dat251.recipeapp.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user by username: " + username);
        return Optional.ofNullable(getUserByEmail(username))
                .map(user -> new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    public User saveUser(User user) {
        user.setPassword(securityService.getPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Get current user: " + username);
        return getUserByEmail(username);
    }

}
