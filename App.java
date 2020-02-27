package beleg;

import java.io.Console;
import java.sql.*;
import java.util.Scanner;

import beleg.Datenbank;

public class App {
	private Connection connection;
	private Scanner scanner;

	App(Scanner s, Connection c) {
		this.scanner = s;
		this.connection = c;
	}

	private void ausgabe() throws SQLException {
		Statement statement = connection.createStatement();
		String sql = "select * from Mitarbeiter";
		ResultSet result = statement.executeQuery(sql);
		while (result.next()) {
			System.out.print("ID: " + result.getInt(1) + " ");
			System.out.print("Name: " + result.getString("name") + " ");
			System.out.println("Titel: " + result.getString("titel") + " ");
			System.out.println("\n");
		}
	}

	private void eingabe() throws SQLException {

		String sql = "INSERT INTO Mitarbeiter(mitarbeiter_id, name, titel) VALUES (?, ?, ?)";
		PreparedStatement prestatement = connection.prepareStatement(sql);
		System.out.println("Mitarbeiter ID: ");
		scanner.nextLine();
		prestatement.setInt(1, scanner.nextInt());
		System.out.println("Name: ");
		scanner.nextLine();
		prestatement.setString(2, scanner.nextLine());
		System.out.println("Titel: ");
		prestatement.setString(3, scanner.nextLine());
		prestatement.executeUpdate();
		prestatement.close();
	}

	private void loeschen() throws SQLException {
		System.out.println("ID zu loeschen");
		int id = scanner.nextInt();
		String sql = "delete from Mitarbeiter where mitarbeiter_id = ?";
		PreparedStatement prestatement = connection.prepareStatement(sql);
		prestatement.setInt(1, id);
		prestatement.executeUpdate();
		prestatement.close();
	}

	private void printRow(ResultSet result) throws SQLException {
		StringBuilder b = new StringBuilder();
		String delimiter = "  ";
		int id = result.getInt(1);
		String name = result.getString(2);
		String titel = result.getString(3);
		b.append(id);
		b.append(delimiter);
		b.append(name);
		b.append(delimiter);
		b.append(titel);
		b.append(delimiter);
		System.out.println(b.toString());

	}

	private void navigieren() throws SQLException {
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		System.out.println(System.lineSeparator());

		String sql = "select * from Mitarbeiter";
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
					loeschen();
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
			System.out.println("Deine Anfrage konnte nicht bearbeitet werden.");
		}
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

//		
		String url = "jdbc:postgresql://db.f4.htw-berlin.de:5432/_s0561121__beleg";
		String user = "s0561121";
		Console console = System.console();
		String password =  new String(console.readPassword("Password: "));
		Connection connect = DriverManager.getConnection(url, user, password);
		Scanner s = new Scanner(System.in);
		App db = new App(s, connect);
		db.start();
		connect.close();
	}


}
