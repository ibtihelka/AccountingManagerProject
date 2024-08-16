package com.accounting_manager.accounting_auth.Controller;

import com.accounting_manager.accounting_auth.Config.JwtService;
import com.accounting_manager.accounting_auth.DTO.Request.*;
import com.accounting_manager.accounting_auth.DTO.Response.LoginResponse;
import com.accounting_manager.accounting_auth.DTO.Response.RefreshTokenResponse;
import com.accounting_manager.accounting_auth.DTO.Response.ResponseDTO;
import com.accounting_manager.accounting_auth.Entity.ThirdParty;
import com.accounting_manager.accounting_auth.Service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.accounting_manager.accounting_auth.Exception.IncompleteDataException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Log4j2
@Tag(name="Authentication Controller", description="Handles authentication-related operations such as registering users, authenticating, managing tokens, and resetting passwords.")
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtService jwtService;

    @Operation(summary = "Register a new accountant", description = "Registers a new accountant using the provided registration details. If successful, returns a confirmation message.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accountant registered successfully with the provided details", content = @io.swagger.v3.oas.annotations.media.Content(examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Accountant Registration", value = "{\n" +
                            "  \"message\": \"Please, Verify Your Email \"\n" +
                            "}")
            })),
            @ApiResponse(responseCode = "400", description = "Invalid input data or incomplete data provided", content = @io.swagger.v3.oas.annotations.media.Content(examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Used Phone Number", value = "{\n" +
                            "  \"message\": \"Used Phone Number\"\n" +
                            "}"),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Email already exists", value = "{\n" +
                            "  \"message\": \"Email already exists\"\n" +
                            "}"),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(name = "Incomplete Data", value = "{\n" +
                            "  \"message\": \"\"Siret Number is already used!\"\n" +
                            "}")
            }))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) throws Exception {
        try {
            String response = service.registerAccountant(request);
            return ResponseEntity.ok(new ResponseDTO(response));
        } catch (IllegalArgumentException | IncompleteDataException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get all third parties", description = "Retrieves a paginated list of all third parties based on the provided query parameters. The results can be filtered using query and discriminator parameters.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Third parties retrieved successfully"),
    })
    @GetMapping
    public ResponseEntity<Page<ThirdParty>> getAllThirdParties(
            @Parameter(name = "query", description = "Search query for filtering third parties, it can me an email, name...etc", example = "user@gmail.com") @RequestParam(required = false, defaultValue = "") String query,
            @Parameter(name = "discriminator", description = "Discriminator value for filtering", example = "PHYSICAL") @RequestParam(required = false, defaultValue = "") String discriminator,
            @Parameter(name = "page", description = "Page number for pagination", example = "0") @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(name = "size", description = "Number of records per page", example = "8") @RequestParam(name = "size", required = false, defaultValue = "8") int size) {
        Page<ThirdParty> thirdParties = service.getAllThirdParties(query, discriminator.toLowerCase(), page, size);
        return ResponseEntity.ok(thirdParties);
    }

    @Operation(summary = "Register a new client for a third party", description = "Registers a new client under a specified third party using the provided registration details. Returns the registered client details if successful.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client registered successfully under the specified third party"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or incomplete data provided")
    })
    @PostMapping("/{id}/register-client")
    public ResponseEntity<?> registerClient(
            @Parameter(name = "id", description = "ID of the accountant under which the client is to be registered", example = "1") @PathVariable Long id,
            @RequestBody RegisterRequest request
    ) throws Exception {
        try {
            ThirdParty response = service.registerClient(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IncompleteDataException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Confirm a client's account", description = "Confirms a client's account using the provided token. This typically completes the registration process for the client.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account approved successfully"),
            @ApiResponse(responseCode = "403", description = "The provided token is invalid or something went wrong during confirmation")
    })
    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(
            @RequestBody TokenRequest token
    ) {
        try {
            service.confirmClient(token.getToken());
            return ResponseEntity.ok(new ResponseDTO("Account Approved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDTO("Something Went Wrong"));
        }
    }

    @Operation(summary = "Authenticate a user", description = "Authenticates a user by validating the provided login credentials. " +
            "Returns a JWT token including the user details in the payload of the token and also returns a refresh token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully and JWT token returned, " +
                    "including user details in the payload of the token, along with a refresh token"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials provided")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody LoginRequest request
    ) {
        try {
            LoginResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(e.getMessage()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Validate a JWT token", description = "Validates the provided JWT token to ensure it is still valid. This can be used to check if the user session is still active.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token is valid and access is granted"),
            @ApiResponse(responseCode = "400", description = "Invalid Authorization header format"),
            @ApiResponse(responseCode = "401", description = "The provided token is invalid")
    })
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        String[] parts = authorizationHeader.split(" ");
        if (parts.length != 2 || !parts[0].equalsIgnoreCase("Bearer")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Authorization header");
        } else {
            if (jwtService.isTokenValid(parts[1])) {
                return ResponseEntity.ok(new ResponseDTO("Access granted"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Invalid token"));
            }
        }
    }

    @Operation(summary = "Logout a user", description = "Logs out the user by invalidating the provided JWT token, it clears the token from the cache. " +
            "This effectively ends the user's session.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token removed successfully and user logged out"),
            @ApiResponse(responseCode = "400", description = "Invalid Authorization header format"),
            @ApiResponse(responseCode = "404", description = "The provided token does not exist or is already invalid")
    })
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String[] parts = authorizationHeader.split(" ");
        if (parts.length != 2 || !parts[0].equalsIgnoreCase("Bearer")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Authorization header");
        } else if (jwtService.isTokenValid(parts[1])) {
            jwtService.removeToken(parts[1]);
            return ResponseEntity.ok("Token removed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token does not exist");
        }
    }

    @Operation(summary = "Reset a user's password", description = "Sends a verification link to the user's email to reset the password. This link allows the user to set a new password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Verification link sent to the provided email address"),
            @ApiResponse(responseCode = "404", description = "User not found with the provided email address")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody EmailRequest emailRequest) throws Exception {
        if (service.existsByEmail(emailRequest.getEmail())) {
            service.resetPassword(emailRequest.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Verification link is sent on your email address"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO("User not found with this email address"));
    }

    @Operation(summary = "Verify reset password token", description = "Verifies if the provided reset password token is valid. This is used as a preliminary check before allowing the user to reset their password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The reset password token is valid"),
            @ApiResponse(responseCode = "403", description = "The provided reset password token is invalid or expired")
    })
    @GetMapping("/reset-password-verify-token")
    public ResponseEntity<?> resetPasswordVerifyToken(
            @Parameter(name = "resetPwdToken", description = "Token used for resetting the password", example = "exampleToken") @RequestParam String resetPwdToken) {
        if (service.isValidToken(resetPwdToken)) {
            return ResponseEntity.ok("Valid reset pwd token");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDTO("Not a valid token"));
    }

    @Operation(summary = "Change a user's password", description = "Changes the user's password using the provided reset password token and new password. This completes the password reset process.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error or unable to change password. Try again")
    })
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        if (service.changePassword(changePasswordRequest.getPassword(), changePasswordRequest.getResetPwdToken())) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Password changed successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO("Unable to change password. Try again!"));
        }
    }

    @Operation(summary = "Refresh an access token", description = "Refreshes the access token using the provided refresh token. This is typically used to extend the user session.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Authorization header format"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshTokenHeader) {
        String[] parts = refreshTokenHeader.split(" ");
        if (parts.length != 2 || !parts[0].equalsIgnoreCase("Bearer")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Authorization header");
        } else {
            if (service.isValidToken(parts[1])) {
                try {
                    var accessToken = service.refreshToken(parts[1]);
                    return ResponseEntity.ok(new RefreshTokenResponse(accessToken));
                } catch (Exception e) {
                    log.error(e);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("Invalid refresh token"));
        }
    }

    @Operation(summary = "Update a third party", description = "Updates the third party details using the provided ID and new details. Returns the updated third party details if successful.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Third party updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or incomplete data provided"),
            @ApiResponse(responseCode = "404", description = "Third party not found with the provided ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateThirdParty(
            @Parameter(name = "id", description = "ID of the third party to be updated", example = "1") @PathVariable Long id,
            @RequestBody UpdateRequest updatedThirdParty) {
        try {
            Optional<?> updated = service.UpdateThirdParty(id, updatedThirdParty);
            if (updated.isPresent()) {
                return ResponseEntity.ok(updated.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException | IncompleteDataException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage()));
        }
    }
}
