package net.andreweast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "RDS_HOSTNAME=TYPE_IN_THE_DATABASE_HOSTNAME_HERE",
        "RDS_PORT=5432",
        "RDS_DB_NAME=DATABASE_NAME_HERE",
        "RDS_USERNAME=USERNAME_HERE",
        "RDS_PASSWORD=PASSWORD_HERE",
        "SERVER_PORT=5000"
})
public class ApplicationTest {
    @Test
    public void contextLoads() throws Exception {
    }
}
