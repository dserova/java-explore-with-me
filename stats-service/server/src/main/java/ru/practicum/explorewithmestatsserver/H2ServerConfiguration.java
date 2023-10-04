package ru.practicum.explorewithmestatsserver;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@Configuration
public class H2ServerConfiguration {

    @Profile("debug")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9093");
    }

}
