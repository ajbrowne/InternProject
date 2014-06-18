package main.Controllers;

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
import main.Helpers.JsonHelp;
import main.config.ApplicationConfig;
import main.config.SpecialRepository;
import main.model.Special;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by maharb on 6/17/14.
 */
@Controller
@RequestMapping(value="/v1/specials")
public class SpecialController {

    @Autowired
    SpecialRepository specialRepository;

    private Logger log = Logger.getLogger(SpecialController.class.getName());
    private DateFormat dateFormat;
    private JsonHelp jsonHelper;

    public SpecialController(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        specialRepository = (SpecialRepository) ctx.getBean("specialRepository");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        jsonHelper = new JsonHelp();
    }

    @RequestMapping(value = "/createSpecial", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> createSpecial(@RequestBody Special special){
        specialRepository.save(special);
        log.info("New special added: Title=" + special.getTitle() + ", dealer=" + special.getDealer());
        return new ResponseEntity<String>(jsonHelper.jsonGen("Created Special"), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> update(@RequestBody Special special){

        return new ResponseEntity<String>(jsonHelper.jsonGen("Updated") ,HttpStatus.OK);
    }

    @RequestMapping(value = "/getSpecialByTitle", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Special> getSpecialTitle(@RequestBody Special title){
        Special special = specialRepository.findByTitle(title.getTitle());
        if(special == null){
            log.info("No Special found that matches this title");
            return new ResponseEntity<Special>(special ,HttpStatus.BAD_REQUEST);
        }
        log.info("A special was found with the given title");
        return new ResponseEntity<Special>(special ,HttpStatus.OK);
    }

    @RequestMapping(value = "/getSpecialByID", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Special> getSpecialID(@RequestBody Special id){
        System.out.println(id.getId());
        Special special = specialRepository.findById(id.getId());
        if(special == null){
            log.info("No special was found with the given id");
            return new ResponseEntity<Special>(special ,HttpStatus.BAD_REQUEST);
        }
        log.info("A special was found with the given id");
        return new ResponseEntity<Special>(special ,HttpStatus.OK);
    }

    @RequestMapping(value = "/getSpecialByType", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Special> getSpecialType(@RequestBody Special type){
        System.out.println(type.getType());
        Special special = specialRepository.findByType(type.getType());
        if(special == null){
            log.info("No special was found with the given type");
            return new ResponseEntity<Special>(special ,HttpStatus.BAD_REQUEST);
        }
        log.info("A special was found with the given Type");
        return new ResponseEntity<Special>(special ,HttpStatus.OK);
    }

    @RequestMapping(value = "/getSpecialByDealer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Special> getSpecialDealer(@RequestBody Special dealer){
        System.out.println(dealer.getDealer());
        Special special = specialRepository.findByDealer(dealer.getDealer());
        if(special == null){
            log.info("No special was found with the given dealer");
            return new ResponseEntity<Special>(special ,HttpStatus.BAD_REQUEST);
        }
        log.info("A special was found with the given Dealer");
        return new ResponseEntity<Special>(special ,HttpStatus.OK);
    }
}
