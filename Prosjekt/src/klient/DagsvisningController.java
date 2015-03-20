package klient;

import java.io.IOException;
import java.time.DayOfWeek;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DagsvisningController {
	
	
	@FXML private ListView<String> notifikasjoner_lv;
	@FXML private GridPane ruter;
	@FXML private Label datoTekst, brukernavn, ukenrTekst, aarTekst;
	@FXML private Button ukeBtn, manedBtn, nesteDag, forrigeDag;
	@FXML private ChoiceBox<String> filtrering;
	
	private String[] notifikasjonene;
	private boolean ingenInvitasjoner;
	private DayOfWeek weekday;
	private List<String> list;
	private ArrayList<Varsel> oppdelte_notifikasjoner;
	private ObservableList<String> items;
	private String[] avtaleListe;
	private ArrayList<StackPane> bokser;
	private Dag dagen;
	private int weekNumber;
	private ArrayList<Avtale> dagAvtaler;
	private double colWidth;
	
	public void initialize() throws Exception {
		list = new ArrayList<String>();
		bokser = new ArrayList<StackPane>();
		dagen = new Dag(LocalDate.now());
		colWidth = 700.0;
		setUpFiltrering();
		showBruker();
		this.notifikasjonene = KalenderController.notifikasjonene;
		loadStuff();
	}
	
	private void loadStuff() throws Exception {
		weekday = dagen.getDato().getDayOfWeek();
		weekNumber = dagen.getDato().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		ukenrTekst.setText(String.valueOf(weekNumber));
		aarTekst.setText(String.valueOf(dagen.getDato().getYear()));
		setDatoTekst();
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
	
	private void loadGrid() throws IOException {
		if(Klienten.avtaler.isEmpty() || Klienten.getChanged()){
			Klienten.setChanged(false);
		}
		setDagAvtaler();
		setTimeLabels();
		for(Avtale app : dagAvtaler){
			setRects(app);
		}
	}
	
	private void setRects(Avtale app) throws IOException{
		StackPane stack = new StackPane();
		int gridPos = app.getTid().getWeekGridPos();
		double ySize = app.getTid().getWeekSize();
		double margin = app.getTid().getMargin();
		Rectangle box = new Rectangle(colWidth,ySize);
		if(app.getStranger()){
			box.setFill(Color.RED);
		}
		else{
			box.setFill(Color.BLUE);
		}
		Text text = new Text(app.getAvtaleNavn());
	    text.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
	    text.setFill(Color.WHITE);
	    text.setStroke(Color.WHITE); 
	    setUpBox(box,app);
		stack.getChildren().addAll(box,text);
		stack.setMinSize(0, 0);
		StackPane.setAlignment(box, Pos.TOP_LEFT);
		StackPane.setAlignment(text, Pos.TOP_LEFT);
		StackPane.setMargin(box, new Insets(margin+1,0,0,0));
		StackPane.setMargin(text, new Insets(margin+1,0,0,0));
		bokser.add(stack);
		ruter.add(stack, 1, gridPos);
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
					try {
						Klienten.setValgtAvtale(KalenderController.enheter[1]);
					}
					catch (NullPointerException e) {
					}
					ScreenNavigator.loadScreen(ScreenNavigator.ENDRE_AVTALE);
				}
				else{
					ScreenNavigator.loadScreen(ScreenNavigator.SE_AVTALE);
				}
			}
		});
	}
	
	private void setDagAvtaler(){
		dagAvtaler = new ArrayList<Avtale>();
		for(Avtale app : Klienten.avtaler){
			if(app.getTid().getDato().equals(dagen.getDato())){
				dagAvtaler.add(app);
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
	
	private void flushView(){
		list = new ArrayList<String>();
		for(StackPane panes : bokser){
			ruter.getChildren().remove(panes);
		}
	}
	
	private void setDatoTekst(){
		String t = dagen.getDagNavn()+" ";
		t += dagen.getDayinMonth()+". "+dagen.getManedNavn();
		datoTekst.setText(t);
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
	
	@FXML
	private void nextDay(ActionEvent event) throws Exception {
		dagen.setDato(dagen.getDato().plusDays(1));
		loadStuff();
	}
	
	@FXML
	private void lastDay(ActionEvent event) throws Exception {
		dagen.setDato(dagen.getDato().minusDays(1));
		loadStuff();
	}
	
	@FXML
	private void nextPaneMonthView(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
	
	@FXML
	private void nextPaneWeekView(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.UKESVISNING);
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
	private void nextPaneLogOut(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.INNLOGGING);
	}
	
	@FXML
	public void extraCal(ActionEvent event) throws IOException{
		Klienten.setDest("/klient/extraPopup.fxml");
		Popup pop = new Popup();
		pop.start(new Stage());
	}
	
	@FXML
	public void refreshKalender(ActionEvent event) throws Exception {
		flushView();
	}
	
	

}
