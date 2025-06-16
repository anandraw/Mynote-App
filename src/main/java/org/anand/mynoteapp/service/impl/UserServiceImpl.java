package org.anand.mynoteapp.service.impl;

import org.anand.mynoteapp.dto.EmailReuest;
import org.anand.mynoteapp.dto.UserDto;
import org.anand.mynoteapp.entity.Role;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.repository.RoleRepository;
import org.anand.mynoteapp.repository.UserRepository;
import org.anand.mynoteapp.service.UserService;
import org.anand.mynoteapp.utils.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

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

    @Override
    public Boolean register(UserDto userDto) throws Exception {

        validator.userValidation(userDto);
        modelMapper.typeMap(UserDto.class, User.class).addMappings(m -> m.skip(User::setRoles));
        User user = modelMapper.map(userDto, User.class);
        setRole(userDto, user);
        User saveUser = userRepository.save(user);
        if (!ObjectUtils.isEmpty(saveUser)) {
            // send email
            emailSend(saveUser);
            return true;
        }
        return false;
    }

    private void setRole(UserDto userDto, User user) {
        List<Integer> roleIds = userDto.getRoles().stream()
                .map(UserDto.RoleDto::getRoleId)
                .toList();
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        user.setRoles(roles);
    }

    private void emailSend(User user) throws Exception {

        String message="Hi,<b>"+user.getFirstName()+"</b> "
                + "<br> Your account register successfully.<br>"
                +"<br> Click the below link verify & Active your account <br>"
                +"<a href='#'>Click Here</a> <br><br>"
                +"Thanks,<br>Enotes.com"
                ;

        EmailReuest emailReuest=EmailReuest.builder()
                .to(user.getEmail())
                .title("Account Creating Confirmation")
                .subject("Account Created Success")
                .message(message)
                .build();
        emailService.sendEmail(emailReuest);
    }
}
