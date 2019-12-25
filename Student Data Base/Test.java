public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBStudentskaBaza db = new DBStudentskaBaza("jdbc:sqlite:C:\\Users\\Srdja\\Documents\\SQL\\Domaci SQL Java 09052019\\Kopija studentskabaza.db");
		db.connect();
		db.dodajStudenta(20140028, "Pilos", "Meric", "06.07.2014", "20.01.1995", "Beograd");
		db.podaciSvihPredmeta();
		db.sviIzBeograda();
		db.mestoRodjenja();
		db.viseOdSest();
		db.predmetiIzmedju8I5();
		db.studentIma81_76ili59();
		db.studentNema81_76I59();
		db.cenaPredmeta();
		db.izbrisatiNevalidnu();
		db.setDatumRodjenja(20140028, "07.07.1997");
		db.disconnect();
	}

}
