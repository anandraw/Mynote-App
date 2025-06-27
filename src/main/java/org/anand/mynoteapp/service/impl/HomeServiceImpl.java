package org.anand.mynoteapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.anand.mynoteapp.entity.AccountStatus;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.anand.mynoteapp.exception.SuccessException;
import org.anand.mynoteapp.repository.UserRepository;
import org.anand.mynoteapp.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {
        log.info("HomeServiceImpl : verifyAccount() : Start");

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));
        if (user.getStatus().getVerificationCode()==null){
            log.info("message : Account already verified");
            throw new SuccessException("Account already verified");
        }

        if (user.getStatus().getVerificationCode().equals(verificationCode)){
            AccountStatus accountStatus = user.getStatus();
            accountStatus.setActive(true);
            accountStatus.setVerificationCode(null);
            user.setStatus(accountStatus);
            userRepo.save(user);
            log.info("message : account verification success");
            return true;
        }
        log.info("HomeServiceImpl : verifyAccount() : End");
        return false;
    }
}
