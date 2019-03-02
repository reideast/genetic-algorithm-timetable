package net.andreweast

class Course {
    String courseName

    static final Course[] allCourses = [
//        new Course(courseName: "CT101"),
//        new Course(courseName: "CT201"),
//        new Course(courseName: "MA101"),
//        new Course(courseName: "MA201"),
        new Course("AA000"),
        new Course("AA001"),
        new Course("AA002"),
        new Course("AA003"),
        new Course("AA004"),
        new Course("AA005"),
        new Course("AA006"),
        new Course("AA007"),
        new Course("AA008"),
        new Course("AA009"),
        new Course("AA010"),
        new Course("BB011"),
        new Course("BB012"),
        new Course("BB013"),
        new Course("BB014"),
        new Course("BB015"),
        new Course("BB016"),
        new Course("BB017"),
        new Course("BB018"),
        new Course("BB019"),
        new Course("BB020"),
        new Course("CC021"),
        new Course("CC022"),
        new Course("CC023"),
        new Course("CC024"),
        new Course("CC025"),
        new Course("CC026"),
        new Course("CC027"),
        new Course("CC028"),
        new Course("CC029"),
        new Course("CC030"),
        new Course("DD031"),
        new Course("DD032"),
        new Course("DD033"),
        new Course("DD034"),
        new Course("DD035")
    ]

    Course(String courseName) {
        this.courseName = courseName
    }

    String toString() {
        courseName
    }
}
