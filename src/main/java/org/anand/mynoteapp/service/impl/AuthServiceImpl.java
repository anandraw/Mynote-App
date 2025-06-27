package org.anand.mynoteapp.service.impl;

import org.anand.mynoteapp.dto.EmailReuest;
import org.anand.mynoteapp.dto.LoginRequest;
import org.anand.mynoteapp.dto.LoginResponce;
import org.anand.mynoteapp.dto.UserRequest;
import org.anand.mynoteapp.entity.AccountStatus;
import org.anand.mynoteapp.entity.Role;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.repository.RoleRepository;
import org.anand.mynoteapp.repository.UserRepository;
import org.anand.mynoteapp.security.CustomUserDetalis;
import org.anand.mynoteapp.service.JwtService;
import org.anand.mynoteapp.service.AuthService;
import org.anand.mynoteapp.utils.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Validation validator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager  manager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public Boolean register(UserRequest userDto, String url) throws Exception {

        validator.userValidation(userDto);
        modelMapper.typeMap(UserRequest.class, User.class).addMappings(m -> m.skip(User::setRoles));
        User user = modelMapper.map(userDto, User.class);
        setRole(userDto, user);
        AccountStatus status = AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveUser = userRepository.save(user);
        if (!ObjectUtils.isEmpty(saveUser)) {
            // send email
            emailSend(saveUser,url);
            return true;
        }
        return false;
    }


    private void setRole(UserRequest userRequest, User user) {
        List<Integer> roleIds = userRequest.getRoles().stream()
                .map(UserRequest.RoleDto::getRoleId)
                .toList();
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        user.setRoles(roles);
    }

    private void emailSend(User user,String url) throws Exception {

        String message="Hi,<b>[[username]]</b> "
                + "<br> Your account register sucessfully.<br>"
                +"<br> Click the below link verify & Active your account <br>"
                +"<a href='[[url]]'>Click Here</a> <br><br>"
                +"Thanks,<br>Enotes.com"
                ;

        message=message.replace("[[username]]", user.getFirstName());
        message=message.replace("[[url]]", url+"/api/v1/home/verify?uid="+user.getUserId()+"&&code="+user.getStatus().getVerificationCode());

        EmailReuest emailReuest=EmailReuest.builder()
                .to(user.getEmail())
                .title("Account Creating Confirmation")
                .subject("Account Created Success")
                .message(message)
                .build();
        emailService.sendEmail(emailReuest);
    }

    //login logic
    @Override
    public LoginResponce login(LoginRequest loginRequest) throws Exception {
        Authentication authenticate = manager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        if (authenticate.isAuthenticated()){
            CustomUserDetalis customUserDetalis=(CustomUserDetalis) authenticate.getPrincipal();

            String token= jwtService.generateToken(customUserDetalis.getUser());

            LoginResponce login = LoginResponce.builder()
                    .user(modelMapper.map(customUserDetalis.getUser(), UserRequest.class))
                    .token(token)
                    .build();
            return login;
        }
        return null;
    }
}
