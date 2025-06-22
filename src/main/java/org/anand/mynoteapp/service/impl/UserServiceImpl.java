package org.anand.mynoteapp.service.impl;

import org.anand.mynoteapp.dto.PasswordChngRequest;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.repository.UserRepository;
import org.anand.mynoteapp.service.UserService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    @Override
    public void changePassword(PasswordChngRequest passwordRequest) {
        User loggedInUser = CommonUtil.getLoggedInUser();
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), loggedInUser.getPassword())) {
             throw new IllegalArgumentException("Old password is incorrect !");
        }
        String encodedPass=passwordEncoder.encode(passwordRequest.getNewPassword());
        loggedInUser.setPassword(encodedPass);
        userRepo.save(loggedInUser);

    }
}
