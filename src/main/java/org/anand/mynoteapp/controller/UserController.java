package org.anand.mynoteapp.controller;

import org.anand.mynoteapp.dto.PasswordChngRequest;
import org.anand.mynoteapp.dto.UserResponce;
import org.anand.mynoteapp.endpoint.UserEndPoint;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.service.UserService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController implements UserEndPoint {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> getProfile() {
        User loggedInUser = CommonUtil.getLoggedInUser();
        UserResponce user = modelMapper.map(loggedInUser, UserResponce.class);
        return CommonUtil.createBuildResponse(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changePassword(@RequestBody PasswordChngRequest passwordChngRequest) {
        userService.changePassword(passwordChngRequest);
        return CommonUtil.createBuildResponseMessagenew("Password Change Success",HttpStatus.OK);
    }
}
