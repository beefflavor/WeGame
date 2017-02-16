package com.theironyard.repositories;

import com.theironyard.entities.Rating;
import com.theironyard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sparatan117 on 1/26/17.
 */
public interface RattingRepository extends JpaRepository<Rating, Integer>{
    List<Rating> findAllByUser(User user);
    Rating findFirstByUser(User user);
}
