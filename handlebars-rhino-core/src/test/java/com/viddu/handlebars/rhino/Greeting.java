package com.viddu.handlebars.rhino;

public class Greeting {
    private String name;
    private String greeting;

    public Greeting(String greeting, String name) {
        this.greeting = greeting;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}