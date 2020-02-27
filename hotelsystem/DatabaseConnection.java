/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconnection;

import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author sami
 */
public class DatabaseConnection {

    private final String url = "jdbc:postgresql://localhost:5432/HotelSystem";
    private final String user = "sami";
    private final String password = "369752";
    Connection conn = null;

    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        DatabaseConnection app = new DatabaseConnection();
        app.connect();

        app.start();

    }

    public Connection connect() {

        try {
            conn = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void ausgabe() throws SQLException {
        Statement statement = conn.createStatement();
        String sql = "select * from gast";
        ResultSet result = statement.executeQuery(sql);
        
        while (result.next()) {
            System.out.print("id: " + result.getInt(1) + " ");
            System.out.print(",name: " + result.getString("name") + " ");
            System.out.print(",vorname: " + result.getString("vorname") + " ");
            System.out.print(",adresse: " + result.getString("adresse") + " ");
            System.out.print(",mobil: " + result.getString("mobil") + " ");
            System.out.println("\n");
        }

    }

    public void eingabe() throws SQLException {
        String sql = "INSERT INTO gast (id,name,vorname,adresse,mobil) VALUES (?,?,?,?,?) ";
        PreparedStatement prestatement = conn.prepareStatement(sql);
        System.out.println("ID:");
        scanner.nextLine();
        prestatement.setInt(1, scanner.nextInt());
        System.out.println("Name:");
        scanner.nextLine();
        prestatement.setString(2, scanner.nextLine());
        System.out.println("Vorname:");
        //scanner.nextLine();
        prestatement.setString(3, scanner.nextLine());
        System.out.println("Adresse:");
       // scanner.nextLine();
        prestatement.setString(4, scanner.nextLine());
        System.out.println("mobil:");
        prestatement.setString(5, scanner.nextLine());
        prestatement.executeUpdate();
        prestatement.close();

    }

    public void löschen() throws SQLException {
        System.out.println("löschen Sie den Gast mit ID : ");
        int id = scanner.nextInt();
        String sql = "DELETE from gast where id =?";
        PreparedStatement prestatement = conn.prepareStatement(sql);
        prestatement.setInt(1, id);
        prestatement.executeUpdate();
        prestatement.close();
    }

    private void printRow(ResultSet result) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String delimiter = "  ";
        int id = result.getInt(1);
        String name = result.getString(2);
        String vorname = result.getString(3);
        String adresse = result.getString(4);
        String mobil=result.getString(5);
        sb.append(id);
        sb.append(delimiter);
        sb.append(name);
        sb.append(delimiter);
        sb.append(vorname);
        sb.append(delimiter);
        sb.append(adresse);
        sb.append(delimiter);
        sb.append(mobil);
        sb.append(delimiter);
        System.out.println(sb.toString());

    }

    private void navigieren() throws SQLException {
        Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println(System.lineSeparator());

        String sql = "select * from gast";
        ResultSet result = statement.executeQuery(sql);
        boolean terminate = false;
        System.out.println("n Next, p previous, q for exit into menu");
        while (!terminate) {
            String input = scanner.nextLine();
            switch (input) {
                case "n":
                    if (result.next()) {
                        printRow(result);
                    } else {
                        result.first();
                        printRow(result);

                    }
                    break;
                case "p":
                    if (result.previous()) {
                        printRow(result);
                    } else {
                        result.last();
                        printRow(result);
                    }
                    break;
                case "q":
                    terminate = true;
                    break;
            }
        }

        statement.close();
    }

    public void instructions() {
        System.out.println(System.lineSeparator());
        System.out.println("1 Ausgabe der Tabelle");
        System.out.println("2 Eingabe Datensätze");
        System.out.println("3 Loschen von Datensatz");
        System.out.println("4 Navigieren");
        System.out.println("5 Beenden");
    }

    public void start() throws SQLException {
        boolean beenden = false;
        boolean again = true;
        try {
            while (!beenden) {
                instructions();
                int input = scanner.nextInt();
                switch (input) {
                    case 1:
                        ausgabe();

                        break;
                    case 2:
                        eingabe();
                        break;
                    case 3:
                        löschen();
                        break;
                    case 4:
                        navigieren();
                        break;
                    case 5:
                        beenden = true;
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Die Anfrage ist nicht zu bearbeiten--!");
        }
    }

}
