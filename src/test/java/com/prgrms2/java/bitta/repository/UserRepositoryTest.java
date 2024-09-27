package com.prgrms2.java.bitta.repository;

import com.prgrms2.java.bitta.user.repository.UserRepository;
import com.prgrms2.java.bitta.user.entity.Role;
import com.prgrms2.java.bitta.user.entity.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 회원삽입(){
        IntStream.rangeClosed(1,10).forEach(i -> {
            User user = User.builder()
                    .username("username" + i)
                    .password(passwordEncoder.encode("1111"))
                    .email("user"+i+"@prgrms2.com")
                    .location("서울시"+i+"번지")
                    .profilePicture(i+".jpg")
                    .role(Role.Actor)
                    .build();
            User savedUser = userRepository.save(user);
            assertNotNull(savedUser);
            log.info("----------"+savedUser.toString());
        });
    }

    @Test
    public void 회원조회(){
        String email = "user1@prgrms2.com";

        Optional<User> foundUser = userRepository.findByEmail(email);
        assertNotNull(foundUser);
        assertEquals(email, foundUser.get().getEmail());

        log.info("Found User"+foundUser.get());
        log.info("email"+foundUser.get().getEmail());
    }

    @Test
    @Transactional
    @Commit
    public void 회원삭제(){
        String email = "user10@prgrms2.com";
        Optional<User> foundUser = userRepository.findByEmail(email);
        if(foundUser.isPresent()) {
            userRepository.deleteByEmail(email);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}