package object;

import exceptions.InvalidValueException;

import java.io.Serializable;

public enum OrganizationType implements Serializable {
    PUBLIC(0),
    TRUST(1),
    PRIVATE_LIMITED_COMPANY(2),
    OPEN_JOINT_STOCK_COMPANY(3);

    private int Id;
    OrganizationType(int id){
        this.Id = id;
    }

    public static OrganizationType getById(int id) {


              for (OrganizationType val : OrganizationType.values()) {
                  if (val.getId() == id) {
                      return val;
                  }
              }

          throw new InvalidValueException("Не найдено");
      }


    public static OrganizationType byName(String s) {
        for (OrganizationType value : OrganizationType.values()) {
            if (value.equals(s.toUpperCase().trim())) {
                return value;
            }
        }
        throw new InvalidValueException("Не найдено, соответствующий строке: " + s);
    }

    public int getId() {
        return Id;
    }
}
