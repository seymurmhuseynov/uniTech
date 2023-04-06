package com.uniTech.controller;

import com.uniTech.exceptions.NotFoundException;
import com.uniTech.models.RequestPin;
import com.uniTech.models.ResponseData;
import com.uniTech.models.ResponseJWT;
import com.uniTech.repos.UserRepository;
import com.uniTech.security.JwtProvider;
import com.uniTech.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/uni-tech/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<ResponseJWT> authenticateUser(@Valid @RequestBody RequestPin requestPin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestPin.getPin(), requestPin.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        userRepository.findByPin(requestPin.getPin()).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(ResponseJWT.builder()
                .token(jwt)
                .type("Bearer")
                .build());
    }
    @PostMapping("/register")
    public ResponseData<?> register(@RequestBody @Valid @NotNull RequestPin requestPin) {
        return userService.register(requestPin);
    }
}