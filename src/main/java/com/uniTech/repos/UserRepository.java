package com.uniTech.repos;

import com.uniTech.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByPin(String pin);
}
