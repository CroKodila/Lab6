package ru.ifmo.lab.parserCSV;

public enum Delimiter {

    COMMA, PIPE, COLON, SEMICOLON, SPACE;

    public static String getDelimiter(Delimiter delimiter) {

        switch (delimiter) {

            case COMMA:
                return ",";
            case PIPE:
                return "|";
            case COLON:
                return ":";
            case SEMICOLON:
                return ";";
            case SPACE:
                return " ";
            default:
                return ",";
        }
    }
}