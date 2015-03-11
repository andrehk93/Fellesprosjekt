package klient;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class UkesvisningController {
	
	@FXML private GridPane ruter;
	@FXML private ScrollPane scroll;
	@FXML private Label ukeNr;
	@FXML private Button nesteUke;
	@FXML private Button forrigeUke;
	
	private String[] avtaleListe;
	private ArrayList<Avtale> ukeAvtaler;
	private LocalDate firstDayOfWeek;
	private int weekNumber;
	private static ArrayList<Dag> dager;
	private static final double colWidth = 92;
	private ArrayList<StackPane> bokser;
	private String avtale;

	
	public void initialize() throws IOException {
		bokser = new ArrayList<StackPane>();
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
		flushView();
		ruter.setGridLinesVisible(true);
		loadGrid();
	}
	
	private void loadGrid() throws IOException {
		if(Klienten.avtaler.isEmpty()){
			hentAvtaler();
		}
		setUkeAvtaler();
		System.out.println(ukeAvtaler);
		setTimeLabels();
		for(Avtale app : ukeAvtaler){
			setRects(app);
		}
	}
	
	private void flushView(){
		for(StackPane panes : bokser){
			ruter.getChildren().remove(panes);
		}
	}
	
	private void setUkeAvtaler(){
		ukeAvtaler = new ArrayList<Avtale>();
		for(Avtale app : Klienten.avtaler){
			if(!app.getTid().getDato().isBefore(firstDayOfWeek) && !app.getTid().getDato().isAfter(firstDayOfWeek.plusWeeks(1).minusDays(1))){
				ukeAvtaler.add(app);
			}
		}
	}
	
	private void setTimeLabels(){
		String string = "";
		for(int i=0;i<24;i++){
			string = "0"+String.valueOf(i)+":00";
			Label label = new Label(string.substring(string.length()-5));
			ruter.add(label, 0, i);
		}
	}
	
	private void setRects(Avtale app) throws IOException{
		StackPane stack = new StackPane();
		int gridPos = app.getTid().getWeekGridPos();
		System.out.println(gridPos);
		double ySize = app.getTid().getWeekSize();
		int day = app.getTid().getDato().getDayOfWeek().getValue();
		double margin = app.getTid().getMargin();
		Rectangle box = new Rectangle(colWidth,ySize);
		box.setFill(Color.BLUE);
		Text text = new Text(app.getAvtaleid());
	    text.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
	    text.setFill(Color.WHITE);
	    text.setStroke(Color.WHITE); 
	    setUpBox(box,app);
		stack.getChildren().addAll(box,text);
		stack.setMinSize(0, 0);
		StackPane.setAlignment(box, Pos.TOP_LEFT);
		StackPane.setAlignment(text, Pos.TOP_LEFT);
		StackPane.setMargin(box, new Insets(margin,0,0,1));
		StackPane.setMargin(text, new Insets(margin,0,0,1));
		bokser.add(stack);
		ruter.add(stack, day, gridPos);
		GridPane.setValignment(stack, VPos.TOP);
		
	}

	private void setUpBox(Rectangle box, Avtale app) {
		box.setOnMouseEntered(new EventHandler<Event>() {
		    public void handle(Event event) {
		        box.setFill(Color.LIGHTBLUE);
		    }
		});
		
		box.setOnMouseExited(new EventHandler<Event>() {
			public void handle(Event event) {
				box.setFill(Color.BLUE);
			}
		});
		
		box.setOnMouseClicked(new EventHandler<Event>() {
			public void handle(Event event) {
				avtale = app.getAvtaleid();
				if(app.getAvtaleAdmin().equals(Klienten.bruker)){
					Klienten.setValgtAvtale(avtale);
					ScreenNavigator.loadScreen(ScreenNavigator.ENDRE_AVTALE);
				}
				else{
					//ScreenNavigator.loadScreen(ScreenNavigator.SE_AVTALE);
				}
			}
		});
	}

	
	private void hentAvtaler() throws IOException {
		try {
			if (Klienten.avtaler.isEmpty()) {
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
		String[] deltakere = Klienten.getDeltakere(avtaleid,"0").split(" ");
		if (! deltakere.toString().equals(null) && ! deltakere.equals("NONE")) {
			for (String epost : deltakere) {
				if (epost.trim().equals("NONE") || epost.trim().equals("")) {
					break;
				}
				else {
					Bruker deltaker = new Bruker(Klienten.getBruker(epost), epost);
					deltaker_liste.add(deltaker);
				}
			}
		}
		Avtale avtale = new Avtale(Klienten.bruker, deltaker_liste, tid, rom, avtaleid);
		Klienten.avtaler.add(avtale);
		Dag dagen = new Dag(tid.getDato());
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
