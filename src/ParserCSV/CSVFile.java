package ParserCSV;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFile {

    protected String[] header;
    protected List<String[]> body;

    private final String path;
    private final Delimiter delimiter;

    public CSVFile(String path) throws IOException {

        this.path = path;
        this.delimiter = Delimiter.COMMA;
        this.setFields(path, Delimiter.COMMA);
    }

    public CSVFile(String path, Delimiter delimiter) throws IOException {

        this.path = path;
        this.delimiter = delimiter;
        this.setFields(path, delimiter);
    }

    private void setFields(String path, Delimiter delimiter) throws IOException {

        List<String> file = Reader.readAsList(path);

        String[] line = null;
        List<String[]> lines = new ArrayList<>();

        if (file.size() != 0) {
            line = cutLine(file.get(0), delimiter);
                           file.remove(0);
        }

        for (String s : file) lines.add(cutLine(s, delimiter));

        this.header = line;
        this.body = lines;

    }


    private String[] cutLine(String line, Delimiter delimiter) {

        String[] columns = line.split(Delimiter.getDelimiter(delimiter));

        for (int i = 0; i < columns.length; i ++)
            columns[i] = columns[i].trim();

        return columns;
    }

    public String getPath() { return path; }

    public Delimiter getDelimiter() { return delimiter; }


    static class Reader {

        private static List<String> readAsList(String path) throws IOException {

            List<String> list = new ArrayList<>();

            Scanner stream = new Scanner(Paths.get(path));

            String line;
            while (stream.hasNextLine()) {

                if(!(line = stream.nextLine().trim()).equals(""))
                    list.add(line);
            }

            return list;
        }
    }
}