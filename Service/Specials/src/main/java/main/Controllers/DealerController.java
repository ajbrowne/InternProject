package main.Controllers;

import main.config.ApplicationConfig;
import main.config.DealerRepository;
import main.model.Dealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by maharb on 6/17/14.
 */
@Controller
@RequestMapping(value="/v1/dealers")
public class DealerController {

    @Autowired
    private DealerRepository dealerRepository;

    private DateFormat dateFormat;

    public DealerController(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        dealerRepository = (DealerRepository) ctx.getBean("dealerRepository");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @RequestMapping(value="/createDealer", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Dealer> createDealer(@RequestBody Dealer dealer){
        double[] first = dealer.getLoc().getCoordinates();
        Point point = new Point(first[0], first[1]);
        dealer.setLocation(point);
        dealerRepository.save(dealer);
        return new ResponseEntity<Dealer>(dealer, HttpStatus.OK);
    }

    @RequestMapping(value="/getDealerByLocation", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Dealer>> getDealerLoc(@RequestBody Dealer dealer){
        //Dealer newdealer = dealerRepository.findByName(dealer.getName());
        double[] first = dealer.getLoc().getCoordinates();
        Point point = new Point(first[0], first[1]);
        System.out.println(dateFormat.format(new Date()) + "  INFO: Location sent from app: " + point);
        List<Dealer> newDealer = dealerRepository.findByLocationNear(point).subList(0,10);
        System.out.println(dateFormat.format(new Date()) + "  INFO: number of dealers returned: " + newDealer.size());
        return new ResponseEntity<List<Dealer>>(newDealer, HttpStatus.OK);
    }

}
