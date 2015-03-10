package klient;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class KalenderController {
	
	public static ArrayList<Dag> dager;
	@FXML private Button refresh, grupper;
	@FXML private GridPane ruter;
	@FXML private Button forrigeManed;
	@FXML private Button nesteManed;
	@FXML private Label manedLabel, brukernavn, arLabel;
	@FXML private ListView<String> notifikasjoner;
	@FXML private ChoiceBox<String> filtrering;
	public static String[] notifikasjon_liste;
	private int maned;
	private int aar;
	private String[] avtale_liste;
	private int filtverdi;
	public static String[] enheter;
	public static List<String> list;
	public static String melding;
	private ObservableList<String> items;
	public static ArrayList<ArrayList<String>> notifikasjon_utenMelding;
	public static ArrayList<String> utenMelding;
	public static ArrayList<String> meldinger;
	public static ArrayList<Varsel> oppdelte_notifikasjoner;
	public static String valg;
	private boolean ingenInvitasjoner = false;
	
	public void initialize() throws Exception{
		setMonth(LocalDate.now().getMonthValue());
		setYear(LocalDate.now().getYear());
		setFiltVerdi(0);
		setUpFiltrering();
		flushView();
	}
	
	private void flushView() throws Exception{
		notifikasjon_utenMelding = new ArrayList<ArrayList<String>>();
		dager = new ArrayList<Dag>();
		meldinger = new ArrayList<String>();
		list = new ArrayList<String>();
		ruter.getChildren().clear();
		getDays();
		loadGrid();	
		svarInvite();
		showInvitasjoner();
		showNotifications();
		setItems();
		showBruker();
	}
	
	private void setUpFiltrering(){
		filtrering.setItems(FXCollections.observableArrayList("Alle","Bare godtatt","Ikke svart","Avslag"));
		filtrering.setValue("Alle");
		filtrering.getSelectionModel().selectedIndexProperty().addListener(filtChange);
	}
	
	ChangeListener<? super Number> filtChange = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			switch(filtrering.getItems().get((Integer) newValue)){
			case "Alle":
				setFiltVerdi(0);
				try {
					flushView();
				} catch (Exception e) {
				}
				break;
			case "Bare godtatt":
				setFiltVerdi(1);
				try {
					flushView();
				} catch (Exception e) {
				}
				break;
			case "Ikke svart":
				setFiltVerdi(2);
				try {
					flushView();
				} catch (Exception e) {
				}
				break;
			case "Avslag":
				setFiltVerdi(3);
				try {
					flushView();
				} catch (Exception e) {
				}
				break;
			}
		}
	};
	
// GÅ TIL EN NY SCREEN*****************
	public void grupperView(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.GRUPPER);
	}
	
	public void redigerBrukerView(ActionEvent event){
		ScreenNavigator.loadScreen(ScreenNavigator.BRUKERREDIGERING);
	}
	
	public void refreshKalender(ActionEvent event) throws Exception {
		flushView();
	}
	
	private void showBruker() {
		System.out.println("navnet: " + Klienten.bruker.getNavn());
		brukernavn.setText(Klienten.bruker.getNavn());
	}
	
	private void svarInvite() {
		ChangeListener<String> invite = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				try {
					enheter = newValue.split(" ");
					if (enheter[0].equals("Invitasjon:")) {
						ScreenNavigator.loadScreen(ScreenNavigator.SE_AVTALE);
					}
					else {
						try {
							pop(newValue);
						} catch (Exception e) {
							System.out.println("FEIL: " + e);
						}
					}
				}
				catch (NullPointerException e) {
					
				}
			
			}
			
		};
		notifikasjoner.getSelectionModel().selectedItemProperty().addListener(invite);
	}
	
	private void pop(String notifikasjon_trykketPå) throws Exception {
		String[] enhetene = notifikasjon_trykketPå.split(" ");
		valg = enhetene[1];
		Popup pop = new Popup();
		pop.start(new Stage());
	}
	
	private void showInvitasjoner() throws Exception {
		String[] notifikasjonene = Klienten.getInvitasjoner(Klienten.bruker).split(" ");
		for (int i = 0; i < notifikasjonene.length; i++) {
			if (notifikasjonene[i].trim().equals("NONE") || notifikasjonene[i].trim().equals("-1")) {
				ingenInvitasjoner = true;
			}
			else if (notifikasjonene[i].equals("\r\n")) {
			}
			else {
				list.add("Invitasjon: " + notifikasjonene[i] + " (Dobbeltrykk)");
				ingenInvitasjoner = false;
			}
		}
	}
	
	private void showNotifications() throws Exception {
		boolean meldingFerdig = false;
		ArrayList<String> resten = new ArrayList<String>();
		oppdelte_notifikasjoner = new ArrayList<Varsel>();
		notifikasjon_liste = Klienten.getVarsel().split(" ");
		String meld = "";
		if (! notifikasjon_liste[0].trim().toString().equals("NONE")) {
			for (String notifikasjon_oppdeling : notifikasjon_liste) {
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
	
	private void setItems() {
		items = FXCollections.observableList(list);
		notifikasjoner.setItems(items);
	}
	
	private void setMonth(int month){
		maned = month;
		manedLabel.setText(LocalDate.of(2015, month, 01).getMonth().toString());
	}
	
	private int getMonth(){
		return maned;
	}
	
	private void setYear(int year){
		aar = year;
		arLabel.setText(String.valueOf(year));
	}
	
	private int getYear(){
		return aar;
	}
	
	private void getDays(){
		try{
			int i=1;
			while(true){
				LocalDate dato = LocalDate.of(aar, maned, i);
				Dag dag = new Dag(dato);
				dager.add(dag);
				i++;
			}
		}
		catch (DateTimeException e){}
	}
	
	private Dag getDag(LocalDate dato) {
		for (Dag dag : dager) {
			if (dag.getDato().equals(dato)) {
				return dag;
			}
		}
		return null;
	}
	
	private void setFiltVerdi(int verdi) {
		this.filtverdi = verdi;
	}
	
	private int getFiltVerdi() {
		return this.filtverdi;
	}
	
	
	
	private void loadGrid() throws IOException{
		int lengde = dager.size();
		hentAvtaler();
		LocalDate fysteDag = dager.get(0).getDato();
		int firstDiM = fysteDag.getDayOfWeek().getValue()-1;
		int t=0;
		for(int j=firstDiM;j<7;j++){
			Label label = new Label(dager.get(t).getDayinMonth());
			ruter.add(label, j, 0);
			ruter.setValignment(label, VPos.TOP);
			setTexts(j,0,t);
			t++;
		}
		for(int i=1;i<8;i++){
			for(int j=0;j<7;j++){
				Label label = new Label(dager.get(t).getDayinMonth());
				ruter.add(label, j, i);
				ruter.setValignment(label, VPos.TOP);
				setTexts(j,i,t);
				t++;
				if(t>=lengde){
					break;
				}
			}
			if(t>=lengde){
				break;
			}
		}
	}
	
	private void hentAvtaler() throws IOException {
		try {
			if (Klienten.avtaler.isEmpty()) {
				avtale_liste = Klienten.mineAvtaler(Klienten.bruker.getEmail(), getFiltVerdi()).split(" ");
				for (int k = 0; k < avtale_liste.length; k++) {
					if (k%2 != 0) {
						String dato = avtale_liste[k];
						String avtaleid = avtale_liste[k-1];
						createAvtale(dato, avtaleid);
					}
				}
			}
			else {
				avtale_liste = Klienten.mineAvtaler(Klienten.bruker.getEmail(), getFiltVerdi()).split(" ");
			}
		}
		catch (NullPointerException e) {
		}
		
	}
	
	
	// VELDIG MYE TULL, MEN FUNKER
	//Lager: bruker(deltaker)-objektene, avtale-objekt, møterom-objekt og tidsobjekt
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
		String[] deltakere = Klienten.getDeltakere(avtaleid, "1").split(" ");
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
		getDag(LocalDate.of(Integer.parseInt(dato.substring(0,4)),
						Integer.parseInt(dato.substring(5,7)), Integer.parseInt(dato.substring(8,10)))).addAvtale(avtale);
	}
	
	private void setTexts(int j,int i,int t) throws IOException{
		Text text = new Text();
		String avtale_dag = dager.get(t).getDato().toString();
		String temp = "";
		for (int k = 0; k < avtale_liste.length; k++) {
			if (k%2 != 0) {
				String dato = avtale_liste[k];
				String avtaleid = avtale_liste[k-1];
				if (avtale_dag.equals(dato)) {
					temp += avtaleid + " ";
				}
			}
		}
		text.setText(temp);
		ruter.add(text, j, i);
	}
	
	@FXML
	private void nextPaneDayView(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.DAGSVISNING);
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
	private void nextMonth(ActionEvent event) throws Exception {
		if(getMonth()+1>=13){
			setMonth(1);
			setYear(getYear()+1);
		}
		else{
			setMonth(getMonth()+1);
		}
		flushView();
	}
	
	@FXML
	private void previousMonth(ActionEvent event) throws Exception {
		if(getMonth()-1<=0){
			setMonth(12);
			setYear(getYear()-1);
		}
		else{
			setMonth(getMonth()-1);
		}
		flushView();
	}
	
	@FXML
	private void logout() throws IOException{
		Klienten.logout();
	}
}
