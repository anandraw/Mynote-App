package org.anand.mynoteapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.anand.mynoteapp.dto.PasswordChngRequest;
import org.anand.mynoteapp.dto.PswdResetRequest;

public interface UserService {

    public void changePassword(PasswordChngRequest passwordRequest);
    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception;
    public void verifyPswdResetLink(Integer uid, String code) throws Exception;
    public void resetPassword(PswdResetRequest pswdResetRequest) throws Exception;
}
