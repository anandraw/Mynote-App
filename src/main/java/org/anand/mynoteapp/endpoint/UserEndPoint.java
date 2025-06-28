package org.anand.mynoteapp.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.anand.mynoteapp.dto.PasswordChngRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/profile")
@Tag(name = "User",description = "User profile & password operations")
public interface UserEndPoint {

    @Operation(
            summary     = "Get profile",
            description = "Fetch the logged‑in user’s profile information",
            tags        = {"User"}
    )
    @GetMapping("/")
    public ResponseEntity<?> getProfile();

    @Operation(
            summary     = "Change password",
            description = "Change the password for the logged‑in user",
            tags        = {"User"}
    )
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChngRequest passwordChngRequest);

}
