import java.sql.*;

public class DBStudentskaBaza {
	String connectionString;
	Connection connection;

	public DBStudentskaBaza(String conStr) {
		connectionString = conStr;
	}

	public void connect() {
		try {
			connection = DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dodajStudenta(int indeks, String ime, String prezime, String datumUpisa, String datumRodjenja,
			String mestoRodjenja) {
		try {
			String upit = "INSERT INTO Dosije (indeks, ime, prezime, datum_upisa, datum_rodjenja, mesto_rodjenja)\n"
					+ " VALUES (" + indeks + ", '" + ime + "', '" + prezime + "', '" + datumUpisa + "', '"
					+ datumRodjenja + "', '" + mestoRodjenja + "')";
			Statement stm = connection.createStatement();
			stm.executeUpdate(upit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//  1. Izdvojiti podatke o svim predmetima.
	public void podaciSvihPredmeta() {
		String upit = "SELECT * FROM predmet";
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);

			while (rs.next()) {
				int id_predmeta = rs.getInt(1);
				String sifra = rs.getString("sifra");
				String naziv = rs.getString("naziv");
				int bodovi = rs.getInt(4);

				System.out.println(id_predmeta + "\t" + sifra + "\t" + naziv + "\t" + bodovi);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

// 2. Izdvojiti podatke o svim studentima rodjenim u Beogradu.
	public void sviIzBeograda() {
		String upit = "SELECT * FROM dosije WHERE dosije.mesto_rodjenja = \"Beograd\"";

		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);

			while (rs.next()) {
				int indeks = rs.getInt(1);
				String ime = rs.getString("ime");
				String prezime = rs.getString("prezime");
				String datum_upisa = rs.getString("datum_upisa");
				String datum_rodjenja = rs.getString("datum_rodjenja");
				String mesto_rodjenja = rs.getString("mesto_rodjenja");

				System.out.println(indeks + "\t" + ime + "\t" + prezime + "\t" + datum_upisa + "\t" + datum_rodjenja
						+ "\t" + mesto_rodjenja);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//3. Izdvojiti mesta u kojima su rodjeni studenti.	
	public void mestoRodjenja() {
		String upit = "SELECT mesto_rodjenja FROM dosije";
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);

			while (rs.next()) {
				String mesto_rodjenja = rs.getString("mesto_rodjenja");
				if (mesto_rodjenja != null) {
					System.out.println(mesto_rodjenja);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

// 4. Izdvojiti nazive predmeta koji imaju vise od 6 bodova.
	public void viseOdSest() {
		String upit = "SELECT naziv FROM predmet WHERE predmet.bodovi >= 6";

		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);

			while (rs.next()) {
				String naziv = rs.getString("naziv");

				System.out.println(naziv);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

// 5. Izdvojiti sifre i nazive predmeta koji imaju izmedju 8 i 15 bodova.	
	public void predmetiIzmedju8I5() {
		String upit = "SELECT sifra, naziv FROM predmet WHERE bodovi BETWEEN 8 AND 15";

		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);

			while (rs.next()) {
				String sifra = rs.getString("sifra");
				String naziv = rs.getString("naziv");

				System.out.println(sifra + "\t" + naziv);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

// 6. Izdvojiti podatke o ispitima na kojima student ima 81, 76 ili 59 bodova.
	public void studentIma81_76ili59() {
		String upit = "SELECT * FROM ispit WHERE ispit.bodovi = 81 OR ispit.bodovi = 76 OR ispit.bodovi = 59";

		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);

			while (rs.next()) {
				int indeks = rs.getInt(1);
				int id_predmeta = rs.getInt(2);
				int godina_roka = rs.getInt(3);
				String oznaka_roka = rs.getString("oznaka_roka");
				int ocena = rs.getInt(5);
				String datum = rs.getString("datum_ispita");
				int bodovi = rs.getInt("bodovi");

				System.out.println(indeks + "\t" + id_predmeta + "\t" + godina_roka + "\t" + oznaka_roka + "\t" + ocena + "\t" + datum + "\t" + bodovi);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

// 7. Izdvojiti podatke o ispitima na kojima student nema 81, 76 ili 59 bodova.
	public void studentNema81_76I59() {
		String upit = "SELECT * FROM ispit WHERE ispit.bodovi != 81 AND ispit.bodovi != 76 AND ispit.bodovi != 59";

		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);

			while (rs.next()) {
				int indeks = rs.getInt(1);
				int id_predmeta = rs.getInt(2);
				int godina_roka = rs.getInt(3);
				String oznaka_roka = rs.getString("oznaka_roka");
				int ocena = rs.getInt(5);
				String datum = rs.getString("datum_ispita");
				int bodovi = rs.getInt("bodovi");

				System.out.println(indeks + "\t" + id_predmeta + "\t" + godina_roka + "\t" + oznaka_roka + "\t" + ocena + "\t" + datum + "\t" + bodovi);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// 8. Izdvojiti nazive predmeta i njihovu cenu za samofinansirajuce studente izrazenu u dinarima. Jedan bod košta 1500 dinara.
	public void cenaPredmeta() {
		String upit = "SELECT predmet.naziv, bodovi*1500 AS cena FROM predmet";
		
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);
			
			while(rs.next()) {
				String naziv = rs.getString("naziv");
				int cena = rs.getInt("cena");
				
				System.out.println(naziv+"\t"+cena);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void izbrisatiNevalidnu() {
		String upit = "DELETE FROM ispit WHERE ocena NOT BETWEEN 5 AND 10";
		
		try {
			Statement stm = connection.createStatement();
			stm.executeUpdate(upit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void setDatumRodjenja(int indeks, String datumRodjenja) {
		String upit = "UPDATE dosije SET datum_rodjenja = '"+datumRodjenja+"' WHERE indeks = "+indeks;
		
		try {
			Statement stm = connection.createStatement();
			stm.executeUpdate(upit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
