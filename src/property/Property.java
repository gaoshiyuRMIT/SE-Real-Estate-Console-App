package property;

import java.util.*;

import se.*;
import consts.*;
import user.customer.*;
import user.employee.*;
import exception.*;

public abstract class Property{
    private static int idCounter = 0;

    private String id;
    private String address;
    private String suburb;
    private HashMap<String, Integer> capacity;
    private PropertyType type;
    private List<Inspection> inspections;
    private HashMap<String, String> documents;
    private PropertyStatus status;
    private Customer owner;
    private Employee assignee;
    private List<ApplicationBase> applications;
    private Branch branch;


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
        this.applications = new ArrayList<ApplicationBase>();
        this.inspections = new ArrayList<Inspection>();

    }

    public Property(String address, String suburb, HashMap<String, Integer> capacity,
                    String typeS, Customer owner) throws InvalidParamException {
        this(address, suburb, capacity, PropertyType.valueOf(typeS), owner);
    }

    public HashMap<String, Integer> getCapacity() {
        return capacity;
    }

    public String getTextualDetail() {
        String ret = String.format(
            "%-30s: %s\n"
                + "%-30s: %s\n"
                + "%-30s: %s\n"
                + "%-30s: %s\n"
                + "%-30s: %s\n"
                + "%-30s: %s",
            "property id", id,
            "address", address,
            "suburb", suburb,
            "type", type.name(),
            "status", status.name(),
            "owner", owner.getId()
        );
        ret += "\ncapacity:";
        for (String k : capacity.keySet()) {
            ret += String.format("\n  %-28s: %d", k, capacity.get(k));
        }
        return ret;
    }

    public List<ApplicationBase> getApplicationBases() {
        return applications;
    }

    public ApplicationBase getApplicationBaseById(String id) {
        for (ApplicationBase a : applications) {
            if (a.getId().equals(id))
                return a;
        }
        return null;
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

    public String getStatusS() {
        return getStatus().name();
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }


	public boolean match(String address, String suburb,
                            HashMap<String, Integer> capacity,
                            PropertyStatus status,
                            PropertyType type) {
        if (address != null && !address.equals("") && !this.address.contains(address))
            return false;
        if (suburb != null && !suburb.equals("") && !suburb.toUpperCase().equals(this.suburb))
            return false;
        if (capacity != null)
            for (String k : capacity.keySet()) {
                if (capacity.get(k) != null && !this.capacity.get(k).equals(capacity.get(k)))
                    return false;
            }
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

    public static String extractPropertyId(ApplicationBase a) {
        return a.getId().substring(0, 9);
    }

    public void addApplicationBase(ApplicationBase a) throws OperationNotAllowedException{
        if (this.getStatus() != PropertyStatus.ApplicationOpen)
            throw new OperationNotAllowedException(
                "Application for this property is currently closed."
            );
        this.applications.add(a);
        a.setProperty(this);
        a.addIdPrefix(getId() + "-");
    }

    public List<ApplicationBase> getPendingApplicationBases() {
        ArrayList<ApplicationBase> res = new ArrayList<ApplicationBase>();
        for (ApplicationBase a : this.applications)
            if (a.isPending())
                res.add(a);
        return res;
    }

    public List<ApplicationBase> getApplicationBasesInitiatedBy(Customer c) {
        ArrayList<ApplicationBase> res = new ArrayList<ApplicationBase>();
        for (ApplicationBase a : this.applications)
            if (a.initiatedBy(c))
                res.add(a);
        return res;
    }

    public void acceptApplicationBase(ApplicationBase a) throws OperationNotAllowedException {
        if (!this.applications.contains(a))
            throw new OperationNotAllowedException("The application specified is not pertinent to this property.");
        if (!a.isPending())
            throw new OperationNotAllowedException("The application is not pending.");
        a.setAccepted();
        setStatus(PropertyStatus.InspectionOpen);
        for (ApplicationBase oa : getPendingApplicationBases())
            oa.setRejected();
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

    public List<Inspection> getUpcomingInspections() {
        ArrayList<Inspection> res = new ArrayList<Inspection>();
        for (Inspection i : this.inspections)
            if (i.isUpcoming())
                res.add(i);
        return res;
    }

    public void cancelAllInspections() throws OperationNotAllowedException{
        Branch branch;
        branch = getBranch();
        for (Inspection i : getUpcomingInspections()) {
            i.setCancelled();
            branch.sendNotifForCancelledInspection(this, i);
        }
    }

    public void setBranch(Branch b) {
        branch = b;
    }

    public Branch getBranch(){
        return branch;
    }

    public void setAddress(String a) {
        address = a;
    }

    public void setPropertyType(String typeS) {
        type = PropertyType.valueOf(typeS);
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