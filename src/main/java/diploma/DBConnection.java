package diploma;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    private final static String TABLE_NAME = "definitions";

    private final static String COLUMN_DEFINITION = "definition";
    private final static String COLUMN_EXPLANATION = "explanation";

    private static String url = "jdbc:sqlite::resource:diploma.db";
//    private static String url = "jdbc:sqlite:diploma.db";

    public static void insert() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS " + TABLE_NAME);
            statement.execute("CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_DEFINITION + " varchar(255) PRIMARY KEY," +
                    COLUMN_EXPLANATION + " varchar(1023));");

            BufferedReader br = new BufferedReader(new FileReader("insertfile.txt"));

            String line = br.readLine();
            while (line != null && !line.isEmpty()) {
//                Statement insertStatement = connection.createStatement();
                statement.execute(line);
                line = br.readLine();
            }
            br.close();

            System.out.println("Connection to SQLite has been established.");

            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<DefinitionEntry> select() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);

            List<DefinitionEntry> results = new ArrayList<>();
            while (resultSet.next()) {
                String def = resultSet.getString(COLUMN_DEFINITION);
                results.add(new DefinitionEntry(def, resultSet.getString(COLUMN_EXPLANATION)));
            }

            connection.close();
            return results;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
