package org.anand.mynoteapp.service;

import org.anand.mynoteapp.dto.PasswordChngRequest;

public interface UserService {

    public void changePassword(PasswordChngRequest passwordRequest);
}
