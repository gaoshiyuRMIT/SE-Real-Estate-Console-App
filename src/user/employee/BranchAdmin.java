package user.employee;

public class BranchAdmin extends Employee {
    public void collectAdvancedRentBond(RentalProperty property);

    /*
    :param uri: file location in the file system / cloud drive
    */
    public void scanDocument(String uri, Property property, String name) {
        property.addDocument(name, uri);
    }
}