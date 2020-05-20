package parserCSV;

import exceptions.EmptyFileException;
import object.Address;
import object.Coordinates;
import object.Organization;
import object.OrganizationType;


import java.time.LocalDate;
import java.util.ArrayList;

public class CSVManager {

    private CSVFile file;
    
    public CSVManager(CSVFile file) { this.file = file; }

    public ArrayList<Organization> getOrganization() throws EmptyFileException {

       ArrayList<Organization> organization = new ArrayList<>();

        if (file.header == null) throw new EmptyFileException();
        if (file.body == null) return organization;

        for (String[] body : file.body)
            organization.add(this.getObject(file.header, body));

        return organization;
    }

    private Organization getObject(String[] header, String[] body) {

        Organization organization = new Organization();
        Coordinates coordinates = new Coordinates();
        Address postalAddress = new Address();

        for (int i = 0, j = 0; i < header.length && j < body.length; i ++, j ++) {

            if (body[j].equals("")) continue;

            switch (header[i]) {

                case "id":                        organization.setId(Long.parseLong(body[j]));
                    break;
                case "name":                      organization.setName(String.valueOf(body[j]));
                    break;
                case "coordinates_x":             coordinates.setX(Long.parseLong(body[j]));
                    break;
                case "coordinates_y":             coordinates.setY(Double.parseDouble(body[j]));
                    break;
                case "creationDate":              organization.setCreationDate(LocalDate.parse(body[j]));
                    break;
                case "annualTurnover":              organization.setAnnualTurnover(Integer.parseInt(body[j]));
                    break;
                case "fullName":                    organization.setFullName(String.valueOf(body[j]));
                    break;
                case "employeesCount":              organization.setEmployeesCount(Long.parseLong(body[j]));
                    break;
                case "type":                        organization.setType(OrganizationType.getById(Integer.parseInt(body[j])));
                    break;
                case "postalAddress_street":           postalAddress.setStreet(String.valueOf(body[j]));
                    break;
                case "postalAddress_zipCode":  postalAddress.setZipCode(String.valueOf(body[j]));
                    break;
            }
        }

        organization.setCoordinates(coordinates);
        organization.setPostalAddress(postalAddress);

        return organization;
    }

    public CSVFile getFile() { return file; }
}
