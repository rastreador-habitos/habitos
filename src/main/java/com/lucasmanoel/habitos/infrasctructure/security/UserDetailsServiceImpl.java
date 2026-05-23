package com.lucasmanoel.habitos.infrasctructure.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Não consulta banco, só cria um UserDetails com o email do token
        // Quem consulta o banco é o microservico usuario, o TOKEN já chega pronto.
        return User.builder()
                .username(email)
                .password("") // sem senha, autenticação é pelo JWT
                .roles("USER")
                .build();
    }
}
