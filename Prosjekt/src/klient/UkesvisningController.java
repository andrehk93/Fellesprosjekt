package klient;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class UkesvisningController {
	
	@FXML private GridPane ruter;
	@FXML private ScrollPane scroll;
	@FXML private Label ukeNr;
	@FXML private Button nesteUke;
	@FXML private Button forrigeUke;
	
	private String[] avtaleListe;
	private LocalDate firstDayOfWeek;
	private int weekNumber;
	private static ArrayList<Dag> dager;
	private static final double colWidth = 97;
	
	public void initialize() throws IOException {
		scroll.setVvalue(scroll.getVmin());
		firstDayOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()-1);
		loadStuff();
	}
	
	private void loadStuff() throws IOException {
		weekNumber = firstDayOfWeek.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		ukeNr.setText(String.valueOf(weekNumber));
		dager = new ArrayList<Dag>();
		dager.add(new Dag(firstDayOfWeek));
		for(int i=1;i<7;i++){
			dager.add(new Dag(firstDayOfWeek.plusDays(i)));
		}
		loadGrid();
	}
	
	private void loadGrid() throws IOException {
		hentAvtaler();
		System.out.println("1");
		for(Avtale app : Klienten.avtaler){
			System.out.println("2");
			setRects(app);
		}
	}
	
	private void setRects(Avtale app) throws IOException{
		int gridPos = app.getTid().getWeekGridPos();
		double ySize = app.getTid().getWeekSize();
		int day = app.getTid().getDato().getDayOfWeek().getValue();
		int rowSpan = app.getTid().getDuration().getHour()+1;
		Rectangle box = new Rectangle(colWidth,ySize);
		box.setFill(Color.BLUE);
		ruter.add(box, day, gridPos);
		ruter.setRowSpan(box, rowSpan);
	}
	
	private void hentAvtaler() throws IOException {
		try {
			if (Klienten.avtaler.isEmpty()) {
				System.out.println("4");
				avtaleListe = Klienten.mineAvtaler(Klienten.bruker.getEmail(), 0).split(" "); //Husk filtrering!!!
				for (int k = 0; k < avtaleListe.length; k++) {
					if (k%2 != 0) {
						String dato = avtaleListe[k];
						String avtaleid = avtaleListe[k-1];
						createAvtale(dato, avtaleid);
					}
				}
			}
			else {
				System.out.println("7");
				avtaleListe = Klienten.mineAvtaler(Klienten.bruker.getEmail(), 0).split(" ");
			}
		}
		catch (NullPointerException e) {
		}
		
	}
	
	private void createAvtale(String dato, String avtaleid) throws IOException {
		ArrayList<Bruker> deltaker_liste = new ArrayList<Bruker>();
		String romnavn = Klienten.getAvtaleRom(avtaleid.trim()).trim();
		int kapasitet = Integer.parseInt(Klienten.getRomStr(romnavn).trim());
		Møterom rom = new Møterom(kapasitet, romnavn);
		String[] tiden = Klienten.getTidspunkt(avtaleid).split(" ");
		TidsIntervall tid = new TidsIntervall(LocalTime.of(Integer.parseInt(tiden[0].substring(0,2)),
				Integer.parseInt(tiden[0].substring(3,5))), LocalTime.of(Integer.parseInt(tiden[1].substring(0,2)),
				Integer.parseInt(tiden[1].substring(3,5))), LocalDate.of(Integer.parseInt(dato.substring(0,4)),
						Integer.parseInt(dato.substring(5,7)), Integer.parseInt(dato.substring(8,10))));
		String[] deltakere = Klienten.getDeltakere(avtaleid).split(" ");
		if (! deltakere.toString().equals(null) && ! deltakere.equals("NONE")) {
			for (String epost : deltakere) {
				if (epost.trim().equals("NONE") || epost.trim().equals("")) {
					System.out.println("BREAK");
					break;
				}
				else {
					Bruker deltaker = new Bruker(Klienten.getBruker(epost), epost);
					deltaker_liste.add(deltaker);
					System.out.println("deltaker"+deltaker);
				}
			}
		}
		Avtale avtale = new Avtale(Klienten.bruker, deltaker_liste, tid, rom, avtaleid);
		Klienten.avtaler.add(avtale);
		System.out.println("Jepp: "+avtale);
		Dag dagen = new Dag(tid.getDato());
		System.out.println(dagen);
		dagen.addAvtale(avtale);
	}
	
	private Dag getDag(LocalDate dato) {
		for (Dag dag : dager) {
			if (dag.getDato().equals(dato)) {
				return dag;
			}
		}
		return null;
	}
	
	@FXML
	private void nextPaneMonthView(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
	
	@FXML
	private void nextPaneDayView(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.DAGSVISNING);
	}
	
	@FXML
	private void nextPaneMakeAppointment(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.AVTALE);
	}
	
	@FXML
	private void nextPaneEditGroups(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.GRUPPER);
	}
	
	@FXML
	private void nextPaneLogOut(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.INNLOGGING);
	}
	
	@FXML
	private void nextWeek(ActionEvent event) throws IOException {
		firstDayOfWeek = firstDayOfWeek.plusWeeks(1);
		loadStuff();
	}
	
	@FXML
	private void prevWeek(ActionEvent event) throws IOException {
		firstDayOfWeek = firstDayOfWeek.minusWeeks(1);
		loadStuff();
	}

}
