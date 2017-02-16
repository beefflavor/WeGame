package com.theironyard.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.theironyard.entities.Game;
import com.theironyard.entities.Match;
import com.theironyard.entities.Mode;
import com.theironyard.entities.User;
import com.theironyard.repositories.GameRepository;
import com.theironyard.repositories.MatchRepository;
import com.theironyard.repositories.ModeRepository;
import com.theironyard.repositories.UserRepository;
import com.theironyard.services.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.theironyard.controllers.UserController.SESSION_USERNAME;

/**
 * Created by sparatan117 on 1/30/17.
 */
@Controller
public class MatchController{

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    ModeRepository modeRepository;

    @Autowired
    TwilioService twilioService;


    /**
     * finds a user with the session username and adds that user to start match and adds all the games with models.
     * @param session current HttpSession
     * @param model to be passed to view
     * @return to start-match
     */
    @RequestMapping(path = "/start-match",method = RequestMethod.GET)
    public String startMatch(HttpSession session, Model model){
        if (session.getAttribute(UserController.SESSION_USERNAME) == null) {
            return "redirect:/login";
        }
        model.addAttribute("games", gameRepository.findAll());
        model.addAttribute("user", userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME)));

        return "start-match";
    }

    /**get method that takes the match id the is pasted to it and finds that make and passes it along with models to
     * found page.
     * @param session current httpSession
     * @param model to be passed to view
     * @param matchId to find a match
     * @return to found
     */
    @RequestMapping(path = "/found/{matchId}", method = RequestMethod.GET)
    public String found(HttpSession session, Model model, int matchId){
        model.addAttribute("user", userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME)));
        model.addAttribute("match", matchRepository.findOne(matchId));
        return "found";
    }

    /**
     *first finds a selected game with an id that is passed in.then finds the user with the session. finds the mode the
     * that was passed in by a sting, lists all the matches that has isMatched false, the selected game, and the mode
     * that was picked.alos takes a string gamertag that is passed in and sets that to the player's gamertag. if the size
     * of the list of unmatched games is 0 it creates a new match with the parameters that was passed in and sets the session
     * user to player one. loops through that list once  and checks to see if player two is null and the session user is
     * not player one. if the session user fits into that he/she is set as player two and ismatched is set to true and
     * saved.then using twilio the program will text both players to let them know we have found them a match.if a match
     * if found user will be redirected to the found page. if not the user is sent to the looking page.
     * @param session current httpSession
     * @param id to find a game
     * @param redirectAttributes to send the match attributes found page
     * @param gamerTag to set the gamer tag
     * @param mode to se the mode
     * @return found page of the match or looking page
     */
    @RequestMapping(path = "/start-match", method = RequestMethod.POST)
    public String findMatch(HttpSession session, int id, RedirectAttributes redirectAttributes, String gamerTag, String mode){
        Game selectedGame = gameRepository.findOne(id);
        User player = userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME));
        Mode pickedMode = modeRepository.findFirstByMode(mode);
        List<Match> matches = matchRepository.findByIsMatchedAndPickedGameAndPickedMode(false, selectedGame, pickedMode);
        player.setGamerTag(gamerTag);
        userRepository.save(player);
        if(matches.size() == 0){
            Match match = new Match(player, selectedGame, pickedMode);
            matchRepository.save(match);
            matches.add(match);

        }
        for(Match m: matches){
            if(m.getPlayerTwo() == null && player != m.getPlayerOne()){
                m.setPlayerTwo(player);
                m.setMatched(true);
                matchRepository.save(m);
                redirectAttributes.addAttribute("matchId", m.getId());
//                twilioService.sendSMS("+1" + m.getPlayerOne().getPhone());
//                twilioService.sendSMS("+1" + m.getPlayerTwo().getPhone());
                return "redirect:/found/"+ m.getId();
            }
            return "redirect:/looking/" + m.getId();

        }
        return "redirect:/";
    }


    /**
     * finds a match by the id that is passed in and sets ismatched to true and saves it. so it stops the program form
     * looking for a match. returns users to slash.
     * @param id to find a match
     * @return to home page
     */
    @RequestMapping(path = "/stop", method = RequestMethod.POST)
    public String stop(int id){
        Match match = matchRepository.findOne(id);
        match.setMatched(true);
        matchRepository.save(match);
        return "redirect:/";
    }

    /**
     * using the path it takes an id and finds a match with it. checks to see if isMatched is true. if it is, it will
     * redirect the attributes of the match to the found page. if not it will return to the looking page.
     * @param matchId to find a match
     * @param model to be passed to view
     * @param redirectAttributes to send the match attributes to found page
     * @return looking page or found page
     */
    @RequestMapping(path = "/looking/{matchId}", method = RequestMethod.GET)
    public String looking(@PathVariable int matchId, Model model, RedirectAttributes redirectAttributes){
        Match match = matchRepository.findOne(matchId);

        if(match.isMatched()){
            redirectAttributes.addAttribute("matchId", match.getId());
            return "redirect:/found/" + match.getId();
        }

        model.addAttribute("match", match);

        return "looking";
    }

}
