package com.theironyard.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.theironyard.entities.Game;
import com.theironyard.entities.Match;
import com.theironyard.entities.Mode;
import com.theironyard.entities.User;
import com.theironyard.repositories.GameRepository;
import com.theironyard.repositories.MatchRepository;
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
    TwilioService twilioService;


    @RequestMapping(path = "/start-match",method = RequestMethod.GET)
    public String startMatch(HttpSession session, Model model){
        if (session.getAttribute(UserController.SESSION_USERNAME) == null) {
            return "redirect:/login";
        }
        model.addAttribute("games", gameRepository.findAll());
        model.addAttribute("user", userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME)));

        return "start-match";
    }

    @RequestMapping(path = "/found/{matchId}", method = RequestMethod.GET)
    public String found(HttpSession session, Model model, int matchId){
        model.addAttribute("user", userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME)));
        model.addAttribute("match", matchRepository.findOne(matchId));
        return "found";
    }

    @RequestMapping(path = "/start-match", method = RequestMethod.POST)
    public String findMatch(HttpSession session, int id, RedirectAttributes redirectAttributes, String gamerTag) throws InterruptedException {
        Game selectedGame = gameRepository.findOne(id);
        User player = userRepository.findFirstByUsername((String) session.getAttribute(SESSION_USERNAME));
        List<Match> matches = matchRepository.findByIsMatchedAndPickedGame(false, selectedGame);
        player.setGamerTag(gamerTag);
        userRepository.save(player);
        if(matches.size() == 0){
            Match match = new Match(player, selectedGame);
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
