package com.theironyard.repositories;

import com.theironyard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by sparatan117 on 1/26/17.
 */
public interface UserRepository extends JpaRepository<User, Integer>{
    User findFirstByUsername(String username);
}
