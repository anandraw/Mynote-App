package org.anand.mynoteapp.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.anand.mynoteapp.dto.PswdResetRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Home",description = "All the homeController apis")
@RequestMapping("/api/v1/home")
public interface HomeEndPoint {

    @Operation(
            summary = "Verify account",
            tags = {"Home"},
            description = "User account verification during account creation"
    )
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception;

    @Operation(
            summary = "Send email for password reset",
            tags = {"Home"},
            description = "Send an email containing a password reset link to the user's registered email"
    )
    @GetMapping("/send-email-reset")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception;

    @Operation(
            summary = "Verify password reset link",
            tags = {"Home"},
            description = "Verify if the password reset link (userId and code) is valid"
    )
    @GetMapping("/verify-pswd-link")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception;

    @Operation(
            summary = "Reset password",
            tags = {"Home"},
            description = "Allows the user to reset the password using the new password details"
    )
    @PostMapping("/reset-pswd")
    public ResponseEntity<?> resetPassword(@RequestBody PswdResetRequest pswdResetRequest) throws Exception;
}
