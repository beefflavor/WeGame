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

    @RequestMapping(path = "/addgame", method = RequestMethod.GET)
    public String home(HttpSession session,Model model) {

        if (session.getAttribute(UserController.SESSION_USERNAME) == null) {
            return "redirect:/login";
        }
        model.addAttribute("games", gameRepository.findAll());
        model.addAttribute("user", userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME)));

        return "addgame";
    }
    @RequestMapping(path = "/game-detail/{gameId}", method = RequestMethod.GET)
    public String gameDetail(Model model, @PathVariable int gameId){
        Game game = gameRepository.findOne(gameId);

        model.addAttribute("games", game);
        model.addAttribute("modes", modeRepository.findByGame(game));
        return "game-detail";
    }

    @RequestMapping(path = "/addgame", method = RequestMethod.POST)
    public String addGame(HttpSession session, GameCommand command) {
        if (session.getAttribute(SESSION_USERNAME) == null) {
            return "redirect:/login";
        }
        Game game = new Game(command.getName(), command.getPlatform(), command.getPlayerCount());
        gameRepository.save(game);

        return "redirect:/";

    }

    @RequestMapping(path = "/delete-game", method = RequestMethod.POST)
    public String deleteGame(int id){
        Game game = gameRepository.findOne(id);
        List<Mode> modes = modeRepository.findByGame(game);
        modeRepository.delete(modes);
        gameRepository.delete(game);
        return "redirect:/";
    }

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
    @RequestMapping(path = "/create-mode", method = RequestMethod.POST)
    public String createMode(ModeCommand command, int id){
        Game game = gameRepository.findOne(id);
        Collection<Mode> modes = game.getModes();
        Mode mode = new Mode(command.getMode(), game);
        modeRepository.save(mode);
        modes.add(mode);
        gameRepository.save(game);
        return "redirect:/addgame";
    }

}
