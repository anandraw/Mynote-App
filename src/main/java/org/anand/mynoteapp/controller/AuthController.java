package org.anand.mynoteapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.anand.mynoteapp.dto.LoginRequest;
import org.anand.mynoteapp.dto.LoginResponce;
import org.anand.mynoteapp.dto.UserRequest;
import org.anand.mynoteapp.service.AuthService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest user, HttpServletRequest request) throws Exception {
        log.info("AuthController : registerUser() : Exceution Start");
        String url=CommonUtil.getUrl(request);
        Boolean register = authService.register(user,url);
        if (register) {
            log.info("Success : {}","Register Success");
            return CommonUtil.createBuildResponse("Register Success", HttpStatus.OK);
        }
        log.info("AuthController : registerUser() : Exceution End");
        return CommonUtil.createErrorResponse("Register Failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) throws Exception {
           log.info("AuthController : loginUser() : Execution Start");
           LoginResponce loginResponce = authService.login(loginRequest);
           if (ObjectUtils.isEmpty(loginResponce)) {
               log.info("Error : {}","Login Unsuccessful");
               return CommonUtil.createErrorResponseMessage("Login Failed Bad Credentials", HttpStatus.INTERNAL_SERVER_ERROR);
           }
           return CommonUtil.createBuildResponse(loginResponce, HttpStatus.OK);
    }
}
