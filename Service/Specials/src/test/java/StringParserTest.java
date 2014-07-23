import com.internproject.api.helpers.StringParser;
import org.junit.Test;

/**
 * Created by maharb on 6/26/14.
 */
public class StringParserTest {

    @Test
    public void parserTest(){
        String test = "2014 Chevrolet Corvette Stingray";
        System.out.println(StringParser.mapValues(StringParser.parseString(test)));
    }
}
