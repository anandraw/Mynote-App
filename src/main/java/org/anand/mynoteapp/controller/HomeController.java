package org.anand.mynoteapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.anand.mynoteapp.dto.PswdResetRequest;
import org.anand.mynoteapp.service.HomeService;
import org.anand.mynoteapp.service.UserService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid,@RequestParam String code) throws Exception{
        Boolean isVerify = homeService.verifyAccount(uid, code);
        if (isVerify) {
            CommonUtil.createBuildResponse("Account Verification Success", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Invalid Verification link", HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/send-email-reset")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception{
         userService.sendEmailPasswordReset(email, request);
         return CommonUtil.createBuildResponseMessagenew("Email Send Success || Check Email Reset Password",HttpStatus.OK);
    }

    @GetMapping("/verify-pswd-link")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception{
         userService.verifyPswdResetLink(uid,code);
         return CommonUtil.createBuildResponseMessagenew("Verification Success",HttpStatus.OK);
    }

    @PostMapping("/reset-pswd")
    public ResponseEntity<?> resetPassword(@RequestBody PswdResetRequest pswdResetRequest) throws Exception {
        userService.resetPassword(pswdResetRequest);
        return CommonUtil.createBuildResponseMessagenew("Password Reser Success",HttpStatus.OK);
    }
}
