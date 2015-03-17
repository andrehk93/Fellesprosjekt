package klient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UkesvisningController {
	
	@FXML private GridPane ruter;
	@FXML private ScrollPane scroll;
	@FXML private Label ukeNr, brukernavn, arLabel, manedLabel;
	@FXML private Button nesteUke, grupper, brukerredigering, refresh;
	@FXML private Button forrigeUke, ekstraKal;
	@FXML private ChoiceBox<String> filtrering;
	@FXML private Text man, tir, ons, tor, fre, lor, son;
	
	private ArrayList<Varsel> oppdelte_notifikasjoner;
	private ObservableList<String> items;
	private ArrayList<Avtale> ukeAvtaler;
	private LocalDate firstDayOfWeek;
	private int weekNumber;
	private static ArrayList<Dag> dager;
	private static final double colWidth = 98;
	private ArrayList<StackPane> bokser;
	private String[] notifikasjonene;
	private boolean ingenInvitasjoner;
	@FXML private ListView<String> notifikasjoner_lv;
	private List<String> list;

	
	public void initialize() throws Exception {
		bokser = new ArrayList<StackPane>();
		scroll.setVvalue(scroll.getVmin());
		firstDayOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()-1);
		setUpFiltrering();
		loadStuff();
	}
	
	private void loadStuff() throws Exception {
		weekNumber = firstDayOfWeek.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		ukeNr.setText(String.valueOf(weekNumber));
		dager = new ArrayList<Dag>();
		dager.add(new Dag(firstDayOfWeek));
		for(int i=1;i<7;i++){
			dager.add(new Dag(firstDayOfWeek.plusDays(i)));
		}
		loadDayTexts();
		flushView();
		showBruker();
		this.notifikasjonene = KalenderController.notifikasjonene;
		settUpListView();
		showInvitasjoner();
		showNotifications();
		ruter.setGridLinesVisible(true);
		setItems();
		loadGrid();
	}
	
	private void loadDayTexts() {
		man.setText(dager.get(0).getDagNavn()+" "+dager.get(0).getDayinMonth()+".");
		tir.setText(dager.get(1).getDagNavn()+" "+dager.get(1).getDayinMonth()+".");
		ons.setText(dager.get(2).getDagNavn()+" "+dager.get(2).getDayinMonth()+".");
		tor.setText(dager.get(3).getDagNavn()+" "+dager.get(3).getDayinMonth()+".");
		fre.setText(dager.get(4).getDagNavn()+" "+dager.get(4).getDayinMonth()+".");
		lor.setText(dager.get(5).getDagNavn()+" "+dager.get(5).getDayinMonth()+".");
		son.setText(dager.get(6).getDagNavn()+" "+dager.get(6).getDayinMonth()+".");
	}

	private void showBruker() {
		brukernavn.setText(Klienten.bruker.getNavn());
	}
	
	private void showInvitasjoner() throws Exception {
		
		//VISER ALLE INVITASJONER HENTET I MÅNEDSVISNINGEN
		
		for (int i = 0; i < notifikasjonene.length; i++) {
			if (notifikasjonene[i].trim().equals("NONE") || notifikasjonene[i].trim().equals("-1")) {
				ingenInvitasjoner = true;
			}
			else if (notifikasjonene[i].equals("\r\n")) {
			}
			else {
				list.add("Invitasjon: " + notifikasjonene[i].trim() + " (Trykk her)");
				ingenInvitasjoner = false;
			}
		}
	}
	
	private void settUpListView() {
		ChangeListener<String> invite = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				try {
					KalenderController.enheter = newValue.split(" ");
					if (KalenderController.enheter[0].equals("Invitasjon:")) {
						Klienten.setValgtAvtale(KalenderController.enheter[1]);
						ScreenNavigator.loadScreen(ScreenNavigator.SE_AVTALE);
						KalenderController.enheter = null;
					}
					else {
						try {
							KalenderController.pop(newValue);
						} catch (Exception e) {
							System.out.println("FEIL: " + e);
						}
						KalenderController.enheter = null;
					}
				}
				catch (NullPointerException e) {
					
				}
			
			}
			
		};
		notifikasjoner_lv.getSelectionModel().selectedItemProperty().addListener(invite);
	}
	
	private void setItems() {
		items = FXCollections.observableList(list);
		notifikasjoner_lv.setItems(items);
	}
	
	private void showNotifications() throws Exception {
		boolean meldingFerdig = false;
		ArrayList<String> resten = new ArrayList<String>();
		oppdelte_notifikasjoner = new ArrayList<Varsel>();
		String streng1 = Klienten.getVarsel();
		KalenderController.notifikasjon_liste = streng1.split(" ");
		String meld = "";
		if (! KalenderController.notifikasjon_liste[0].trim().toString().equals("NONE")) {
			for (String notifikasjon_oppdeling : KalenderController.notifikasjon_liste) {
				if(! notifikasjon_oppdeling.equals("!ENDMESS!") && ! meldingFerdig) {
					meld += notifikasjon_oppdeling + " ";
				}
				else if(notifikasjon_oppdeling.equals("!ENDMESS!")) {
					meldingFerdig = true;
				}
				else if (! notifikasjon_oppdeling.equals("!END!")) {
					resten.add(notifikasjon_oppdeling);
				}
				else if (notifikasjon_oppdeling.equals("!END!")){
					Varsel vars = new Varsel(meld, null, false, null, null);
					vars.setBrukerSendtFra(resten.get(0));
					vars.setAvtaleid(resten.get(1));
					vars.setTidspunkt(resten.get(2));
					list.add("Notifikasjon: " + vars);
					oppdelte_notifikasjoner.add(vars);
					resten = new ArrayList<String>();
					meld = "";
					meldingFerdig = false;
					ingenInvitasjoner = false;
				}
				
			}
		}
		else {
			if (ingenInvitasjoner) {
				list.add("Ingen notifikasjoner");
			}
		}
	}
	
	private void loadGrid() throws IOException {
		if(Klienten.avtaler.isEmpty() || Klienten.getChanged()){
			Klienten.setChanged(false);
		}
		setUkeAvtaler();
		setTimeLabels();
		for(Avtale app : ukeAvtaler){
			setRects(app);
		}
	}
	
	private void flushView(){
		list = new ArrayList<String>();
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
		double ySize = app.getTid().getWeekSize();
		int day = app.getTid().getDato().getDayOfWeek().getValue();
		double margin = app.getTid().getMargin();
		Rectangle box = new Rectangle(colWidth,ySize);
		if(app.getStranger()){
			box.setFill(Color.RED);
		}
		else{
			box.setFill(Color.BLUE);
		}
		Text text = new Text(app.getAvtaleid());
	    text.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
	    text.setFill(Color.WHITE);
	    text.setStroke(Color.WHITE); 
	    setUpBox(box,app);
		stack.getChildren().addAll(box,text);
		stack.setMinSize(0, 0);
		StackPane.setAlignment(box, Pos.TOP_LEFT);
		StackPane.setAlignment(text, Pos.TOP_LEFT);
		StackPane.setMargin(box, new Insets(margin+1,0,0,1));
		StackPane.setMargin(text, new Insets(margin+1,0,0,1));
		bokser.add(stack);
		ruter.add(stack, day, gridPos);
		GridPane.setValignment(stack, VPos.TOP);
		
	}

	private void setUpBox(Rectangle box, Avtale app) {
		box.setOnMouseEntered(new EventHandler<Event>() {
		    public void handle(Event event) {
		    	if(app.getStranger()){
					box.setFill(Color.PINK);
				}
				else{
					box.setFill(Color.LIGHTBLUE);
				}
		    }
		});
		
		box.setOnMouseExited(new EventHandler<Event>() {
			public void handle(Event event) {
				if(app.getStranger()){
					box.setFill(Color.RED);
				}
				else{
					box.setFill(Color.BLUE);
				}
			}
		});
		
		box.setOnMouseClicked(new EventHandler<Event>() {
			public void handle(Event event) {
				String avtale = app.getAvtaleid();
				Klienten.setValgtAvtale(avtale);
				if(app.getEier().getEmail().equals(Klienten.bruker.getEmail())){
					
					ScreenNavigator.loadScreen(ScreenNavigator.ENDRE_AVTALE);
				}
				else{
					ScreenNavigator.loadScreen(ScreenNavigator.SE_AVTALE);
				}
			}
		});
	}
	
	public void setUpFiltrering(){
		ObservableList<String> lista = FXCollections.observableArrayList("Alle","Bare godtatt","Ikke svart","Avslag");
		filtrering.setItems(lista);
		filtrering.setValue(lista.get(Klienten.getFiltrering()));
		filtrering.getSelectionModel().selectedIndexProperty().addListener(filtChange);
	}

	ChangeListener<? super Number> filtChange = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			switch(filtrering.getItems().get((Integer) newValue)){
			case "Alle":
				Klienten.setFiltrering(0);
				try {
					(new KalenderController()).hentAvtaler();
					loadStuff();
				} catch (Exception e) {
				}
				break;
			case "Bare godtatt":
				Klienten.setFiltrering(1);
				try {
					(new KalenderController()).hentAvtaler();
					loadStuff();
				} catch (Exception e) {
				}
				break;
			case "Ikke svart":
				Klienten.setFiltrering(2);
				try {
					(new KalenderController()).hentAvtaler();
					loadStuff();
				} catch (Exception e) {
				}
				break;
			case "Avslag":
				Klienten.setFiltrering(3);
				try {
					(new KalenderController()).hentAvtaler();
					loadStuff();
				} catch (Exception e) {
				}
				break;
			}
		}
	};
	
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
	private void nextPaneEditUsers(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.BRUKERREDIGERING);
	}
	
	@FXML
	private void nextWeek(ActionEvent event) throws Exception {
		firstDayOfWeek = firstDayOfWeek.plusWeeks(1);
		loadStuff();
	}
	
	@FXML
	private void prevWeek(ActionEvent event) throws Exception {
		firstDayOfWeek = firstDayOfWeek.minusWeeks(1);
		loadStuff();
	}
	
	@FXML
	public void refreshKalender(ActionEvent event) throws Exception {
		flushView();
	}
	
	@FXML
	public void extraCal(ActionEvent event) throws IOException{
		Klienten.setDest("/klient/extraPopup.fxml");
		Popup pop = new Popup();
		pop.start(new Stage());
	}
	
	@FXML
	private void logout() throws IOException{
		Klienten.logout();
	}

}
