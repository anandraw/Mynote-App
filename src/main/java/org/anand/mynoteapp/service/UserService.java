package org.anand.mynoteapp.service;

import org.anand.mynoteapp.dto.UserDto;

public interface UserService {

    public Boolean register(UserDto userDto,String url) throws Exception;
}
