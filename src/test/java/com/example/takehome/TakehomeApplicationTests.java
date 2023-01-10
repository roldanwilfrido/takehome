package com.example.takehome;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
class TakehomeApplicationTests {

    @Test
    void main() {

        TakehomeApplication.main(new String[]{});
        assertTrue("Mocking test", true);
    }

}
