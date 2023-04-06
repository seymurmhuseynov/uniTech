package com.uniTech.services;

import com.uniTech.entities.User;
import com.uniTech.exceptions.AccessDeniedException;
import com.uniTech.exceptions.AlreadyExistException;
import com.uniTech.models.RequestPin;
import com.uniTech.models.ResponseData;
import com.uniTech.repos.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseData<?> register(RequestPin requestPin) {
        Optional<User> oldUser = userRepository.findByPin(requestPin.getPin());
        if (oldUser.isEmpty()) {
            userRepository.save(User.builder()
                    .pin(requestPin.getPin())
                    .password(passwordEncoder.encode(requestPin.getPassword()))
                    .createdDate(LocalDateTime.now())
                    .build());
            return ResponseData.ok();
        } else {
            throw new AlreadyExistException();
        }
    }
    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        return userRepository.findByPin(pin).orElseThrow(AccessDeniedException::new);
    }
}
