package com.nistra.demy.platform;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Excluded for CI - requires full Spring context with all dependencies")
@SpringBootTest
class DemyPlatformApplicationTests {

    @Test
    void contextLoads() {
    }

}
