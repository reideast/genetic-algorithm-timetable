package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO: This was made as...an enum??
public class Module implements Serializable {
    long id;

    String name;

    int numEnrolled;

    // TODO: Any departments which are offering this module for one of their courses. Used to find department preferences for buildings
//    List<Long> departmentIds;

    // TODO: to be used to find lecturer preferences for timeslots
//    long lecturerId;

    public Module(long id, String name, int numEnrolled) {
        this.id = id;
        this.name = name;
        this.numEnrolled = numEnrolled;
    }

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", numEnrolled=" + numEnrolled +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String moduleName;

    public Module(String moduleName) {
        this.moduleName = moduleName;
    }

//    public String toString() {
//        return moduleName;
//    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    private static final List<Module> ALL_MODULES = new ArrayList<>(Arrays.asList(new Module("AA000"), new Module("AA001"), new Module("AA002"), new Module("AA003"), new Module("AA004"), new Module("AA005"), new Module("AA006"), new Module("AA007"), new Module("AA008"), new Module("AA009"), new Module("AA010"), new Module("BB011"), new Module("BB012"), new Module("BB013"), new Module("BB014"), new Module("BB015"), new Module("BB016"), new Module("BB017"), new Module("BB018"), new Module("BB019"), new Module("BB020"), new Module("CC021"), new Module("CC022"), new Module("CC023"), new Module("CC024"), new Module("CC025"), new Module("CC026"), new Module("CC027"), new Module("CC028"), new Module("CC029"), new Module("CC030"), new Module("DD031"), new Module("DD032"), new Module("DD033"), new Module("DD034"), new Module("DD035")));
    public static int getAllModulesSize() {
        return ALL_MODULES.size();
    }
    public static Module getFromAllModulesByIndex(int i) {
        return ALL_MODULES.get(i);
    }
}
