package net.andreweast.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class TestingRestController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/greeting", method = GET)
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        System.out.println(System.getProperty("SERVER_PORT"));
        System.out.println(System.getProperty("RDS_HOSTNAME"));
        System.out.println(System.getProperty("SPRING_DATASOURCE_URL"));
        System.out.println(System.getProperty("spring.datasource.url"));

        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}


/*
Will be marshalled by Jackson into JSON format: https://github.com/FasterXML/jackson
 */
class Greeting {
    private final long id;
    private final String greeting;

    public long getId() {
        return id;
    }

    public String getGreeting() {
        return greeting;
    }

    public Greeting(long id, String greeting) {
        this.id = id;
        this.greeting = greeting;
    }
}
