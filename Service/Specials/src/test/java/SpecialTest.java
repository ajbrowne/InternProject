//import main.config.SpecialRepository;
//import main.model.Special;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import main.Application;
//
//import java.util.Date;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Created by maharb on 6/17/14.
// */
//public class SpecialTest {
//
//    static ApplicationContext applicationContext = null;
//    static SpecialRepository specialRepository = null;
//
//
//    @BeforeClass
//    public static void setup(){
//        applicationContext = new AnnotationConfigApplicationContext(Application.class);
//        specialRepository = (SpecialRepository) applicationContext.getBean("specialRepository");
//    }
//
//    @Test
//    public void testCreateSpecial(){
//        Special special = new Special("Test Title", "Test Dealer", "Test Type", "Test Amount", "Test Description", 1, new Date(), new Date());
//        specialRepository.save(special);
//        Special getSpecial = specialRepository.findByTitle("Test Title");
//        assertEquals(getSpecial.getTitle(), "Test Title");
//        specialRepository.delete(special);
//    }
//
//}
