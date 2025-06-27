package org.anand.mynoteapp.controller;

import org.anand.mynoteapp.dto.PasswordChngRequest;
import org.anand.mynoteapp.dto.UserResponce;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.service.UserService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getProfile() {
        User loggedInUser = CommonUtil.getLoggedInUser();
        UserResponce user = modelMapper.map(loggedInUser, UserResponce.class);
        return CommonUtil.createBuildResponse(user, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChngRequest passwordChngRequest) {
        userService.changePassword(passwordChngRequest);
        return CommonUtil.createBuildResponseMessagenew("Password Change Success",HttpStatus.OK);
    }
}
