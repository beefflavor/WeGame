package com.theironyard.repositories;

import com.theironyard.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sparatan117 on 1/26/17.
 */
public interface GameRepository extends JpaRepository<Game, Integer>{
    Game findFristByName(String name);
    Game findFristByPlatform(String platform);
}
