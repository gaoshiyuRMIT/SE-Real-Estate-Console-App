import java.util.*;

public abstract class Property {
    private String address;
    private String suburb;
    private HashMap<String, Integer> capacity;
    private String type;
    private ArrayList<Inspection> inspections;
    private HashMap<String, String> documents;
    private Owner owner;
    private EmployeeAssigned employee;

}