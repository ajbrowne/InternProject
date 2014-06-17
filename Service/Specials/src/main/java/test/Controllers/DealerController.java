package test.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import test.config.ApplicationConfig;
import test.config.DealerRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
}
