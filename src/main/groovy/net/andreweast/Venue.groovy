package net.andreweast

class Venue {
//    Building building // TODO: Restructure this so building and room numbers are related
//    RoomNumber roomNumber

    Room room

    static Random random = new Random()

    static Venue getRandomVenue() {
        new Venue(
            room: Room.getRandomRoom()
        )
    }

    String toString() {
        "${room.name()}"
    }

    enum Room {
        IT201, IT202//, IT204, IT205, IT125, IT250

        static Room getRandomRoom() {
            values()[Venue.random.nextInt(values().length)]
        }
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Venue venue = (Venue) o

        if (room != venue.room) return false

        return true
    }
}
