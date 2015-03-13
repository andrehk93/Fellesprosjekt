package klient;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardDownRightHandler;

import org.controlsfx.control.CheckComboBox;

import com.sun.javafx.css.StyleCache.Key;
//import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;




import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class Endre_avtaleController {
	
	Property<Number> ant_gjester = new SimpleIntegerProperty();
	private ArrayList<Bruker> gjeste_liste;
	private LocalTime start;
	private LocalTime slutt;
	private LocalDate dato;
	private String[] ledigeBrukerEmailer;
	private ArrayList<Bruker> ledigeBrukere;
	private int startT;
	private int startM;
	private int sluttT;
	private int sluttM;
	private LocalTime evigheten;
	private Avtale avtalen;
	private int indexen;
	private HashMap<String,String> oppmoteListe;
	private Bruker theUser;
	
	
	@FXML
	CheckBox hele_dagen = new CheckBox();
	@FXML
	Button leggTil = new Button();
	@FXML
	Button addGjestBtn;
	@FXML
	Text feilDato = new Text();
	@FXML
    TextField avtalenavn = new TextField();
	@FXML
    Text feilTekst = new Text();
    @FXML
    DatePicker startdato = new DatePicker();
    @FXML
    ChoiceBox<String> timeFra = new ChoiceBox<String>();
    @FXML
    ChoiceBox<String> timeTil = new ChoiceBox<String>();
    @FXML
    ChoiceBox<String> minuttFra = new ChoiceBox<String>();
    @FXML
    ChoiceBox<String> minuttTil = new ChoiceBox<String>();
    @FXML
    ComboBox<Bruker> legg_til_gjester = new ComboBox<Bruker>();
    @FXML
    ListView<HBox> gjesteliste = new ListView<HBox>();
    @FXML
    Button s�k_m�terom = new Button();
    @FXML
    ListView<String> m�teromliste = new ListView<String>();
    @FXML
    Slider m�teromliste_slider = new Slider();
    @FXML
    TextField antall_gjester = new TextField();
    @FXML
    TextField valgt_rom = new TextField();
    @FXML
    Button slett_knapp = new Button();
    @FXML
    Button lagre_knapp = new Button();
    @FXML
    RadioButton skal = new RadioButton();
    @FXML
    RadioButton ikke_svart = new RadioButton();
    @FXML
    RadioButton skal_ikke = new RadioButton();
    @FXML
    ListView<HBox> gjesteliste2 = new ListView<HBox>();
    List<HBox> gjestelisten;
    ObservableList<HBox> gjestene;
    ObservableList<HBox> gjestene2;
    
    private Bruker valg;
	
	public void initialize() throws IOException {
		gjeste_liste = new ArrayList<Bruker>();
		gjestelisten = new ArrayList<HBox>();
    	gjestene = FXCollections.observableList(gjestelisten);
    	gjestene2 = FXCollections.observableList(gjestelisten);
    	ledigeBrukerEmailer = Klienten.getAllUserEmails();
		ledigeBrukere = new ArrayList<Bruker>();
		oppmoteListe = new HashMap<String,String>();
		for(String email : ledigeBrukerEmailer){
			if(email != null || email != ""){
				ledigeBrukere.add(new Bruker(Klienten.getBruker(email), email,0));
			}
		}
		
		legg_til_gjester.getItems().addAll(ledigeBrukere);
		insertAppDetails();
		antall_gjester.setDisable(false);
		antall_gjester.setEditable(true);
		ant_gjester.setValue(0);
		ChangeListener<Number> antall = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if (ant_gjester.getValue().intValue() < gjestene.size()) {
					ant_gjester.setValue(gjestene.size());
				}
				antall_gjester.setText(ant_gjester.getValue().toString());
			}
			
		};
		
		
		ChangeListener<String> tekstStr = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				try {
					int str = Integer.parseInt(antall_gjester.getText());
					if (str < gjestene.size()) {
						antall_gjester.setText(gjestene.size()+"");
						antall_gjester.end();
					}
				}
				catch (Exception e) {
					antall_gjester.setText(gjestene.size()+"");
					antall_gjester.end();
				}
			}
			
		};
		antall_gjester.textProperty().addListener(tekstStr);
		ant_gjester.addListener(antall);
		FxUtil.autoCompleteComboBox(legg_til_gjester, FxUtil.AutoCompleteMode.CONTAINING);
		ChangeListener<Bruker> valgt_bruker = new ChangeListener<Bruker>() {

			@Override
			public void changed(ObservableValue<? extends Bruker> arg0,
					Bruker arg1, Bruker arg2) {
				valg = arg2;
			}
			
		};
		legg_til_gjester.getSelectionModel().selectedItemProperty().addListener(valgt_bruker);
		
		
		//layoutX="439.0" layoutY="261.0" prefWidth="150.0"
		
		ChangeListener<Boolean> allDay = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (hele_dagen.isSelected()) {
					timeFra.setDisable(true);
					timeTil.setDisable(true);
					minuttFra.setDisable(true);
					minuttTil.setDisable(true);
					start = LocalTime.of(0, 0);
					slutt = LocalTime.of(23, 59);
				}
				else {
					timeFra.setDisable(false);
					timeTil.setDisable(false);
					minuttFra.setDisable(false);
					minuttTil.setDisable(false);
				}
			}
			
		};
		hele_dagen.selectedProperty().addListener(allDay);
		
		
		feilTekst.setVisible(false);
		feilDato.setVisible(false);
		List<String> list = new ArrayList<String>();
		ObservableList<String> timer = FXCollections.observableList(list);
		for (int i = 0; i < 24; i++) {
			String leggTil = "";
			if (i < 10) {
				leggTil = "0" + i;
			}
			else {
				leggTil = i +"";
			}
			timer.add(leggTil);
		}
		timeFra.setItems(timer);
		timeTil.setItems(timer);
		List<String> list_2 = new ArrayList<String>();
		ObservableList<String> minutter = FXCollections.observableList(list_2);
		for (int i = 0; i < 60; i++) {
			String leggTil = "";
			if (i%5 == 0) {
				if (i < 10) {
					leggTil = "0" + i;
				}
				else {
					leggTil = i +"";
				}
				minutter.add(leggTil);
			}
		}
		minuttFra.setItems(minutter);
		minuttTil.setItems(minutter);
		timeFra.getSelectionModel().selectedIndexProperty().addListener(handleFraTime);
		timeTil.getSelectionModel().selectedIndexProperty().addListener(handleTilTime);
		minuttFra.getSelectionModel().selectedIndexProperty().addListener(handleFraMinutt);
		minuttTil.getSelectionModel().selectedIndexProperty().addListener(handleTilMinutt);
		startT = Integer.parseInt(timeFra.getValue());
		startM = Integer.parseInt(minuttFra.getValue());
		sluttT = Integer.parseInt(timeTil.getValue());
		sluttM = Integer.parseInt(minuttTil.getValue());
		evigheten = LocalTime.of(00, 00);
		start = LocalTime.of(startT, startM);
		slutt = LocalTime.of(sluttT, sluttM);
	}
    
    
    private void insertAppDetails() throws IOException {
    	int indeks = 0;
		for(Avtale app : Klienten.avtaler){
			if(Klienten.getValgtAvtale().equals(app.getAvtaleid())){
				avtalen = app;
				indexen = indeks;
				break;
			}
			indeks++;
		}
		avtalenavn.setText(avtalen.getAvtaleNavn());
		startdato.setValue(avtalen.getTid().getDato());
		dato = startdato.getValue();
		timeFra.setValue(timeStringFormat(String.valueOf(avtalen.getTid().getStart().getHour())));
		timeTil.setValue(timeStringFormat(String.valueOf(avtalen.getTid().getSlutt().getHour())));
		minuttFra.setValue(timeStringFormat(String.valueOf(avtalen.getTid().getStart().getMinute())));
		minuttTil.setValue(timeStringFormat(String.valueOf(avtalen.getTid().getSlutt().getMinute())));
		valgt_rom.setText(avtalen.getRom().getNavn());
		valg = new Bruker();
		for(Bruker brukere : gjeste_liste){
			oppmoteListe.put(brukere.getEmail(), Klienten.getStatus(avtalen.getAvtaleid(), brukere.getEmail()));
		}
		for(Bruker d : avtalen.getDeltakere()){
			for(Bruker b : legg_til_gjester.getItems()){
				if(b.getEmail().equals(d.getEmail())){
					addGjestenEgentlig(b);
					gjeste_liste.add(b);
				}
			}
		}
		valg = null;
		antall_gjester.setText(String.valueOf((int)gjeste_liste.size()/2));
		//setOppmote();
		
    }
    
    private void setOppmote() throws IOException {
		for(Bruker b : gjeste_liste){
			String oppmote = Klienten.getStatus(avtalen.getAvtaleid(), b.getEmail());
			
		}
		
	}

    public void addRadioListeners() {
		ChangeListener<Boolean> select_skal = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (skal.isSelected()) {
					skal_ikke.setSelected(false);
					ikke_svart.setSelected(false);
				}
			}
			
		};
		ChangeListener<Boolean> select_skal_ikke = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (skal_ikke.isSelected()) {
					skal.setSelected(false);
					ikke_svart.setSelected(false);
				}
			}
			
		};
		ChangeListener<Boolean> select_ikke_svart = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (ikke_svart.isSelected()) {
					skal_ikke.setSelected(false);
					skal.setSelected(false);
				}
			}
		};
		skal.selectedProperty().addListener(select_skal);
		skal_ikke.selectedProperty().addListener(select_skal_ikke);
		ikke_svart.selectedProperty().addListener(select_ikke_svart);
	}

	private String timeStringFormat(String s){
    	if(s.length()==1){
    		return "0"+s;
    	}
    	return s;
    }


	public void showRom(ArrayList<String> rommene) {
    	m�teromliste.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(ObservableValue<? extends String> observable,
    	            String oldValue, String newValue) {
    	    	valgt_rom.setText(newValue);
    	    }
    	});
    	ObservableList<String> rom = FXCollections.observableList(rommene);
    	m�teromliste.setItems(rom);
    }
    
    @FXML
    public void slett() throws IOException {
    	Klienten.deleteAvtale(avtalen.getAvtaleid());
    	Klienten.avtaler.remove(indexen);
    	ScreenNavigator.loadScreen(ScreenNavigator.lastScreen);
    }
    
    @FXML
    public void handleDato() {
    	ChangeListener<LocalDate> datoEndring = new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0,
					LocalDate arg1, LocalDate arg2) {
				if(arg2.isBefore(LocalDate.now())){
					feilDato.setVisible(true);
				}
				else{
					feilDato.setVisible(false);
					dato = arg2;
					System.out.println("���������");
				}
			}
    	};
    	startdato.valueProperty().addListener(datoEndring);
    }
    
    private DayOfWeek findWeekDay(String dag) {
    	switch(dag){
    	case "Mandag":
    		return DayOfWeek.MONDAY;
    	case "Tirsdag":
    		return DayOfWeek.TUESDAY;
    	case "Onsdag":
    		return DayOfWeek.WEDNESDAY;
    	case "Torsdag":
    		return DayOfWeek.THURSDAY;
    	case "Fredag":
    		return DayOfWeek.FRIDAY;
    	case "L�rdag":
    		return DayOfWeek.SATURDAY;
    	case "S�ndag":
    		return DayOfWeek.SUNDAY;
    	}
    	return null;
    }
    
    @FXML
    ChangeListener<? super Number> handleFraTime = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			try {
				startT = Integer.parseInt(timeFra.getItems().get((Integer) newValue));
				if(startM!=999){
					start = LocalTime.of(startT, startM);
					feilTekst.setVisible(!sjekkTid(start, slutt));
				}
			}
			catch (NullPointerException e) {
			}
			catch (ArrayIndexOutOfBoundsException a) {
			}
		}
	};
	
	ChangeListener<? super Number> handleTilTime = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			try {
				
				sluttT = Integer.parseInt(timeTil.getItems().get((Integer) newValue));
				if(sluttM!=999){
					slutt = LocalTime.of(sluttT, sluttM);
					System.out.println(slutt);
					feilTekst.setVisible(!sjekkTid(start, slutt));
				}
			}
			catch (NullPointerException e) {
			}
			catch (ArrayIndexOutOfBoundsException a) {
			}
		}
	};
	
	ChangeListener<? super Number> handleFraMinutt = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			try {
				startM = Integer.parseInt(minuttFra.getItems().get((Integer) newValue));
				if(startT!=999){
					start = LocalTime.of(startT, startM);
					feilTekst.setVisible(!sjekkTid(start, slutt));
				}
			}
			catch (NullPointerException e) {
			}
			catch (ArrayIndexOutOfBoundsException a) {
			}
		}
	};
	
	ChangeListener<? super Number> handleTilMinutt = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			try {
				sluttM = Integer.parseInt(minuttTil.getItems().get((Integer) newValue));
				if(sluttT!=999){
					slutt = LocalTime.of(sluttT, sluttM);
					feilTekst.setVisible(!sjekkTid(start, slutt));
				}
			}
			catch (NullPointerException e) {
			}
			catch (ArrayIndexOutOfBoundsException a) {
			}
		}
	};


	public boolean sjekkTid(LocalTime start, LocalTime slutt) {
    	if (start == evigheten || slutt == evigheten) {
    		return true;
    	}
    	else if(start.isBefore(slutt)){
    		return true;
    	}
    	return false;
    }
    
    public void showGjest(HBox boks) {
    	gjestene.add(boks);
    	this.gjesteliste.setItems(gjestene);
    	ant_gjester.setValue(ant_gjester.getValue().intValue()+1);
    }
    
    public void removeGjest(HBox boks) {
    	for (Bruker bruker : gjeste_liste) {
    		Text te = (Text) boks.getChildren().get(0);
    		if (bruker.getNavn().equals(te.getText())) {
    			gjeste_liste.remove(bruker);
    			System.out.println("HERVED FJERNET");
    		}
    		else {
    			System.out.println(boks.getChildren().get(0).toString());
    		}
    	}
    	gjestene.remove(boks);
    	this.gjesteliste.setItems(gjestene);
    	ant_gjester.setValue(ant_gjester.getValue().intValue()-1);
    	antall_gjester.setText(ant_gjester.getValue().intValue() + "");
    }
    
    @FXML
    public void addGjest(ActionEvent event) throws IOException {
    	
    }
    
    @FXML
    private void addGjesten(ActionEvent event) {
    	Bruker selectedUser = FxUtil.getComboBoxValue(legg_til_gjester);
    	addGjestenEgentlig(selectedUser);
    }
    
    private void addGjestenEgentlig(Bruker selectedUser){
    	if (valg != null) {
    		theUser = selectedUser;
        	for(Bruker gjest : gjeste_liste){
        		if(gjest.getEmail() == selectedUser.getEmail()){
        			System.out.println("Brukeren er allerede invitert");
        			return;
        		}
        	}
	    	HBox boks = new HBox();
	    	Button kryss = new Button();
	    	kryss.setText("x");
	    	kryss.setOpacity(0.8);
	    	kryss.setOnAction(new EventHandler<ActionEvent>() {
	
				@Override
				public void handle(ActionEvent event) {
					removeGjest(boks);
				}
	    		
	    	});
	    	String brukernavn = selectedUser.getNavn();
	    	Bruker gjest = new Bruker(brukernavn, selectedUser.getEmail(),0);
	    	boks.getChildren().add(new Text(brukernavn));
	    	ChoiceBox<String> oppmote = new ChoiceBox<String>();
	    	ArrayList<String> valg = new ArrayList<String>();
	    	valg.add("Skal");
	    	valg.add("Ikke svart");
	    	valg.add("Skal ikke");
	    	oppmote.setItems(FXCollections.observableList(valg));
	    	oppmote.setPrefHeight(boks.getPrefHeight());
	    	oppmote.getSelectionModel().selectedIndexProperty().addListener(handleOppmote);
	    	boks.getChildren().add(oppmote);
	    	showGjest(boks);
	    	gjeste_liste.add(gjest);
    	}
    }
    
    @FXML
    public void finnRom(ActionEvent event) throws IOException {
    	String rom = "";
    	try {
	    	if (hele_dagen.isSelected()) {
	    		rom = Klienten.getRom(dato.toString(), "00:00", "23:59", Integer.parseInt(antall_gjester.getText()) + "");
	    	}
	    	else {
	    		rom = Klienten.getRom(dato.toString(), start.toString(), slutt.toString(), Integer.parseInt(antall_gjester.getText()) + "");
	    	}
	    	System.out.println("ROMMENE: " + rom);
	    	rom = Klienten.getRom(dato.toString(), start.toString(), slutt.toString(), gjeste_liste.size() + "");
	    	ArrayList<String> rommene = new ArrayList<String>();
		    String[] rom_listen = rom.split(" ");
		    for (int i = 0; i < rom_listen.length; i++) {
		    	rommene.add(rom_listen[i]);
			}
		    showRom(rommene);
    	}
    	catch (NullPointerException e) {
    		System.out.println("M� fylle ut avgj�rende felter");
    	}
    	System.out.println("ROMMENE: " + rom);
    	ArrayList<String> rommene = new ArrayList<String>();
    	String[] rom_listen = rom.split(" ");
    	for (int i = 0; i < rom_listen.length; i++) {
    		rommene.add(rom_listen[i]);
		}
    	//M� sp�rre database om ledig(e) rom, midlertidig metode:
    	showRom(rommene);
    }
    
    @FXML
	public void handleKeyInput(KeyEvent event) {
		ChangeListener<String> avtaleListener = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				if (basicInputCheck(arg2)) {
					avtalenavn.setStyle("-fx-text-box-border : white ");
				}
				else {
					avtalenavn.setStyle("-fx-text-box-border : red ");
				}
			}
			
		};
		avtalenavn.textProperty().addListener(avtaleListener);
	}
	
    @FXML
	public void lagre(ActionEvent event) throws IOException {
		System.out.println(start);
		System.out.println(slutt);
		System.out.println(dato);
		if (! feilTekst.isVisible() && ! feilDato.isVisible() && avtalenavn.getText() != null){
			System.out.println("WHAAAAAAAAAAAAAT????!?!?!");
			handleChanges();
			ScreenNavigator.loadScreen(ScreenNavigator.lastScreen);
		}
	}
    
    private void handleChanges() throws IOException{
    	boolean nyRom,nyStart,nySlutt,nyDato;
		M�terom rom = new M�terom(sluttM, "he");
		if(!avtalenavn.getText().equals(avtalen.getAvtaleNavn())){
			Klienten.changeAvtale(avtalen.getAvtaleid(), avtalenavn.getText(), "APPNAME");
			avtalen.setAvtaleNavn(avtalenavn.getText());
		}
    	if(!valgt_rom.getText().equals(avtalen.getRom().getNavn())){
			Klienten.changeAvtale(avtalen.getAvtaleid(), valgt_rom.getText(),"ROOM");
			//avtalen.setRom(Klienten.m�terom);
		}
		if(!start.equals(avtalen.getTid().getStart())){
			Klienten.changeAvtale(avtalen.getAvtaleid(), start.toString(),"STARTTIME");
			avtalen.setTid(new TidsIntervall(start,slutt,dato));
		}
		if(!slutt.equals(avtalen.getTid().getSlutt())){
			Klienten.changeAvtale(avtalen.getAvtaleid(), slutt.toString(),"ENDTIME");
			avtalen.setTid(new TidsIntervall(start,slutt,dato));
		}
		if(!dato.equals(avtalen.getTid().getDato())){
			Klienten.changeAvtale(avtalen.getAvtaleid(), dato.toString(), "DATE");
			avtalen.setTid(new TidsIntervall(start,slutt,dato));
		}
		ArrayList<Bruker> nye_gjester = new ArrayList<Bruker>();
		ArrayList<Bruker> fjerna_gjester = new ArrayList<Bruker>();
		if(!gjeste_liste.equals(avtalen.getDeltakere())){
			boolean ny;
			for(Bruker b : gjeste_liste){
				ny = true;
				for(Bruker n : avtalen.getDeltakere()){
					if(b.getEmail().equals(n.getEmail())){
						ny = false;
					}
				}
				if(ny){
					nye_gjester.add(b);
					b.inviterTilNyAvtale(avtalen);
					avtalen.addDeltakere(b);
				}
			}
			boolean fjernet;
			for(Bruker b :  avtalen.getDeltakere()){
				fjernet = true;
				for(Bruker n : gjeste_liste){
					if(b.getEmail().equals(n.getEmail())){
						fjernet = false;
					}
				}
				if(fjernet){
					fjerna_gjester.add(b);
					b.deleteAvtale(avtalen);
				}
			}
		}
    }
	
	public Bruker getBruker() {
		return Klienten.bruker;
	}
	

	public boolean basicInputCheck(String input) {
		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)"
				+ "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		if (input.matches(regex)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	ChangeListener<? super Number> handleOppmote = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			String verdi = "";
			switch((Integer) newValue){
				case 0:
					verdi = "0";
				case 1:
					verdi = "null";
				case 2:
					verdi = "1";
			}
			oppmoteListe.put(theUser.getEmail(), verdi);
		}
	};
	
	@FXML
	public void avbryt(){
		ScreenNavigator.loadScreen(ScreenNavigator.lastScreen);
	}
	
	
		
}


