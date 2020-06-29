package ru.ifmo.lab.managers;
import ru.ifmo.lab.database.Credentials;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.lab.object.Address;
import ru.ifmo.lab.object.Coordinates;
import ru.ifmo.lab.object.Organization;
import ru.ifmo.lab.object.OrganizationType;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;
@Slf4j
public class ConsoleManager {
    private Scanner scanner;
    private boolean isScript;
    private Writer writer;
    private Reader reader;


    public void print(String message){
        try {
        writer.write(message+"\n");
        writer.flush();
    } catch (IOException e) {
        log.error("Ошибка при выводе. {}", e.getMessage());
    }}

    public ConsoleManager(Reader reader, Writer writer, boolean isScript)
    {
        this.reader = reader;
        this.writer = writer;
        scanner = new Scanner(reader);
        this.isScript = isScript;
    }
    public Boolean getIsScript(){return isScript;}

    /**
     * выводит сообщение с вводом от пользователя
     * @param message
     * @param canNull
     * @return
     */
        public String readmessage(String message,boolean canNull){
        String out = "";
        do  {
                print(message);
                out = scanner.nextLine();
        }while (out.equals("") && !canNull);

        return out;
    }

    public String read(){
        return scanner.nextLine();
    }

    public boolean hasNextLine(){
        return scanner.hasNextLine();
    }
    /**
     * получает данные элемента коллекции
     * @return
     */
    public Organization getOrganization(){
        String name = readmessage("Input name: ", false);
        Coordinates coordinates = getCoordinates();
        int annualTurnover = (int) readmessageTryMinMax("Input annual turnover(натуральное число)",new BigDecimal(0),new BigDecimal(Integer.MAX_VALUE),false).intValue();
        String fullName = readmessage("Input full name",false);
        Long employeesCount = readmessageTryMinMax("Input employees count(целое число работников)",new BigDecimal(0),new BigDecimal(Long.MAX_VALUE),false).longValue();
        OrganizationType organizationType = getOrganizationType();
        Address postalAddress = getpostalAddress();

        return new Organization(-1L,name,coordinates.getX(),coordinates.getY(), LocalDate.now(),annualTurnover,fullName,employeesCount,organizationType,postalAddress.getStreet(),postalAddress.getZipCode(),-1,"");
    }
    /**
     * получает координаты
     * @return
     */
    public Coordinates getCoordinates(){

            long x = (long) readmessageTryMinMax("Input x-coordinate(целое число, большее -746 и меньшее  9223372036854775807)",new BigDecimal(-745),new BigDecimal(Long.MAX_VALUE),false);
            double y =  readmessageTryMinMax("Input y-coordinate(натуральное число)", new BigDecimal(Double.MIN_VALUE),new BigDecimal(Double.MAX_VALUE), false).doubleValue();

            return new Coordinates(x,y);
    }
    /**
     * получает тип организации
     * @return
     */
    public OrganizationType getOrganizationType(){
        String out = "";
        for(OrganizationType val : OrganizationType.values()){
            out += "\n" + val.ordinal() + " - " + val.toString();
        }

        String id = readmessageTryMinMax("Chooze organization type(введите соответствующую цифру)" + out, new BigDecimal(0),new BigDecimal(3),false).toString();
        if(id == null){
            return null;
        }

        return OrganizationType.getById(Integer.parseInt(id));
    }
    /**
     * получает адрес
     * @return
     */
    public Address getpostalAddress(){
       String street = readmessage("Input street:",true);
        String zipCode = readmessage("Input zipCode:",true);
        return new Address(street, zipCode);
    }


    private final Pattern NUMBER_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

    boolean isNumber(String s){
        return NUMBER_PATTERN.matcher(s).matches();
    }

    public static boolean isInRange2(BigDecimal number, BigDecimal min,
                                     BigDecimal max) {
        try {
         return max.compareTo(number) >= 0 && min.compareTo(number) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * выводит сообщение с вводом от пользователя, с проверкой на минимальное и макскимальное значение
     * @param message
     * @param min
     * @param max
     * @param canNull
     * @return
     */
    public String readWithMessageMinMax(String message, BigDecimal min, BigDecimal max, boolean canNull){
        String output = "";

        do {
            output = readmessage(message, canNull);
           if(Float.parseFloat(min.toString())> Float.parseFloat(output)){
                print("Некорректный ввод, соблюдайте указанные требования.\nПовторите ввод");
            }
            if(output == null && canNull || !isNumber(output)){

                       break;}


        }while (!isInRange2(new BigDecimal(output), min, max));

        return output;
    }
    /**
     * Проверка на соответствие вводимых данных требованиям
     * @param message
     * @param min
     * @param max
     * @param canNull
     * @return
     */
    private Number readmessageTryMinMax(String message, BigDecimal min, BigDecimal max, boolean canNull){
        Number output = null;
        while(true) {
            try {
                String num = readWithMessageMinMax(message,min,max, canNull);
                NumberFormat format = NumberFormat.getInstance();
                ParsePosition pos = new ParsePosition(0);
                output = format.parse(num, pos);
                if(canNull && output==null){ break;}
                if (pos.getIndex() != num.length() || pos.getErrorIndex() != -1) {

                    throw new Exception();
                }


                break;

            } catch (Exception ex) {

                print("Некорректный ввод, соблюдайте указанные требования.\nПовторите ввод");
            }

        }
        return output;
    }
    public Credentials getCredentials(){
        String username = readmessage("Login: ", false);
        String password = readmessage("Password: ", false);

        return new Credentials(-1, username, password);
    }

}


