package object;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.io.Serializable;
import java.time.LocalDate;

public class Organization implements Comparable, Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int annualTurnover; //Значение поля должно быть больше 0
    private String fullName; //Длина строки не должна быть больше 1539, Поле не может быть null
    private Long employeesCount; //Поле может быть null, Значение поля должно быть больше 0
    private OrganizationType type; //Поле не может быть null
    private Address postalAddress; //Поле может быть null

    public Organization(){}

    public Organization(final long id, final String name, final Long x, final double y, final int annualTurnover,final String fullName, final Long employeesCount,final OrganizationType type,final String street,final String zipCode){
        this.name = name;
        this.coordinates = new Coordinates(x,y);
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.employeesCount = employeesCount;
        this.type = type;
        this.postalAddress = new Address(street, zipCode);
    }
    public Organization(String name, Coordinates coordinates, int annualTurnover, String fullName, Long employeesCount, OrganizationType type, Address postalAddress){
        this.name = name;
        this.coordinates = coordinates;
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.employeesCount = employeesCount;
        this.type = type;
        this.postalAddress = postalAddress;
    }

    public void CheckFields(){

    }

    public Long getId() {
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(int annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(Long employeesCount) {
        this.employeesCount = employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public Address getPostalAddress() { return postalAddress; }

    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
    }


    public int compareTo(Object o){
        if(o == null)
            return -1;

        if(!(o instanceof Organization))
            throw new ClassCastException();

        Organization oo = (Organization)o;
        if(this.getEmployeesCount().equals(oo.getEmployeesCount())){
            return 0;
        }else{
            return this.getEmployeesCount() < oo.getEmployeesCount()? -1 : 1;
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\n");
        sb.append("\t").append("id: ").append(this.id).append("\n");
        sb.append("\t").append("name: ").append(this.name).append("\n");
        sb.append("\t").append("coordinates: ").append(this.coordinates).append("\n");
        sb.append("\t").append("СreationDate: ").append(this.creationDate).append("\n");
        sb.append("\t").append("annualTurnover: ").append(this.annualTurnover).append("\n");
        sb.append("\t").append("fullName: ").append(this.fullName).append("\n");
        sb.append("\t").append("employeesCount: ").append(this.employeesCount).append("\n");
        sb.append("\t").append("type: ").append(this.type).append("\n");
        sb.append("\t").append("postalAddress: ").append(this.postalAddress).append("\n");
        sb.append("}").append("\n");
        return sb.toString();
    }


}
