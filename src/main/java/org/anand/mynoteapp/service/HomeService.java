package org.anand.mynoteapp.service;

public interface HomeService {
    public Boolean verifyAccount(Integer userId,String verificationCode) throws Exception;
}
