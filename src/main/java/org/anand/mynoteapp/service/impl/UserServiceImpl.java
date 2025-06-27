package org.anand.mynoteapp.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.anand.mynoteapp.dto.EmailReuest;
import org.anand.mynoteapp.dto.PasswordChngRequest;
import org.anand.mynoteapp.dto.PswdResetRequest;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.anand.mynoteapp.repository.UserRepository;
import org.anand.mynoteapp.service.UserService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    @Override
    public void changePassword(PasswordChngRequest passwordRequest) {
        User loggedInUser = CommonUtil.getLoggedInUser();
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), loggedInUser.getPassword())) {
             throw new IllegalArgumentException("Old password is incorrect !");
        }
        String encodedPass=passwordEncoder.encode(passwordRequest.getNewPassword());
        loggedInUser.setPassword(encodedPass);
        userRepo.save(loggedInUser);

    }

    @Override
    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception {
        User user=userRepo.findByEmail(email);
        if (ObjectUtils.isEmpty(user)){
            throw new ResourceNotFoundException("Invalid Email !");
        }
        String passwordRestToken = UUID.randomUUID().toString();
        user.getStatus().setPasswordResetToken(passwordRestToken);
        User saveUser=userRepo.save(user);
        String url=CommonUtil.getUrl(request);
        sendEmailRequest(saveUser,url);
    }

    private void sendEmailRequest(User user, String url) throws Exception {
        String message = "Hi <b>[[username]]</b> "
                +"<br><p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=[[url]]>Change my password</a></p>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p><br>"
                + "Thanks,<br>Enotes.com";

        message = message.replace("[[username]]", user.getFirstName());
        message = message.replace("[[url]]", url + "/api/v1/home/verify-pswd-link?uid=" + user.getUserId() + "&&code="
                + user.getStatus().getPasswordResetToken());

        EmailReuest emailRequest = EmailReuest.builder().to(user.getEmail())
                .title("Password Reset").subject("Password Reset link").message(message).build();

        // send password reset email to user
        emailService.sendEmail(emailRequest);
    }

    @Override
    public void verifyPswdResetLink(Integer uid, String code) throws Exception {
        User user = userRepo.findById(uid).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));
        verifyPasswordResetToken(user.getStatus().getPasswordResetToken(),code);

    }

    private void verifyPasswordResetToken(String existToken, String reqToken) {
        if (StringUtils.hasText(reqToken)){
            if (!StringUtils.hasText(existToken)){
                throw  new IllegalArgumentException("Already password reset");
            }
            if (!existToken.equals(reqToken)){
                throw new IllegalArgumentException("invalid url");
            }
        }
        else {
            throw new IllegalArgumentException("Invalid Token");
        }
    }

    @Override
    public void resetPassword(PswdResetRequest pswdResetRequest) throws Exception {
        User user=userRepo.findById(pswdResetRequest.getUid())
                  .orElseThrow(() -> new ResourceNotFoundException("invalid user"));
        String encodePassword = passwordEncoder.encode(pswdResetRequest.getNewPassword());
        user.setPassword(encodePassword);
        user.getStatus().setPasswordResetToken(null);
        userRepo.save(user);
    }
}
