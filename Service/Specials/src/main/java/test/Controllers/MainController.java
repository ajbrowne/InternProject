package test.Controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import test.config.ApplicationConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Created by maharb on 6/11/14.
 */

@Controller
@RequestMapping(value="/v1/test")
public class MainController {

    private DateFormat dateFormat;

    public MainController(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @RequestMapping(value="/test")
    @ResponseBody
    public String test(){
        return "HELLO";
    }


}
