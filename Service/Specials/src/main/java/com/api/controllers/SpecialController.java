package com.api.controllers;

import com.api.models.Special;
import com.api.services.SpecialService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * Created by maharb on 6/18/14.
 */
@Controller
@RequestMapping(value="/v1/specials")
public class SpecialController {

    private Logger log = Logger.getLogger(SpecialController.class.getName());
    @Autowired
    private SpecialService specialService;

    public SpecialController(SpecialService specialService){
        this.specialService = specialService;
    }

    public SpecialController(){}


    /**
     * This method is a method for us to create test
     * specials. Since the user will not be able to create specials
     * this will not be used in the application.
     * Mapped to: /v1/specials/specialTest
     *
     * @param special A special object is passed in and will be stored
     * @return returns the object on successful storing of the special
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Special> createSpecial(@RequestBody Special special){
        specialService.store(special);
        log.info("New special added: Title=" + special.getTitle() + ", dealer=" + special.getDealer());
        return new ResponseEntity<Special>(special, HttpStatus.CREATED);
    }


    /**
     * This method is used to get specials based on any of the parameters
     * that were passed in. All of the parameters are optional.
     * Mapped to /v1/specials/special
     *
     * @return - the list of all specials that match the values passed in
     */
    @RequestMapping(value = "/special",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Special>> getMatchingSpecials(@RequestParam(value = "info", required = false) Special test){

        if(test == null){
            log.info("All specials returned");
            return new ResponseEntity<List<Special>>(specialService.getAllSpecials() ,HttpStatus.OK);
        }
        List<Special> special = specialService.getSpecials(test);
        //If no specials were found let the app know so the user can be notified
        if(special.size() == 0){
            log.info("No Specials found");
            return new ResponseEntity<List<Special>>(special ,HttpStatus.BAD_REQUEST);
        }
        //Successfully found specials
        log.info(special.size() + " Specials were found that matched the criteria");
        return new ResponseEntity<List<Special>>(special ,HttpStatus.OK);
    }
}
