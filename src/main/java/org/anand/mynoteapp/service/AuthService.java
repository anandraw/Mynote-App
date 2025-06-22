package org.anand.mynoteapp.service;

import org.anand.mynoteapp.dto.LoginRequest;
import org.anand.mynoteapp.dto.LoginResponce;
import org.anand.mynoteapp.dto.UserRequest;

public interface AuthService {

    public Boolean register(UserRequest userRequest, String url) throws Exception;
    public LoginResponce login(LoginRequest loginRequest) throws Exception;
}
