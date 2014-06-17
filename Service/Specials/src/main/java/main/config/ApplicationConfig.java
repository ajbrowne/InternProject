package main.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
* Created by maharb on 6/11/14.
*/

@Configuration
@EnableMongoRepositories
public class ApplicationConfig extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "Specials";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("localhost");
    }

}
