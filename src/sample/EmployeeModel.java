package sample;

public class EmployeeModel {

    String nameSurname,address,position,date;
    int permissionDay,userID,placesID,id;
    double salary;


    public EmployeeModel(String nameSurname, String address, String position, String date, int permissionDay, int userID, int placesID, int id, double salary) {
        this.nameSurname = nameSurname;
        this.address = address;
        this.position = position;
        this.date = date;
        this.permissionDay = permissionDay;
        this.userID = userID;
        this.placesID = placesID;
        this.id = id;
        this.salary = salary;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPermissionDay() {
        return permissionDay;
    }

    public void setPermissionDay(int permissionDay) {
        this.permissionDay = permissionDay;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPlacesID() {
        return placesID;
    }

    public void setPlacesID(int placesID) {
        this.placesID = placesID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
