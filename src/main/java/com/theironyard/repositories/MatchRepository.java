package com.theironyard.repositories;

import com.theironyard.entities.Game;
import com.theironyard.entities.Match;
import com.theironyard.entities.Mode;
import com.theironyard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by sparatan117 on 1/30/17.
 */
public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByIsMatchedAndPickedGameAndPickedMode(boolean isMatched, Game game, Mode mode);
    List<Match> findByIsMatched(boolean isMatched);
    Match findFirstByIsMatchedAndPlayerOne(boolean isMatched, User user);
}
