package test.Controllers;

import org.jasypt.util.password.BasicPasswordEncryptor;
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
import test.config.ApplicationConfig;
import test.config.UserRepository;
import test.model.Special;
import test.model.User;

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

    private DateFormat dateFormat;

    public MainController(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        userRepository = (UserRepository) ctx.getBean("userRepository");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @RequestMapping(value="/test")
    @ResponseBody
    public String test(){
        return "HELLO";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User user){
        System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " is trying to login.");
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        User check = userRepository.findByUsername(user.getUsername());
        System.out.println(user.getPassword());
        if(!passwordEncryptor.checkPassword(user.getPassword(), check.getPassword())){
            System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " failed to login.");
            return new ResponseEntity<String>("Username or Password Incorrect" ,HttpStatus.UNAUTHORIZED);
        }
        System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " logged in successfully.");
        return new ResponseEntity<String>("Logged In" ,HttpStatus.OK);
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody User user){
        String temp = user.getPassword();
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String encrypted = passwordEncryptor.encryptPassword(temp);
        user.setPassword(encrypted);
        userRepository.save(user);

        return new ResponseEntity<String>("Registered",HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> update(@RequestBody Special special){

        return new ResponseEntity<String>("Updated" ,HttpStatus.OK);
    }
}
