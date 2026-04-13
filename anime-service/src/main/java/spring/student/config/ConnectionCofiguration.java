package spring.student.config;

import external.dependencies.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConnectionCofiguration {


    @Bean
    public Connection connectionMySql(){
        return new Connection("local","devMySql","API-KeyUNit");
    }

    @Bean(name = "connection")
   // @Primary
    public Connection connectionMyMongo(){
        return new Connection("local","devMongo","API-KeyUNit");
    }
}
