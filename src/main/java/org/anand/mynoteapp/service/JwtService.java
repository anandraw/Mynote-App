package org.anand.mynoteapp.service;

import org.anand.mynoteapp.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String generateToken(User user);
    public String extractUsername(String token);
    public boolean validateToken(String token, UserDetails user);
}
