public class Teren {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBFudbalskiSavez db = new DBFudbalskiSavez("jdbc:sqlite:C:\\Users\\Srdja\\Documents\\SQL\\Domaci SQL Java 09062019\\FudbalskiSavez.db");
		db.connect();
		db.imenaSvihFudbalera();
		db.imenaFudbaleraManchesterUniteda();
		db.goloviPoFudbaleru();
		db.fudbalerSaNajviseGolova();
		db.brojKartona();
		db.gradoviGdeJeFudbalerIgrao(13);
		db.dodajFudbalera("Pele", "Barselona");
		db.dodajTim("Soker Tim", "Britanija");
		db.dodajUtakmicu("Arsenal", "Manchester United", 12, "X", 1983);
		db.obrisiTim("Arsenal");
		db.dissconect();
	}

}
