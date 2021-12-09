/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
public class SpringConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SpringConfiguration.class);

    @Autowired
    private DataSource dataSource;

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider
                (new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    /**
     * Setup JOOQ
     * @return
     */
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(SQLDialect.H2);
        jooqConfiguration.settings()
                .withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED)
                .withRenderNameCase(RenderNameCase.AS_IS);
        return jooqConfiguration;
    }


    @Bean
    CommandLineRunner initDatabase(DSLContext create, SimpleEntityDao dao) {
        return args -> {


            //For now we are using in-memory db so always create table and insert data on startup
            String scriptName = "createTables-H2.sql";
            runScript(scriptName, create);

            SimpleEntityVO vo1 = new SimpleEntityVO();
            vo1.setXid("XID_1");
            vo1.setName("Name 1");
            vo1.setEnabled(true);
            dao.insert(vo1);
            log.info("Preloading " + vo1);

            SimpleEntityVO vo2 = new SimpleEntityVO();
            vo2.setXid("XID_2");
            vo2.setName("Name 2");
            vo2.setEnabled(false);
            dao.insert(vo2);
            log.info("Preloading " + vo2);
        };
    }

    void runScript(String scriptName, DSLContext create) {
        try (InputStream in = getClass().getResourceAsStream(scriptName)) {
            Connection connection = dataSource.getConnection();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String[] script = reader.lines().toArray(String[]::new);

                StringBuilder statement = new StringBuilder();

                for (String line : script) {
                    // Trim whitespace
                    line = line.trim();

                    // Skip comments
                    if (line.startsWith("--"))
                        continue;

                    statement.append(line);
                    statement.append(" ");
                    if (line.endsWith(";")) {
                        // Execute the statement
                        Statement stmt = connection.createStatement();
                        stmt.executeUpdate(statement.toString());
                        stmt.close();
                        //create.execute(statement.toString());
                        statement.delete(0, statement.length() - 1);
                    }
                }
            }finally {
                connection.close();
            }
        }catch(IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
