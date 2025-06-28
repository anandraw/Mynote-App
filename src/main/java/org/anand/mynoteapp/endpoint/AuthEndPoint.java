package org.anand.mynoteapp.endpoint;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.anand.mynoteapp.dto.LoginRequest;
import org.anand.mynoteapp.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "Authentication & Registration APIs")
@RequestMapping("/api/v1/user")
public interface AuthEndPoint {

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user and sends verification email link",
            tags = {"Auth"}
    )
    @PostMapping("/")
    ResponseEntity<?> registerUser(@RequestBody UserRequest user, HttpServletRequest request) throws Exception;

    @Operation(
            summary = "User login",
            description = "Authenticates user and returns login response including token",
            tags = {"Auth"}
    )
    @PostMapping("/login")
    ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) throws Exception;
}
