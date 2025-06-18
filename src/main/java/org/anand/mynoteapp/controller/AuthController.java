package org.anand.mynoteapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.anand.mynoteapp.dto.UserDto;
import org.anand.mynoteapp.service.UserService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody UserDto user, HttpServletRequest request) throws Exception {
        String url=CommonUtil.getUrl(request);
        Boolean register = userService.register(user,url);
        if (register) {
            return CommonUtil.createBuildResponse("Register Success", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponse("Register Failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
