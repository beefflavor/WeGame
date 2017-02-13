package com.theironyard.repositories;

import com.theironyard.entities.Game;
import com.theironyard.entities.Match;
import com.theironyard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by sparatan117 on 1/30/17.
 */
public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByIsMatchedAndPickedGame(boolean isMatched, Game game);
    List<Match> findByIsMatched(boolean isMatched);

}
