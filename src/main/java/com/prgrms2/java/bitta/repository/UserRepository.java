package com.prgrms2.java.bitta.repository;

import com.prgrms2.java.bitta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
