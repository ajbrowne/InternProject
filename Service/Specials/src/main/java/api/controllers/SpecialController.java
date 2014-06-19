package api.controllers;

import api.models.Special;
import api.repositories.SpecialRepository;
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

    @Autowired
    private SpecialRepository specialRepository;
    private Logger log = Logger.getLogger(SpecialController.class.getName());

    public SpecialController(SpecialRepository specialRepository){
        this.specialRepository = specialRepository;
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
    @RequestMapping(value = "/specialTest",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Special> createSpecial(@RequestBody Special special){
        specialRepository.save(special);
        log.info("New special added: Title=" + special.getTitle() + ", dealer=" + special.getDealer());
        return new ResponseEntity<Special>(special, HttpStatus.CREATED);
    }

    /**
     * This method is the gel all specials method.
     * It is mapped to /v1/specials/special
     * and takes in no parameters
     *
     * @return it returns a list of all of the specials
     */
    @RequestMapping(value = "/special",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Special>> getAllSpecials(){
        return new ResponseEntity<List<Special>>(specialRepository.findAll() ,HttpStatus.OK);
    }

    /**
     * This method is used to get specials based on any of the parameters
     * that were passed in. All of the parameters are optional.
     * Mapped to /v1/specials/special
     *
     * @param title - title of the special we are looking for
     * @param id - id of the special we are looking for
     * @param type - the type of the special we are looking for
     * @param dealer - all specials with this dealer_id
     * @return - the list of all specials that match the values passed in
     */
    @RequestMapping(value = "/special",method = RequestMethod.GET, params = {"title", "id", "type", "dealer"})
    @ResponseBody
    public ResponseEntity<List> getMatchingSpecials(@RequestParam(value = "title", required = false) String title,
                                                   @RequestParam(value = "id", required = false) String id,
                                                   @RequestParam(value = "type", required = false) String type,
                                                   @RequestParam(value = "dealer", required = false) String dealer){
        //Create a special using the data passed in as a variable
        Special test = new Special();
        test.setId(id);
        test.setTitle(title);
        test.setType(type);
        test.setDealer(dealer);
        List special = specialRepository.findMatching(test);
        //If no specials were found let the app know so the user can be notified
        if(special.size() == 0){
            log.info("No Specials found");
            return new ResponseEntity<List>(special ,HttpStatus.BAD_REQUEST);
        }
        //Successfully found specials
        log.info("Specials were found that matched the criteria");
        return new ResponseEntity<List>(special ,HttpStatus.OK);
    }
}
