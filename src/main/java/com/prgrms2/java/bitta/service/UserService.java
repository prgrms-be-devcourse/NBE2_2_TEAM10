package com.prgrms2.java.bitta.service;

import com.prgrms2.java.bitta.dto.UserDTO;
import com.prgrms2.java.bitta.entity.User;
import com.prgrms2.java.bitta.exception.UserException;
import com.prgrms2.java.bitta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class UserService {                              /**로그인 로직*/
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO read(String email, String password) {
        Optional<User> foundUser = userRepository.findByEmail(email);

        User user = foundUser.orElseThrow(UserException.BAD_CREDENTIALS::get);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw UserException.BAD_CREDENTIALS.get();
        }
    return new UserDTO(user);
    }

    public UserDTO read(String email) {                 /**단순 조회 로직*/
        Optional<User> foundUser = userRepository.findByEmail(email);
        User user = foundUser.orElseThrow(UserException.BAD_CREDENTIALS::get);
        return new UserDTO(user);
    }
}
