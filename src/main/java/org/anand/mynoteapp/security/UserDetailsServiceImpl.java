package org.anand.mynoteapp.security;

import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("email not found with this : "+ user.getEmail());
        }
        return new CustomUserDetalis(user);
    }
}
