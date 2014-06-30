package com.internproject.api.controllers;

import com.internproject.api.models.User;
import com.internproject.api.repositories.UserRepository;
import com.internproject.api.helpers.JsonHelper;
import com.internproject.api.helpers.PasswordHash;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by maharb on 6/18/14.
 */
@Controller
@RequestMapping(value="/v1/specials")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private Logger log = Logger.getLogger(UserController.class.getName());
    private JsonHelper jsonHelper;

    public UserController(){
        jsonHelper = new JsonHelper();
    }

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    /**
     * Current method for logging the user into the app.
     * Username/eamil and encrypted password are passed in and checked against
     * the matching username/email in the database. Is a post method so
     * we aren't sending passwords as part of a url.
     * Mapped to /v1/specials/login
     *
     * @param user - The user object of the user trying to login
     * @return - status of login and string of info.
     */
    @RequestMapping(value="/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User user){
        log.info(user.getUsername() + " is trying to login.");

        User check = userRepository.getUser(user);
        try {
            if(!PasswordHash.validatePassword(user.getPassword(), check.getPassword())){
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

    /**
     * Current method for user registration. Takes in a user object
     * encrypts the password that was sent and stores the object in
     * the Database.
     * Mapped to /v1/specials/register
     *
     * @param user - The user trying to register
     * @return - Return the user object upon registration.
     */
    @RequestMapping(value="/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<User> register(@RequestBody User user){
        User temp = user;
        try {
            String securePass = PasswordHash.generateStorngPasswordHash(user.getPassword());
            user.setPassword(securePass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        User check = userRepository.save(user);
        if(check == null){
            log.info(user.getUsername() + " has already been registered to another user.");
            return new ResponseEntity<User>(check, HttpStatus.BAD_REQUEST);
        }
        log.info(user.getUsername() + " has registered as a new user.");
        return new ResponseEntity<User>(temp, HttpStatus.CREATED);
    }
}
