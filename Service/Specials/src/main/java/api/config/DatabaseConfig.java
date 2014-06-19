package api.config;

import api.repositories.*;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;

/**
 * Created by maharb on 6/18/14.
 */
@Configuration
public class DatabaseConfig {


    @Bean
    public SpecialRepository specialRepository() throws UnknownHostException {
        return new MongoSpecialRepository(mongoTemplate());
    }

    @Bean
    public UserRepository userRepository() throws UnknownHostException {
        return new MongoUserRepository(mongoTemplate());
    }


    @Bean
    public DealerRepository dealerRepository() throws UnknownHostException {
        return new MongoDealerRepository(mongoTemplate());
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClient("localhost"), "Specials");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        MongoTemplate template = new MongoTemplate(mongoDbFactory());
        return template;
    }


}
