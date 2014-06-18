package main.Controllers;

import main.Helpers.JsonHelp;
import main.Helpers.PasswordHash;
import main.config.ApplicationConfig;
import main.config.UserRepository;
import main.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by maharb on 6/17/14.
 */
@Controller
@RequestMapping(value="/v1/specials/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    private PasswordHash passwordHash;
    private Logger log = Logger.getLogger(UserController.class.getName());
    private JsonHelp jsonHelper;

    public UserController(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        userRepository = (UserRepository) ctx.getBean("userRepository");
        passwordHash = new PasswordHash();
        jsonHelper = new JsonHelp();
    }

    @RequestMapping(value="/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User user){
        log.info(user.getUsername() + " is trying to login.");


        User check = userRepository.findByUsername(user.getUsername());

        try {
            if(!passwordHash.validatePassword(user.getPassword(), check.getPassword())){
                log.info(user.getUsername() + " failed to login.");

                return new ResponseEntity<String>(jsonHelper.jsonGen("Invalid Username or Password"), HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            log.info("No such user");
            return new ResponseEntity<String>(jsonHelper.jsonGen("Invalid Username or Password"), HttpStatus.UNAUTHORIZED);
        }

        log.info(user.getUsername() + " logged in successfully.");
        return new ResponseEntity<String>(jsonHelper.jsonGen("Login Success") ,HttpStatus.OK);
    }

    @RequestMapping(value="/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody User user){
        try {
            String securePass = passwordHash.generateStorngPasswordHash(user.getPassword());
            user.setPassword(securePass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        userRepository.save(user);
        log.info(user.getUsername() + " has registered as a new user.");
        return new ResponseEntity<String>(jsonHelper.jsonGen("Registered"), HttpStatus.OK);
    }
}
