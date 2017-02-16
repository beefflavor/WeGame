package com.theironyard;

import com.theironyard.controllers.GameController;
import com.theironyard.controllers.UserController;
import com.theironyard.entities.*;
import com.theironyard.repositories.*;
import com.theironyard.utillities.PasswordStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WePlayApplicationTests {

    User user1;
    User user2;
    User user3;
    Game game1;
    Game game2;
    Game game3;
    Mode mode;
    Match match;
    Match match2;

	@Autowired
    UserRepository userRepository;

	@Autowired
    GameRepository gameRepository;

	@Autowired
    MatchRepository matchRepository;

	@Autowired
    ModeRepository modeRepository;

	@Autowired
    RattingRepository rattingRepository;

	@Autowired
    WebApplicationContext wap;

	MockMvc mockMvc;

	@Before
    public void setup() throws PasswordStorage.CannotPerformOperationException {
	    mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
	    user1 = new User("bob", "smith", "bio", "fake@email.com", "1231321234",
                "male", "testUser1", PasswordStorage.createHash("password"), 23);
	    user2 = new User("bob2", "smith", "bio", "test@test.com", "1233241234",
                "female", "testUser2", PasswordStorage.createHash("password"), 24);
        user3 = new User("bob3", "smith2", "bio", "test2@test.com", "1235551234",
                "female", "testUser3", PasswordStorage.createHash("password"), 25);
	    game1 = new Game("testGame", "PC");
	    game2 = new Game("testGame2", "x-box");
	    game3 = new Game("testGame3", "playsation");
	    mode = new Mode("testMode");
        match = new Match(user2, game2, mode);
        match2 = new Match(user3, game3, mode);
        modeRepository.save(mode);
	    List<Mode> gameModes1 = (List<Mode>) game1.getModes();
        List<Mode> gameModes2 = (List<Mode>) game2.getModes();
        List<Mode> gameModes3 = (List<Mode>) game3.getModes();
	    game1.setModes(gameModes1);
	    game2.setModes(gameModes2);
	    game3.setModes(gameModes3);
	    userRepository.save(user1);
	    userRepository.save(user2);
	    userRepository.save(user3);
	    gameRepository.save(game1);
	    gameRepository.save(game2);
	    gameRepository.save(game3);
	    matchRepository.save(match);
	    matchRepository.save(match2);
    }
    @After
    public void after(){
	    for(Game game: gameRepository.findAll()){
	        List<Mode> modes = (List<Mode>) game.getModes();
	        modes.remove(mode);
        }
        for(User user: userRepository.findAll()){
	        user.setRatings(null);
        }
        matchRepository.deleteAll();
	    rattingRepository.deleteAll();
        modeRepository.deleteAll();
	    userRepository.deleteAll();
	    gameRepository.deleteAll();

    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                .param("username", "testUser1")
                .param("password", "password")
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        User user = userRepository.findFirstByUsername("testUser1");
        assertNotNull(user);
        assertTrue(PasswordStorage.verifyPassword("password", user.getPassword()));
    }

    @Test
    public void testCreateUser() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.post("/registration")
                .param("nameFirst", "joey")
                .param("nameLast", "mckinney")
                .param("bio", "a bio")
                .param("email", "someone@mail.com")
                .param("phone", "1231231234")
                .param("sex", "male")
                .param("username", "fakeUser")
                .param("password", "password")
                .param("age", "23")
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        User user = userRepository.findFirstByUsername("fakeUser");
        assertNotNull(user);
        assertTrue(PasswordStorage.verifyPassword("password", user.getPassword()));
    }

    @Test
    public void testAddGame()throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.post("/add-Games")
                .sessionAttr(UserController.SESSION_USERNAME, user1.getUsername())
                .param("id", String.valueOf(game1.getId()))
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        User user = userRepository.findFirstByUsername("testUser1");
        Game game = gameRepository.findFristByName("testGame");
        List<Game> userGames = (List<Game>) user.getGames();
        assertTrue(userGames.contains(game));
    }

    @Test
    public void testFindMatchNoMatch()throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.post("/start-match")
                .sessionAttr(UserController.SESSION_USERNAME, user1.getUsername())
                .param("id", String.valueOf(game1.getId()))
                .param("gamerTag", "testTag")
                .param("mode", "testMode")
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        User user = userRepository.findFirstByUsername("testUser1");
        Match match = matchRepository.findFirstByIsMatchedAndPlayerOne(false, user);

        assertNotNull(match);
        assertNotNull(user);

    }

    @Test
    public void testFindMatchWithMatch()throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.post("/start-match")
                .sessionAttr(UserController.SESSION_USERNAME, user3.getUsername())
                .param("id", String.valueOf(game2.getId()))
                .param("gamerTag", "tesTag")
                .param("mode", "testMode")
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        User user1 = userRepository.findFirstByUsername("testUser2");
        User user2 = userRepository.findFirstByUsername("testUser3");
        Match match = matchRepository.findFirstByIsMatchedAndPlayerOne(true, user1);

        assertNotNull(user2);
        assertNotNull(match);

    }

    @Test
    public void testStop()throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.post("/stop")
                .param("id", String.valueOf(match2.getId()))
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        User user = userRepository.findFirstByUsername("testUser3");
        Match match = matchRepository.findFirstByIsMatchedAndPlayerOne(true, user);

        assertNotNull(match);
    }

    @Test
    public void testCreateRating() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.post("/create-rating")
                .sessionAttr(UserController.SESSION_USERNAME, user1.getUsername())
                .param("userId", String.valueOf(user2.getId()))
                .param("friendliness", "3")
                .param("skill", "4")
                .param("comment", "a comment")

        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        User user =  userRepository.findFirstByUsername("testUser2");
        Rating rating = rattingRepository.findFirstByUser(user);

        assertNotNull(rating);
    }


	@Test
	public void contextLoads() {
	}
    
}
