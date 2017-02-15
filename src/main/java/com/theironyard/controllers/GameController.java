package com.theironyard.controllers;

import com.theironyard.commands.GameCommand;
import com.theironyard.commands.ModeCommand;
import com.theironyard.entities.Game;
import com.theironyard.entities.Mode;
import com.theironyard.entities.User;
import com.theironyard.repositories.GameRepository;
import com.theironyard.repositories.ModeRepository;
import com.theironyard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Collection;
import java.util.List;

import static com.theironyard.controllers.UserController.SESSION_USERNAME;

/**
 * Created by sparatan117 on 1/28/17.
 */
@Controller
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModeRepository modeRepository;

    /**
     * finds user by the session username and gets a list of all the games and passes to addgame using model.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(path = "/addgame", method = RequestMethod.GET)
    public String home(HttpSession session,Model model) {

        if (session.getAttribute(UserController.SESSION_USERNAME) == null) {
            return "redirect:/login";
        }
        model.addAttribute("games", gameRepository.findAll());
        model.addAttribute("user", userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME)));

        return "addgame";
    }

    /**
     * takes an id form the path and finds a game and passes it to game-detail using model.
     * @param model
     * @param gameId
     * @return
     */
    @RequestMapping(path = "/game-detail/{gameId}", method = RequestMethod.GET)
    public String gameDetail(Model model, @PathVariable int gameId){
        Game game = gameRepository.findOne(gameId);

        model.addAttribute("games", game);
        model.addAttribute("modes", modeRepository.findByGame(game));
        return "game-detail";
    }

    /**
     * finds a user with the session username. uses GameComand to create a new game and saves and returns to slash.
     * @param session
     * @param command
     * @return
     */
    @RequestMapping(path = "/addgame", method = RequestMethod.POST)
    public String addGame(HttpSession session, GameCommand command) {
        if (session.getAttribute(SESSION_USERNAME) == null) {
            return "redirect:/login";
        }
        Game game = new Game(command.getName(), command.getPlatform());
        gameRepository.save(game);

        return "redirect:/";

    }

    /**
     * takes an id and finds a game and uses that game to get a list of modes and deletes the game and modes and
     * returns to slash.
     * @param id
     * @return
     */
    @RequestMapping(path = "/delete-game", method = RequestMethod.POST)
    public String deleteGame(int id){
        Game game = gameRepository.findOne(id);
        List<Mode> modes = modeRepository.findByGame(game);
        modeRepository.delete(modes);
        gameRepository.delete(game);
        return "redirect:/";
    }

    /**
     * finds a user using session username. uses an id to find a game and adds the game to a list of games that the
     * user has.then returns to slash.
     * @param session
     * @param id
     * @return
     */
    @RequestMapping(path = "add-Games", method = RequestMethod.POST)
    public String addGame(HttpSession session, int id){
        User user = userRepository.findFirstByUsername((String) session.getAttribute(UserController.SESSION_USERNAME));
        if(user == null){
            return "redirect:/login";
        }
        Game game = gameRepository.findOne(id);
        Collection<Game> games= user.getGames();
        games.add(game);
        userRepository.save(user);
        return "redirect:/";
    }

    /**
     * takes a id to find a game and grab the list of modes.then uses ModeCommand to create a new mode and save the mode
     * and the game and returns to the game-detail page of that game.
     * @param command
     * @param id
     * @return
     */
    @RequestMapping(path = "/create-mode", method = RequestMethod.POST)
    public String createMode(ModeCommand command, int id){
        Game game = gameRepository.findOne(id);
        Collection<Mode> modes = game.getModes();
        Mode mode = new Mode(command.getMode(), game);
        modeRepository.save(mode);
        modes.add(mode);
        gameRepository.save(game);
        return "redirect:/game-detail/" + id;
    }

}
