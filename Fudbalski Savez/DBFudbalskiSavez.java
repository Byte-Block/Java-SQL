package paketDomaci24;

import java.sql.*;

public class DBFudbalskiSavez {
	String connectionString;
	Connection connection;

	public DBFudbalskiSavez(String connectionString) {
		this.connectionString = connectionString;
	}

	public void connect() {
		try {
			connection = DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dissconect() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	-metodu koja ispisuje imena svih fudbalera u bazi i timove za koje igraju
//	ime fudbalera - ime tima
	public void imenaSvihFudbalera() {
		String upit = "SELECT Fudbaler.Ime, Tim.Naziv FROM Fudbaler, Tim WHERE Fudbaler.IdTim = Tim.IdTim";
		
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);
			
			while(rs.next()) {
				String ime = rs.getString("Ime");
				String naziv = rs.getString("Naziv");
				
				System.out.println("\t"+ime+"\t\t"+naziv);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
// -metodu koja ispisuje sve fudbalere Manchester United-a	
	public void imenaFudbaleraManchesterUniteda(){
		String upit = "SELECT Fudbaler.Ime FROM Fudbaler, Tim WHERE Fudbaler.IdTim=Tim.IdTim AND Tim.Naziv = \"Manchester United\"";
		
		try {
			
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);
		
			while(rs.next()) {
				String ime = rs.getString("Ime");
				
				System.out.println("\t"+ime);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// -metodu koja za svakog fudbalera ispisuje koliko golova je postigao
	public void goloviPoFudbaleru() {
		String upit = "SELECT Fudbaler.Ime, COUNT(Gol.RedniBrGola)\n" + 
					  "FROM Fudbaler, Gol\n" + 
				      "WHERE Fudbaler.IdFud = Gol.IdFud\n" + 
				      "GROUP BY Fudbaler.Ime\n" + 
				      "ORDER BY Gol.IdGol";
		
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);
			
			while(rs.next()) {
				String ime = rs.getString("Ime");
				int gol = rs.getInt(2);
				
				System.out.println("\t"+ime+"\t"+gol);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// -metodu koja ispisuje ime fudbalera koji je postigao najvise golova
	public void fudbalerSaNajviseGolova() {
		String upit = "SELECT Fudbaler.Ime, COUNT(Gol.IdGol)\n" + 
				"FROM Fudbaler, Gol\n" + 
				"WHERE Fudbaler.IdFud = Gol.IdFud \n" + 
				"GROUP BY Fudbaler.Ime\n" + 
				"HAVING COUNT(Gol.IdGol) = (SELECT MAX(maxGolova) FROM (SELECT COUNT(Gol.RedniBrGola) AS maxGolova FROM Fudbaler, Gol WHERE Fudbaler.IdFud = Gol.IdFud GROUP BY Fudbaler.Ime))";
		
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);
					
					while(rs.next()) {
						System.out.println(rs.getString("Ime")+"\t"+rs.getInt(2));
					}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// --metodu koja ispisuje koliko crvenih i koliko zutih kartona je svaki igrac dobio
//	ime fudbalera - broj crvenih - broj zutih	
	public void brojKartona() {
		String upit = "SELECT Fudbaler.Ime, COUNT(CASE WHEN Karton.Tip = \"zuti karton\" THEN 1 ELSE NULL END) AS Zuti_Karton,"
				+ " COUNT(CASE WHEN Karton.Tip = \"crveni karton\" THEN 1 ELSE NULL END) AS Crveni_Karton\n" + 
				"FROM Fudbaler, Karton\n" + 
				"WHERE Fudbaler.IdFud = Karton.IdFud\n" + 
				"GROUP BY Fudbaler.Ime";
		
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(upit);
			
			while(rs.next()) {
				System.out.println(rs.getString("Ime")+"\t"+rs.getInt(2)+"\t"+rs.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
//	-metodu koja ispisuje imena gradova u kojima je fudbaler igrao
//	(parametar metode je id fudbalera, fudbaler je igrao u gradovima gde je njegov tim igrao kao gostujuci + u gradu u kom igra njegov tim)
	public void gradoviGdeJeFudbalerIgrao(int id) {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT Tim.mesto AS \"Gradovi u kojim je igrao\""
					+ " FROM  Fudbaler JOIN Tim  using(IdTim) JOIN Utakmica on (IdTim = IdDomacin)"
					+ " where IdFud = ?"
					+ " UNION"
					+ " SELECT Tim.Mesto"
					+ " From Tim"
					+ " WHERE IdTim in(SELECT IdDomacin"
					+ " FROM  Fudbaler JOIN Tim  using(IdTim) JOIN Utakmica on (IdGost = IdTim)"
					+ " where IdFud = ?)");
			ps.setInt(1, id);
			ps.setInt(2, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println("Gradovi u kojim je igrao: " + rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	-metodu koja ubacuje fudbalera u bazu(parametri metode su ime fudbalera i ime tima)
	public void dodajFudbalera(String ime, String tim) {
		PreparedStatement max;
		try {
			max = connection.prepareStatement("SELECT MAX(IdFud)" + " FROM Fudbaler");
			ResultSet rsmax = max.executeQuery();
			int idFudb = (rsmax.getInt(1) + 1);
			PreparedStatement ps = connection.prepareStatement("INSERT INTO Fudbaler(IdFud, Ime, IdTim)"
					+ " VALUES(?, ?, ( SELECT IdTim from Tim" + " WHERE Tim.Naziv = ?))");
			ps.setInt(1, idFudb);
			ps.setString(2, ime);
			ps.setString(3, tim);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
//	-metodu koja ubacuje tim u bazu(parametri metode su ime tima i mesto)
	public void dodajTim(String ime, String mesto) {
		try {
			PreparedStatement maxps = connection.prepareStatement("SELECT MAX(IdTim)" + " FROM Tim");
			ResultSet maxRs = maxps.executeQuery();
			int idTim = maxRs.getInt(1) + 1;
			PreparedStatement ps = connection.prepareStatement("INSERT INTO TIM(IdTim, Naziv, Mesto)" + " VALUES(?,?,?)");
			ps.setInt(1, idTim);
			ps.setString(2, ime);
			ps.setString(3, mesto);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
//  -metodu koja ubacuje utakmicu u bazu
//	(metoda prima ime gostujuceg tima, ime domaceg tima, kolo, ishod, godinu), 
//	potrebno je izvrsiti i ubacivanja u Tabelu igrao za sva igrace koji su igrali na utakmici(poziciju igraca mozete ostaviti kao NULL)	
	public void dodajUtakmicu(String domaci, String gost, int kolo, String ishod, int godina) {
		try {
			if (!ishod.equals("X") && !ishod.equals("1") && !ishod.equals("2")) {
				System.out.println("Ukucali ste los ishod(prihvata se X, 1 , 2)");
				return;
			}
			boolean imaGost = false;
			boolean imaDomacin = false;
			PreparedStatement naziv = connection.prepareStatement("Select Naziv" + " FROM TIM" + " WHERE Naziv = ? OR Naziv = ?");
			naziv.setString(1, domaci);
			naziv.setString(2, gost);
			ResultSet provera = naziv.executeQuery();
			while (provera.next()) {
				if (provera.getString(1).equals(gost)) {
					imaGost = true;
				}
				if (provera.getString(1).equals(domaci)) {
					imaDomacin = true;
				}
			}
			if (!imaDomacin || !imaGost) {
				System.out.println("Jedan od timova koji ste ukucali ne postoji");
				return;
			}
		}
			catch(SQLException e) {
				
			}
		try {
			PreparedStatement koloCheck = connection.prepareStatement("SELECT Naziv"
					+ " FROM Tim JOIN Utakmica on (IdTim = IdDomacin OR IdTim = IdGost)"
					+ " WHERE KOLO = ?");
			koloCheck.setInt(1, kolo);
			ResultSet rsKolo = koloCheck.executeQuery();
			boolean ima = false;
			while(rsKolo.next()) {
				if (rsKolo.getString(1).equals(domaci) || rsKolo.getString(1).equals(gost)) {
					ima = true;
				}
			}
			if (ima) {
				System.out.println("Tim ne moze da igra dva puta u istom kolu" );
				return;
			}
		} catch(SQLException e) {
			
		}
		try {
			PreparedStatement maxps = connection.prepareStatement("SELECT MAX(IdUta)" + " FROM Utakmica");
			ResultSet maxRs = maxps.executeQuery();
			int idUta = maxRs.getInt(1) + 1;
			PreparedStatement ps = connection.prepareStatement("INSERT INTO Utakmica(IdUta, IdDomacin, IdGost, Kolo, Ishod, Godina)"
							+ " VALUES(?, (SELECT IdTim FROM Tim WHERE Naziv = ?), (SELECT IdTim FROM Tim WHERE Naziv = ?), ?, ?, ?)");
			ps.setInt(1, idUta);
			ps.setString(2, domaci);
			ps.setString(3, gost);
			ps.setInt(4, kolo);
			ps.setString(5, ishod);
			ps.setInt(6, godina);
			ps.executeUpdate();
			PreparedStatement psUtakmice = connection.prepareStatement("INSERT INTO IGRAO(idFud,idUta, PozicijaIgraca)"
					+ " SELECT IdFud, IdUta, null"
					+ " FROM Fudbaler JOIN Tim  using(IdTim) JOIN Utakmica on (IdTim = IdDomacin OR IdTim = IdGost)"
					+ " WHERE IdUta = ?");
			psUtakmice.setInt(1, idUta);
			psUtakmice.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
//	-metodu koja iz baze brise tim(parametar je ime tima), da bi se mogao obrisati tim,
//	moraju se prvo obrisati svi igraci koji igraju u tom timu,
//	da bi se obrisali svi igraci, moraju se prvo obrisati svi golovi i kartoni i igrao za svakog igraca,
//	a da bi se oni obrisali moraju se prvo obrisati sve utakmice u kojima je svaki igrac igrao
	public void obrisiTim(String ime) {
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement("SELECT Naziv"
					+ " FROM Tim");
			ResultSet rs = ps.executeQuery();
			boolean postoji = false;
			while(rs.next()) {
				if(ime.equals(rs.getString(1))) {
					postoji = true;
				}
			}
			if(!postoji) {
				System.out.println("Tim koji ste ukucali ne postoji");
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		 try {
			ps = connection.prepareStatement("DELETE FROM Utakmica"
					+ " WHERE IdDomacin in (SELECT IdTim FROM Tim WHERE Tim.Naziv = ?) OR IdGost in (SELECT IdTim FROM Tim WHERE Tim.Naziv = ?)");
			ps.setString(1, ime);
			ps.setString(2, ime);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {
			ps = connection.prepareStatement("DELETE FROM Igrao"
					+ " WHERE IdFud IN (SELECT IdFud FROM Fudbaler WHERE IdTim IN (SELECT IdTim FROM Tim WHERE Naziv = ?))");
			ps.setString(1, ime);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			ps = connection.prepareStatement("DELETE FROM Gol"
					+ " WHERE IdFud IN (SELECT IdFud FROM Fudbaler WHERE IdTim IN (SELECT IdTim FROM Tim WHERE Naziv = ?))");
			ps.setString(1, ime);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {
			ps = connection.prepareStatement("DELETE FROM Karton"
					+ " WHERE IdFud IN (SELECT IdFud FROM Fudbaler WHERE IdTim IN (SELECT IdTim FROM Tim WHERE Naziv = ?))");
			ps.setString(1, ime);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {
			ps = connection.prepareStatement("DELETE FROM Fudbaler"
					+ " WHERE IdTim IN (SELECT IdTim FROM Tim WHERE Naziv = ?)");
			ps.setString(1, ime);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {
			ps = connection.prepareStatement("DELETE FROM Tim"
					+ " WHERE Tim.naziv = ?");
			ps.setString(1, ime);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
