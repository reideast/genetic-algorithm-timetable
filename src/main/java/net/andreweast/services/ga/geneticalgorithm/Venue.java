package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.Random;

public class Venue implements Serializable {
//    Building building // TODO: Restructure this so building and room numbers are related
//    RoomNumber roomNumber

    private Room room;

    private static Random random = new Random();

    public static Venue getRandomVenue() {
        Venue venue = new Venue();
        venue.room = Room.getRandomRoom();
        return venue;
    }

    public Room getRoom() {
        return room;
    }

    public String toString() {
        return room.name();
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
