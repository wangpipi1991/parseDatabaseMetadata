package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
class DatabaseMetadataServiceTest {

    @Autowired
    DatabaseMetadataService databaseMetadataService;

    @Test
    public void contextLoads() {
        // Database connection config (You need to change these params to adapt to your own database config)
        String url = "jdbc:mysql://localhost:3306/";
        String databaseName = "sakila";
        String username = "root";
        String password = "admin";
        databaseMetadataService.parseDatabaseMetadata(url, databaseName, username, password);
    }
}
