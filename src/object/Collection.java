package object;

import object.Organization;

import java.util.ArrayList;

public class Collection  {
    private ArrayList<Organization> collection = new ArrayList<>();

    public String getTypeColl(){return collection.getClass().getTypeName();}

    public ArrayList<Organization> getOrganizations(){
        return  collection;
    }

    public  void setOrganizations(ArrayList<Organization> collection){
        this.collection = collection;
    }


}
