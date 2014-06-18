package main.Controllers;

import main.Helpers.JsonHelp;
import main.config.ApplicationConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Created by maharb on 6/11/14.
 */

@Controller
@RequestMapping(value="/v1/main")
public class MainController {

    private DateFormat dateFormat;
    private JsonHelp jsonHelp;

    public MainController(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        jsonHelp = new JsonHelp();
    }

    @RequestMapping(value= "/main", produces = "application/json")
    @ResponseBody
    public String test(){
        return jsonHelp.jsonGen("test");
    }


}
