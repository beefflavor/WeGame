package com.theironyard.controllers;

import com.theironyard.commands.LoginCommand;
import com.theironyard.commands.RegisterCommand;
import com.theironyard.entities.Game;
import com.theironyard.entities.Rating;
import com.theironyard.entities.User;
import com.theironyard.repositories.*;
import com.theironyard.utillities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by sparatan117 on 1/26/17.
 */
@Controller
public class UserController {
    public static final String SESSION_USERNAME = "username";

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository  gameRepository;

    @Autowired
    RattingRepository rattingRepository;

    @Autowired
    ModeRepository modeRepository;


    /**
     * takes the session and finds a user with it. then it grabs all the ratings and finds the avg rating for each.
     * then it finds all the games that the user has and finds all the games the user dose not have. after that it uses
     * models to add games the users doesn't have, a match that user may have, sets username form the session, sets the user
     * form session, and passes all the games in the database to the home page.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(HttpSession session, Model model){
        if(session.getAttribute(SESSION_USERNAME) == null){
            return "redirect:/login";
        }
        User user = userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME));
        List<Rating> ratings = rattingRepository.findAllByUser(user);
        ArrayList<Integer> allFriendly = new ArrayList<>();
        ArrayList<Integer> allSkill = new ArrayList<>();
        for(Rating a: ratings){
            int numA = a.getFriendliness();
            allFriendly.add(numA);
            int numB = a.getSkill();
            allSkill.add(numB);
         }
         int sum = 0;
         for(int i: allFriendly){
            sum += i;
         }
         int sum2 = 0;
         for(int i: allSkill){
             sum2 += i;
         }
         if(allFriendly.size() > 0){
             double avgFriend = sum / allFriendly.size();
             user.setFriendAvg(avgFriend);
         }
         if(allSkill.size() > 0){
             double avgSkill = sum2 / allSkill.size();
             user.setSkillAvg(avgSkill);
         }
        userRepository.save(user);
        List<Game> usersGames = (List<Game>) user.getGames();
        ArrayList<Game> nonUserGames = new ArrayList<>();
        for(Game g: gameRepository.findAll()){
            if(!usersGames.contains(g)){
                nonUserGames.add(g);
            }
        }

        model.addAttribute("nonUserGames", nonUserGames);
        model.addAttribute("usermatch", matchRepository.findFirstByIsMatchedAndPlayerOne(false, user));
        model.addAttribute("matches", matchRepository.findByIsMatched(false));
        model.addAttribute("username", session.getAttribute(SESSION_USERNAME));
        model.addAttribute("user", userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME)));
        model.addAttribute("games", gameRepository.findAll());

        return "home";
    }

    /**
     * get method for the login page and returns to login.
     * @param session
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLogin(HttpSession session){
        if(session.getAttribute(SESSION_USERNAME) != null){
            return "redirect:/";
        }
        return "login";
    }

    /**
     * get method for the registration page and returns registration.
     * @return
     */
    @RequestMapping(path = "/registration", method = RequestMethod.GET)
    public String getRegistration(){
        return "registration";
    }


    /**
     * uses the LoginCommand to log in a user and set the session's username after it first checks the users password
     * using passwordStorage.verifyPassword
     * @param session
     * @param command
     * @return
     * @throws PasswordStorage.InvalidHashException
     * @throws PasswordStorage.CannotPerformOperationException
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, LoginCommand command) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        User user = userRepository.findFirstByUsername(command.getUsername());
        if(user == null){
            return "redirect:/login";
        }
        else if(!PasswordStorage.verifyPassword(command.getPassword(), user.getPassword())){
            return "redirect:/login";
        }

        session.setAttribute(SESSION_USERNAME, user.getUsername());
        return "redirect:/";
    }

    /**
     * checks to see if user is in the session is null or not.then creates a new user using the RegisterCommand.sets the
     * new user in the session and returns to slash.
     * @param session
     * @param command
     * @return
     * @throws PasswordStorage.CannotPerformOperationException
     */
    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public String createUser(HttpSession session, RegisterCommand command) throws PasswordStorage.CannotPerformOperationException {
        User user = userRepository.findFirstByUsername(command.getUsername());
        if(user != null){
            return "redirect:/registration";
        }
        user = new User(command.getNameFirst(),command.getNameLast(),command.getBio(), command
        .getEmail(), command.getPhone(), command.getSex(),command.getUsername(),PasswordStorage.createHash(command.getPassword()), command.getAge());
        userRepository.save(user);
        session.setAttribute(SESSION_USERNAME, user.getUsername());
        return "redirect:/";

    }

    /**
     * logs the user out by invalidating the session.
     * @param session
     * @return
     */
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
