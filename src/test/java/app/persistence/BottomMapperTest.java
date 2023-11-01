package app.persistence;

import app.entities.Bottom;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BottomMapperTest {
    private ConnectionPool testConnectionPool; // Use a test-specific connection pool

    @BeforeEach
    void setUp() {
        // Configure the test connection pool with test database settings
        // Replace these with your actual test database details
        String testDbUrl = "jdbc:postgresql://localhost:5432/cupcake";
        String testDbUser = "postgres";
        String testDbPassword = "postgres";

        testConnectionPool = ConnectionPool.getInstance(testDbUser, testDbPassword, testDbUrl, "testdb");

        // Create test data in the test database if needed
    }


    @Test
    void getAllBottomInfo() {
        try {
            List<Bottom> bottoms = BottomMapper.getAllBottomInfo(testConnectionPool);
            assertNotNull(bottoms);
            boolean isNameEqual = "chocolate".equals(bottoms.get(0).getName());
            System.out.println("Is name equal: " + isNameEqual);
        } catch (DatabaseException e) {
            System.out.println("you got a database error; might wanna do a todo list");
        }

    }
}