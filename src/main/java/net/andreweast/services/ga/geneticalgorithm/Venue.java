package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Venue implements Serializable {
//    Building building // TODO: Restructure this so building and room numbers are related
//    RoomNumber roomNumber

    // Database ID
    long id;

    // DEBUG: Name is just for debugging
    String name;

    // Is a computer lab. If false, this is a lecture venue
    boolean isLab;

    // Seats for students
    int capacity;

    // Location of the building which this venue is in
    double locationX;
    double locationY;

    // The score the all departments have given this venue
    // When a module has been tentatively scheduled in this venue, can look up all departments teaching that module here
    // and then average the scores for fitness
    Map<Long, Integer> departmentsScores;

    public Venue(long id, String name, boolean isLab, int capacity, double locationX, double locationY, HashMap<Long, Integer> departmentsScores) {
        this.id = id;
        this.name = name;
        this.isLab = isLab;
        this.capacity = capacity;
        this.locationX = locationX;
        this.locationY = locationY;
        this.departmentsScores = departmentsScores;
    }

    @Override
    public String toString() {
        return "Venue{" +
                name +
                " size=" + capacity +
                " lab=" + isLab +
                " deptScores=[" + departmentsScores.keySet().stream().map(key -> key + "=" + departmentsScores.get(key)).collect(Collectors.joining(",")) +
                "]}";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLab() {
        return isLab;
    }

    public void setLab(boolean lab) {
        isLab = lab;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public Map<Long, Integer> getDepartmentsScores() {
        return departmentsScores;
    }

    public void setDepartmentsScore(Map<Long, Integer> departmentsScores) {
        this.departmentsScores = departmentsScores;
    }

//    enum Room {
//        IT201, IT202; //, IT204, IT205, IT125, IT250
//
//        static Room getRandomRoom() {
//            return values()[Venue.random.nextInt(values().length)];
//        }
//    }
//
//    private Room room;
//
//    private static Random random = new Random();
//
//    public static Venue getRandomVenue() {
//        Venue venue = new Venue();
//        venue.room = Room.getRandomRoom();
//        return venue;
//    }
//    public Venue() {
//    }
//
//    public Room getRoom() {
//        return room;
//    }
//
////    public String toString() {
////        return room.name();
////    }
}
