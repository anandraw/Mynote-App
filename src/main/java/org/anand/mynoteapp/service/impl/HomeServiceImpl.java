package org.anand.mynoteapp.service.impl;

import org.anand.mynoteapp.entity.AccountStatus;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.anand.mynoteapp.exception.SuccessException;
import org.anand.mynoteapp.repository.UserRepository;
import org.anand.mynoteapp.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));
        if (user.getStatus().getVerificationCode()==null){
            throw new SuccessException("Account already verified");
        }

        if (user.getStatus().getVerificationCode().equals(verificationCode)){
            AccountStatus accountStatus = user.getStatus();
            accountStatus.setActive(true);
            accountStatus.setVerificationCode(null);
            user.setStatus(accountStatus); // this line is optional but good

            userRepo.save(user);
            return true;
        }
        return false;
    }
}
