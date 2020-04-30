package Managers;

import Exceptions.EmptyFileException;
import ParserCSV.CSVFile;
import ParserCSV.CSVManager;
import ParserCSV.Delimiter;
import com.sun.org.apache.xpath.internal.operations.Or;
import object.Address;
import object.Collection;
import object.Organization;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager {
    private CSVFile csvFile;
    private LocalDate InitDate;
    private ArrayList<Organization> csvCollection = new ArrayList<>();
    private long maxId = 0L;
    /**
     * Класс, который работает с коллекцией
     * @param file
     * @throws IOException
     */
    public CollectionManager(String file){
        try {
            csvFile = new CSVFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file_ = new File(file);
        try{
            if(!file_.exists()) throw new FileNotFoundException();
        }
        catch(FileNotFoundException e){
            System.out.println("Файл не существует");
            System.exit(1);
        }
        try{
            if(!file_.canWrite() || !file_.canRead()) throw new SecurityException();
        }catch (SecurityException ex){
            System.out.println("Файл недоступен. Измените права доступа");
            System.exit(1);
        }


        file2Collection();
        InitDate = LocalDate.now();
    }

    /**
     * Добавляет новый элемент в коллекцию
     * @param org
     */
    public void add(Organization org,Long Id){
        while (true) {maxId+=1;
       if (!checkIdExist(maxId)) break;
       }
        org.CheckFields();
        if (Id == 0L){org.setId(maxId);} else {org.setId(Id);}
        org.setCreationDate(LocalDate.now());

        csvCollection.add(org);
    }
    public void add(Organization org){
        add(org,0L);
    }
    /**
     * Удаляет элемент по id
     * @param id
     * @return
     */
    public void removebyID(long id){
        Map.Entry<Integer,Organization> entry = extractById(id).entrySet().iterator().next();
        this.getCsvCollection().remove(entry.getValue());
    }
    public void clear(){
        csvCollection.clear();
    }


    /**
     * Находит элемент по id
     * @param id
     * @return
     */
    private Map<Integer, Organization> extractById(Long id){
        Map<Integer, Organization> map = new HashMap<>();
        for(int i=0;i<this.getCsvCollection().size();i++) {
            if(this.getCsvCollection().get(i).getId().equals(id)) {
                map.put(i, this.getCsvCollection().get(i));
                return map;
            }
        }

        return null;
    }

    private void file2Collection() {
        try {
            csvCollection = new CSVManager(csvFile).getOrganization();
            csvCollection.forEach(x-> {
                if(maxId < x.getId())
                    maxId = x.getId();
            });
        } catch (EmptyFileException e) {
            e.printStackTrace();
        }

        //тут после загрузки, отсортировать по ID
        this.sort();
    }

    private void sort(){
        Collections.sort(csvCollection, new Comparator<Organization>() {
            @Override
            public int compare(Organization o1, Organization o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
    }

    public String getCollectionType(){
        return csvCollection.getClass().getName();
    }
    public int size(){
        return csvCollection.size();
    }
    public LocalDate getInitDate(){
        return InitDate;
    }
    public ArrayList<Organization> getCsvCollection(){
        return csvCollection;
    }

    /**
     *
     * @param id
     * @return свободный id
     */
    public boolean checkIdExist(Long id){
        for(int i=0;i<this.getCsvCollection().size();i++) {
            if(this.getCsvCollection().get(i).getId().equals(id)) {
                return true;
            }
        }

        return false;
    }
    /**
     * сохраняет коллекцию в csv формате
     */
 public void save(){
        String header = "id,name,coordinates_x,coordinates_y,creationDate,annualTurnover,fullName,employeesCount,type,postalAddress_street,postalAddress_zipCode";

        String path = csvFile.getPath();
        String delimiter = Delimiter.getDelimiter(csvFile.getDelimiter());

        StringBuilder builder = new StringBuilder(header).append("\n");
        for (Organization organization : getCsvCollection()) {
            builder.append(organization.getId()).append(delimiter);
            builder.append(organization.getName()).append(delimiter);
            builder.append(organization.getCoordinates().getX()).append(delimiter);
            builder.append(organization.getCoordinates().getY()).append(delimiter);
            builder.append(organization.getCreationDate()).append(delimiter);
            builder.append(organization.getAnnualTurnover()).append(delimiter);
            builder.append(organization.getFullName()).append(delimiter);
            if (organization.getEmployeesCount() == null) builder.append(delimiter);
            else builder.append(organization.getEmployeesCount()).append(delimiter);
            builder.append(organization.getType().getId()).append(delimiter);
            if (organization.getPostalAddress().getStreet() == null) builder.append(delimiter);
            else builder.append(organization.getPostalAddress().getStreet()).append(delimiter);
            if (organization.getPostalAddress().getZipCode() == null) builder.append(delimiter);
            else builder.append(organization.getPostalAddress().getZipCode()).append(delimiter);
            builder.append("\n");
        }

        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
            stream.write(builder.toString().getBytes());
            stream.close();
            System.out.println("Collection successfully saved to the file.");
        } catch (IOException e) {
            System.out.println("Here a problem with saving collection to the file!");
        }
    }
    /**
     * обновляет коллекцию по его id
     * @param organization
     * @param id
     */
    public void update(Organization organization, Long id){
        Map.Entry<Integer,Organization> entry = extractById(id).entrySet().iterator().next();
        Organization updOrganization = entry.getValue();
        updOrganization.setName(organization.getName());
        updOrganization.setCoordinates(organization.getCoordinates());
        updOrganization.setAnnualTurnover(organization.getAnnualTurnover());
        updOrganization.setFullName(organization.getFullName());
        updOrganization.setEmployeesCount(organization.getEmployeesCount());
        updOrganization.setType(organization.getType());

        updOrganization.setPostalAddress(organization.getPostalAddress());


        this.getCsvCollection().set(entry.getKey(), updOrganization);
    }

    /**
     * удаляет элементы, меньше заданного
     * @param organization
     */
    public void removeLower(Organization organization) {
        List<Organization> organizations = this.getCsvCollection().stream().sorted().collect(Collectors.toList());
        organizations.forEach(x -> {
            if (x.compareTo(organization) < 0) {
                this.getCsvCollection().remove(x);
            }
        });
    }
    /**
     * удаляет элементы, больше заданного
     * @param organization
     */
    public void removeGreater(Organization organization) {
        List<Organization> organizations = this.getCsvCollection().stream().sorted().collect(Collectors.toList());
        organizations.forEach(x -> {
            if (x.compareTo(organization) > 0) {
                this.getCsvCollection().remove(x);
            }
        });
    }
    /**
     * удаляет элементы по значению адреса
     * @param address
     */
    public void remove_by_postal_address(Address address){
        getCsvCollection().removeIf(x->x.getPostalAddress().compareTo(address) == 0);
    }
    /**
     * сортирует элементы коллекции по значению annual turnover в порядке возрастания
     */
    public ArrayList<Organization> sortByAnnualTurnover(){
        ArrayList<Organization> sortColl = new ArrayList<Organization>(this.getCsvCollection());
        sortColl.sort(Comparator.comparing(e -> e.getAnnualTurnover()));

        return sortColl;
    }
}
