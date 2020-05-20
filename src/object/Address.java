package object;

import java.io.Serializable;

public class Address implements Comparable<Address>, Serializable {
    private String street; //Строка не может быть пустой, Поле не может быть null
    private String zipCode; //Длина строки должна быть не меньше 8, Поле может быть null



    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getZipCode() {
        return zipCode;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getStreet() {
        return street;
    }
    public Address(String street, String zipCode) {
        this.street = street;
        this.zipCode = zipCode;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\t").append("address(street): ").append(this.street).append("\n");
        sb.append("\t").append("address(zipCode): ").append(this.zipCode).append("\n");
        return sb.toString();
    }
    public Address(){};
    @Override
    public int compareTo(Address o) {
        if(o == null||getStreet()==null)
            return -1;

        if(!(o instanceof Address))
            throw new ClassCastException();

        Address oo = (Address)o;
        if(this.getStreet().equals(oo.getStreet())){
            return 0;
        }else{
            return -1;
        }
    }


}
