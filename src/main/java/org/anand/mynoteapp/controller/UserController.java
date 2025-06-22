package org.anand.mynoteapp.controller;

import org.anand.mynoteapp.dto.UserResponce;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.utils.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/")
    public ResponseEntity<?> getProfile() {
        User loggedInUser = CommonUtil.getLoggedInUser();
        UserResponce user = modelMapper.map(loggedInUser, UserResponce.class);
        return CommonUtil.createBuildResponse(user, HttpStatus.OK);
    }
}
