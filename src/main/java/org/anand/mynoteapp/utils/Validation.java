package org.anand.mynoteapp.utils;

import org.anand.mynoteapp.dto.TodoDto;
import org.anand.mynoteapp.dto.UserDto;
import org.anand.mynoteapp.enums.TodoStatus;
import org.anand.mynoteapp.exception.ExistDataException;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.anand.mynoteapp.repository.RoleRepository;
import org.anand.mynoteapp.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.List;

@Component
public class Validation {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Validation(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public void todoValidation(TodoDto todo) throws Exception {

        TodoDto.StatusDto reqStatus = todo.getStatus();
        Boolean statusFound = false;
        for (TodoStatus st : TodoStatus.values()) {
            if (st.getId().equals(reqStatus.getId())) {
                statusFound = true;
            }
        }
        if (!statusFound) {
            throw new ResourceNotFoundException("invalid status");
        }
    }
    public void userValidation(UserDto userDto) throws Exception {

        if (!StringUtils.hasText(userDto.getFirstName())) {
            throw new IllegalArgumentException("first name is invalid");
        }

        if (!StringUtils.hasText(userDto.getLastName())) {
            throw new IllegalArgumentException("last name is invalid");
        }

        if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
            throw new IllegalArgumentException("email is invalid");
        }else{
           boolean userEmail = userRepository.existsByEmail(userDto.getEmail());
           if (userEmail) {
               throw new ExistDataException("Email already exist");
           }
        }

        if (!StringUtils.hasText(userDto.getMobNo()) || !userDto.getMobNo().matches(Constants.MOBNO_REGEX)) {
            throw new IllegalArgumentException("mobno is invalid");
        }

        if (CollectionUtils.isEmpty(userDto.getRoles())) {
            throw new IllegalArgumentException("role is invalid");
        } else {

            List<Integer> roleIds = roleRepository.findAll().stream().map(r -> r.getRoleId()).toList();

            List<Integer> invalidReqRoleids = userDto.getRoles().stream().map(r -> r.getRoleId())
                    .filter(roleId -> !roleIds.contains(roleId)).toList();

            if (!CollectionUtils.isEmpty(invalidReqRoleids)) {
                throw new IllegalArgumentException("role is invalid" + invalidReqRoleids);
            }

        }
    }
}
