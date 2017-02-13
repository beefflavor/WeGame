package com.theironyard.controllers;

import com.theironyard.commands.LoginCommand;
import com.theironyard.commands.RegisterCommand;
import com.theironyard.entities.Rating;
import com.theironyard.entities.User;
import com.theironyard.repositories.GameRepository;
import com.theironyard.repositories.MatchRepository;
import com.theironyard.repositories.RattingRepository;
import com.theironyard.repositories.UserRepository;
import com.theironyard.utillities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
        model.addAttribute("matches", matchRepository.findByIsMatched(false));
        model.addAttribute("username", session.getAttribute(SESSION_USERNAME));
        model.addAttribute("user", userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME)));
        model.addAttribute("games", gameRepository.findAll());

        return "home";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLogin(HttpSession session){
        if(session.getAttribute(SESSION_USERNAME) != null){
            return "redirect:/";
        }
        return "login";
    }
    @RequestMapping(path = "/registration", method = RequestMethod.GET)
    public String getRegistration(){
        return "registration";
    }

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

    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public String createUser(HttpSession session, RegisterCommand command) throws PasswordStorage.CannotPerformOperationException {
        User user = userRepository.findFirstByUsername(command.getUsername());
        if(user != null){
            return "redirect:/registration";
        }
        user = new User(command.getNameFirst(),command.getNameLast(),command.getPictureUrl(),command.getBio(), command
        .getEmail(), command.getPhone(), command.getSex(),command.getUsername(),PasswordStorage.createHash(command.getPassword()), command.getAge());
        userRepository.save(user);
        session.setAttribute(SESSION_USERNAME, user.getUsername());
        return "redirect:/";

    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
