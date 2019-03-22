package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class Venue implements Serializable {
//    Building building // TODO: Restructure this so building and room numbers are related
//    RoomNumber roomNumber

    long id;

    String name;
    boolean isLab;
    int capacity;

    double locationX;
    double locationY;

    // TODO: the score the department running the job has given this venue
    HashMap<Long, Integer> departmentsScore;
    /* Logic of scores for venue:
        each venue needs to have a HashMap: department -> score
        when a module has been tentatively scheduled in that venue
        the module has an associated department(s)
        then can used that associated department to look up the score that department has given the venue
            (if a module has more than one department (i.e. cross-listed IT and Maths), then average their scores for that venue)
     */

    public Venue(long id, String name, boolean isLab, int capacity, double locationX, double locationY) {
        this.id = id;
        this.name = name;
        this.isLab = isLab;
        this.capacity = capacity;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "name='" + name + '\'' +
                ", isLab=" + isLab +
                ", capacity=" + capacity +
                ", locationX=" + locationX +
                ", locationY=" + locationY +
                '}';
    }

    private Room room;

    private static Random random = new Random();

    public static Venue getRandomVenue() {
        Venue venue = new Venue();
        venue.room = Room.getRandomRoom();
        return venue;
    }
    public Venue() {
    }

    public Room getRoom() {
        return room;
    }

//    public String toString() {
//        return room.name();
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    enum Room {
        IT201, IT202; //, IT204, IT205, IT125, IT250

        static Room getRandomRoom() {
            return values()[Venue.random.nextInt(values().length)];
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return room == venue.room;
    }
}
