package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void register(String name, String email, String rawPassword) {
        email = email.trim().toLowerCase();
        if (rawPassword.length() < 6) {
            throw new IllegalArgumentException("Пароль должен быть не короче 6 символов");
        }
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже зарегистрирован");
        }
        User user = new User();
        user.setName(name.trim());
        user.setEmail(email);
        user.setPassword(encoder.encode(rawPassword));
        repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}