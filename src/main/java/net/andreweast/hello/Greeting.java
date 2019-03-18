package net.andreweast.hello;

/*
Will be marshalled by Jackson into JSON format: https://github.com/FasterXML/jackson
 */
public class Greeting {
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
