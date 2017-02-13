package com.theironyard.repositories;

import com.theironyard.entities.Game;
import com.theironyard.entities.Mode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sparatan117 on 1/31/17.
 */
public interface ModeRepository extends JpaRepository<Mode, Integer>{
    List<Mode> findByGame(Game game);
}
