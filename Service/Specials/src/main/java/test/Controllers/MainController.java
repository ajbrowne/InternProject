package test.Controllers;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
import test.Helpers.PasswordHash;
import test.config.ApplicationConfig;
import test.config.DealerRepository;
import test.config.SpecialRepository;
import test.config.UserRepository;
import test.model.Special;
import test.model.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by maharb on 6/11/14.
 */

@Controller
@RequestMapping(value="/v1/specials")
public class MainController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SpecialRepository specialRepository;
    @Autowired
    DealerRepository dealerRepository;

    private PasswordHash passwordHash;

    private DateFormat dateFormat;

    public MainController(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        userRepository = (UserRepository) ctx.getBean("userRepository");
        specialRepository = (SpecialRepository) ctx.getBean("specialRepository");
        dealerRepository = (DealerRepository) ctx.getBean("dealerRepository");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        passwordHash = new PasswordHash();
    }

    @RequestMapping(value="/test")
    @ResponseBody
    public String test(){
        return "HELLO";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User user){
        System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " is trying to login.");


        User check = userRepository.findByUsername(user.getUsername());

        try {
            if(!passwordHash.validatePassword(user.getPassword(), check.getPassword())){
                System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " failed to login.");

                return new ResponseEntity<String>(jsonGen("Invalid Username or Password"),HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " logged in successfully.");
        return new ResponseEntity<String>(jsonGen("Login Success") ,HttpStatus.OK);
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
        return new ResponseEntity<String>(jsonGen("Registered"), HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> createSpecial(@RequestBody Special special){

        return new ResponseEntity<String>(jsonGen("Created Special"), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> update(@RequestBody Special special){

        return new ResponseEntity<String>(jsonGen("Updated") ,HttpStatus.OK);
    }

    @RequestMapping(value = "/getSpecial", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Special> getSpecial(@RequestBody String info){

        return new ResponseEntity<Special>(new Special() ,HttpStatus.OK);
    }

    private String jsonGen(String response){
        JSONObject returnObj = new JSONObject();
        try {
            returnObj.put("response", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnObj.toString();
    }

}
