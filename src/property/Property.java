package property;

import java.util.*;

import SnE.*;
import consts.*;
import user.customer.*;
import user.employee.*;
import exception.*;

public abstract class Property {
    private static int idCounter = 0;

    private String id;
    private String address;
    private String suburb;
    private HashMap<String, Integer> capacity;
    private PropertyType type;
    private ArrayList<Inspection> inspections;
    private HashMap<String, String> documents;
    private PropertyStatus status;
    private Customer owner;
    private Employee assignee;
    private ArrayList<ApplicationBase> applications;


    public Property(String address, String suburb, HashMap<String, Integer> capacity,
                    PropertyType type, Customer owner) throws InvalidParamException {
        this.status = PropertyStatus.NotListed;
        this.suburb = suburb.toUpperCase();
        this.type = type;
        PropertyUtil.checkCapacity(capacity);
        this.capacity = capacity;
        this.address = address;
        this.id = Property.genId();
        this.owner = owner;
    }

    public Property(String address, String suburb, HashMap<String, Integer> capacity,
                    String typeS, Customer owner) throws InvalidParamException {
        this(address, suburb, capacity, PropertyType.valueOf(typeS), owner);
    }

    public ArrayList<ApplicationBase> getApplications() {
        return applications;
    }

    public boolean isOpenForInspection() {
        return this.status == PropertyStatus.ApplicationOpen
                || this.status == PropertyStatus.InspectionOpen;
    }

    public static String genId() {
        return String.format("p%08d", (Property.idCounter)++);
    }

    public String getId() {
        return id;
    }

    public HashMap<String, String> getDocuments() {
        return documents;
    }

    public void setEmployee(Employee e) {
        this.assignee = e;
    }

    public Employee getEmployee() {
        return assignee;
    }


    public Customer getOwner() {
        return owner;
    }

    public void list() throws OperationNotAllowedException{
        if (getStatus() != PropertyStatus.NotListed)
            throw new OperationNotAllowedException();
        setStatus(PropertyStatus.ApplicationOpen);
    }

    public void addDocument(String name, String uri) {
        documents.put(name, uri);
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public String getSuburb() {
        return suburb;
    }

	public boolean match(String address, String suburb,
                            HashMap<String, Integer> capacity,
                            PropertyStatus status,
                            PropertyType type) {
        if (address != null && !this.address.contains(address))
            return false;
        if (suburb != null && !suburb.toUpperCase().equals(this.suburb))
            return false;
        for (String k : capacity.keySet())
            if (capacity.get(k) != null && this.capacity.get(k) != capacity.get(k))
                return false;
        if (status != null && status != this.status)
            return false;
        if (type != null && type != this.type)
            return false;
        return true;
    }

    public boolean match(PropertyStatus status, Customer owner) {
        if (status != null && status != this.status)
            return false;
        if (owner != null && !owner.equals(this.owner))
            return false;
        return true;
    }

    public boolean match(PropertyStatus status, Employee employee) {
        if (status != null && status != this.status)
            return false;
        if (employee != null && !employee.equals(this.assignee))
            return false;
        return true;
    }

    public void addApplicationBase(ApplicationBase a) throws OperationNotAllowedException{
        if (this.getStatus() != PropertyStatus.ApplicationOpen)
            throw new OperationNotAllowedException(
                "Application for this property is currently closed."
            );
        this.applications.add(a);
    }

    public ArrayList<ApplicationBase> getPendingApplicationBases() {
        ArrayList<ApplicationBase> res = new ArrayList<ApplicationBase>();
        for (ApplicationBase a : this.applications)
            if (a.isPending())
                res.add(a);
        return res;
    }

    public ArrayList<ApplicationBase> getApplicationBasesInitiatedBy(Customer c) {
        ArrayList<ApplicationBase> res = new ArrayList<ApplicationBase>();
        for (ApplicationBase a : this.applications)
            if (a.initiatedBy(c))
                res.add(a);
        return res;
    }

    public void acceptApplicationBase(ApplicationBase a) throws OperationNotAllowedException {
        if (this.applications.contains(a) && a.isPending())  {
            a.setAccepted();
            setStatus(PropertyStatus.InspectionOpen);
            for (ApplicationBase oa : getPendingApplicationBases())
                oa.setRejected();
        } else
            throw new OperationNotAllowedException();
    }

    public void rejectApplicationBase(ApplicationBase a) throws OperationNotAllowedException {
        if (this.applications.contains(a) && a.isPending())  {
            a.setRejected();
        } else
            throw new OperationNotAllowedException();
    }

    public void withdrawApplicationBase(ApplicationBase a) throws OperationNotAllowedException {
        if (this.applications.contains(a) && a.isWithdrawable()) {
            a.setWithdrawn();
            if (a.isAccepted())
                setStatus(PropertyStatus.ApplicationOpen);
        } else
            throw new OperationNotAllowedException();
    }

    public void addInspection(Inspection i) throws OperationNotAllowedException {
        if (!isOpenForInspection())
            throw new OperationNotAllowedException(
                "Currently inspection is closed for this property."
            );
        this.inspections.add(i);
    }

    public ArrayList<Inspection> getUpcomingInspections() {
        ArrayList<Inspection> res = new ArrayList<Inspection>();
        for (Inspection i : this.inspections)
            if (i.isUpcoming())
                res.add(i);
        return res;
    }

    public void cancelAllInspections() {
        for (Inspection i : getUpcomingInspections())
            i.setCancelled();
    }
}

class PropertyUtil {
    public static void checkCapacity(HashMap<String, Integer> cap) throws InvalidParamException {
        if (!(cap.keySet().containsAll(Arrays.asList("bedroom", "bathroom", "car space"))))
            throw new InvalidParamException(
                "Capacity must contains the keys 'bedroom', 'bathroom' and 'car space'."
            );
        for (int a : cap.values())
            if (a < 0)
                throw new InvalidParamException(
                    "Numbers of bedrooms/bathrooms/car spaces must be zero or more."
                );
    }
}