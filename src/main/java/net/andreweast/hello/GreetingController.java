package net.andreweast.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class GreetingController {
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
