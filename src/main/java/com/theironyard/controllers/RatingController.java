package com.theironyard.controllers;

import com.theironyard.commands.RatingCommand;
import com.theironyard.entities.Match;
import com.theironyard.entities.Rating;
import com.theironyard.entities.User;
import com.theironyard.repositories.MatchRepository;
import com.theironyard.repositories.RattingRepository;
import com.theironyard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

import static com.theironyard.controllers.UserController.SESSION_USERNAME;

/**
 * Created by sparatan117 on 2/8/17.
 */
@Controller
public class RatingController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RattingRepository rattingRepository;

    @Autowired
    MatchRepository matchRepository;

    /**
     * grabs the id form the path to use for finding a match. finds a user with session and sees if that user is player
     * one or player two and adds the other user with model. the user form the session becomes the creator in the rating
     * object.
     * @param Model
     * @param session
     * @param Id
     * @return
     */
    @RequestMapping(path = "/create-rating/{Id}", method = RequestMethod.GET)
    public String getUser(Model Model, HttpSession session,@PathVariable int Id){
        if(session.getAttribute(SESSION_USERNAME) == null){
            return "redirect:/";
        }
        Match match = matchRepository.findOne(Id);
        User user = userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME));
        if( user == match.getPlayerOne()){
            Model.addAttribute("user", match.getPlayerTwo());
        }
        else {
            Model.addAttribute("user", match.getPlayerOne());
        }
        Model.addAttribute("creator", userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME)));
        return "create-rating";
    }

    /**
     * Using the ratingCommand to create a rating. takes in an id form userId and uses that id and the session user to
     * make a new rating and saves it.
     * @param command
     * @param session
     * @param userId
     * @return
     */
    @RequestMapping(path = "/create-rating", method = RequestMethod.POST)
    public String createRating(RatingCommand command, HttpSession session, int userId){
        User user = userRepository.findOne(userId);
        User creator = userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME));
        Rating rating = new Rating(command.getFriendliness(), command.getSkill(), command.getComment(), user, creator);
        rattingRepository.save(rating);
        return "redirect:/";
    }
}
