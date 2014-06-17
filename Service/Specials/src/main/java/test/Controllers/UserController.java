package test.Controllers;

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
import test.Helpers.JsonHelp;
import test.Helpers.PasswordHash;
import test.config.ApplicationConfig;
import test.config.UserRepository;
import test.model.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by maharb on 6/17/14.
 */
@Controller
@RequestMapping(value="/v1/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    private PasswordHash passwordHash;
    private DateFormat dateFormat;
    private JsonHelp jsonHelper;

    public UserController(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        userRepository = (UserRepository) ctx.getBean("userRepository");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        passwordHash = new PasswordHash();
        jsonHelper = new JsonHelp();
    }

    @RequestMapping(value="/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User user){
        System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " is trying to login.");


        User check = userRepository.findByUsername(user.getUsername());

        try {
            if(!passwordHash.validatePassword(user.getPassword(), check.getPassword())){
                System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " failed to login.");

                return new ResponseEntity<String>(jsonHelper.jsonGen("Invalid Username or Password"), HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " logged in successfully.");
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
        return new ResponseEntity<String>(jsonHelper.jsonGen("Registered"), HttpStatus.OK);
    }
}
