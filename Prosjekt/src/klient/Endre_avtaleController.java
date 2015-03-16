package klient;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
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
import javafx.event.EventType;
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
	private ArrayList<Bruker> ledigeBrukere;
	private int startT;
	private int startM;
	private int sluttT;
	private int sluttM;
	private LocalTime evigheten;
	private Avtale avtalen;
	private int indexen;
	private HashMap<String,String> oppmoteListe;
	private HashMap<HBox,String> boksBruker;
	private String avtaleid;
	private String choiceBruker;
	private boolean fysteGongen;
	
	
//LISTENE SOM INNEHOLDER GJESTER OG GRUPPER (IKKE TIL LISTVIEW, TIL COMBOBOX)
	private ObservableList<Bruker> gjeste_ComboBox;
	private ObservableList<Gruppe> gruppe_ComboBox;
		
//LISTER OVER INVITERTE BRUKERE OG GRUPPER (UAVHENGIGE LISTER)
	private ArrayList<Bruker> inviterte_gjester;
	private ArrayList<Gruppe> inviterte_grupper;
	private List<Bruker> listeForGjesteCombobox;
	private List<Gruppe> listeForGruppeCombobox;
	
//LEDIGE BRUKERE OG GRUPPER
	private String[] ledigeBrukerEmailer;
    private String[] ledigeGrupperId;
	
//LISTENER TIL LISTVIEW SINE GJELDENDE VALG (MULIG IKKE FUNGERER)
	private Bruker valg;
    private Gruppe valgGruppe;
    private Bruker forrigeValg;
	
	
//LISTER OG OBSERVABLELISTER TIL LISTVIEW
	private List<HBox> gjeste_listen;
    private List<HBox> gruppe_listen;
    private ObservableList<HBox> gjestene_observable;
    private ObservableList<HBox> gruppene_observable;
	
	@FXML
	CheckBox hele_dagen;
	@FXML
	Text feilDato, feilTekst;
    @FXML
    DatePicker startdato;
    @FXML
    ChoiceBox<String> timeFra, timeTil, minuttFra, minuttTil;
    @FXML
    ComboBox<Bruker> legg_til_gjester;
    @FXML
    ComboBox<Gruppe> legg_til_gruppe = new ComboBox<Gruppe>();
    @FXML
    ListView<HBox> gjesteliste, gruppeliste;
    @FXML
    ListView<String> møteromliste;
    @FXML
    Slider møteromliste_slider;
    @FXML
    TextField antall_gjester,valgt_rom, avtalenavn;
    @FXML
    Button addGruppeBtn, lagre_knapp, slett_knapp, søk_møterom, addGjestBtn, leggTil;
    
    
    List<HBox> gjestelisten;
    ObservableList<HBox> gjestene;
	
	
	public void initialize() throws IOException {
		//INSTATNSIERER SELVE LISTENE SOM INNEHOLDER GRUPPER/BRUKERE TIL COMBOBOXENE
		listeForGjesteCombobox = new ArrayList<Bruker>();
		listeForGruppeCombobox = new ArrayList<Gruppe>();
		inviterte_gjester = new ArrayList<Bruker>();
		inviterte_grupper = new ArrayList<Gruppe>();
		gjeste_liste = new ArrayList<Bruker>();
		gjestelisten = new ArrayList<HBox>();
    	gjestene = FXCollections.observableList(gjestelisten);
    	ledigeBrukerEmailer = Klienten.getAllUserEmails();
		ledigeBrukere = new ArrayList<Bruker>();
		oppmoteListe = new HashMap<String,String>();
		boksBruker = new HashMap<HBox,String>();
		for(String email : ledigeBrukerEmailer){
			if(email != null || email != ""){
				ledigeBrukere.add(new Bruker(Klienten.getBruker(email), email,0));
			}
		}
		
		legg_til_gjester.getItems().addAll(ledigeBrukere);
		antall_gjester.setDisable(false);
		antall_gjester.setEditable(true);
		ant_gjester.setValue(0);
		ChangeListener<Number> antall = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if (ant_gjester.getValue().intValue() < inviterte_gjester.size()) {
					ant_gjester.setValue(inviterte_gjester.size());
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
					if (str < inviterte_gjester.size()) {
						antall_gjester.setText(inviterte_gjester.size()+"");
						antall_gjester.end();
					}
				}
				catch (Exception e) {
					antall_gjester.setText(inviterte_gjester.size()+"");
					antall_gjester.end();
				}
			}
			
		};
		antall_gjester.textProperty().addListener(tekstStr);
		ant_gjester.addListener(antall);
				
	//INSTANSIERER HBOX-LISTENE OG LEGGER DE TIL I OBSERVABLELISTENE
				
		gjeste_listen = new ArrayList<HBox>();
		gruppe_listen = new ArrayList<HBox>();
			
		gjestene_observable = FXCollections.observableList(gjeste_listen);
	   	gruppene_observable = FXCollections.observableList(gruppe_listen);
				
				
	//HENTER GRUPPER OG LEGGER TIL GRUPPER OG BRUKERE I LISTVIEWENE
				
		ledigeBrukerEmailer = Klienten.getAllUserEmails();
		
		for(String email : ledigeBrukerEmailer){
			if(email != null && email != "" && ! email.trim().equals(Klienten.bruker.getEmail())){
				listeForGjesteCombobox.add(new Bruker(Klienten.getBruker(email), email, 0));
			}
			else {
				System.out.println(email);
			}
		}
		
		ledigeGrupperId = Klienten.getGroups().split(" ");
		
		//LEGGER GRUPPER TIL I GRUPPE_COMBOBOX
		lagGrupper();
		
		gjeste_ComboBox = FXCollections.observableList(listeForGjesteCombobox);
		gruppe_ComboBox = FXCollections.observableList(listeForGruppeCombobox);
		legg_til_gjester.setItems(gjeste_ComboBox);
		legg_til_gruppe.setItems(gruppe_ComboBox);
		
		
	//LEGGER TIL LISTENERS FOR LEGG TIL GRUPPER/BRUKER
		
		ChangeListener<Bruker> valgt_bruker = new ChangeListener<Bruker>() {
				@Override
			public void changed(ObservableValue<? extends Bruker> arg0,
					Bruker arg1, Bruker arg2) {
				valg = FxUtil.getComboBoxValue(legg_til_gjester);
			}
			
		};
		legg_til_gjester.getSelectionModel().selectedItemProperty().addListener(valgt_bruker);
		
		ChangeListener<Gruppe> valgt_gruppe = new ChangeListener<Gruppe>() {
				@Override
			public void changed(ObservableValue<? extends Gruppe> arg0,
					Gruppe arg1, Gruppe arg2) {
				valgGruppe = FxUtil.getComboBoxValue(legg_til_gruppe);
			}
			
		};
		legg_til_gruppe.getSelectionModel().selectedItemProperty().addListener(valgt_gruppe);
		
	//LAGER COMBOBOXENE SØKBARE
		FxUtil.autoCompleteComboBox(legg_til_gjester, FxUtil.AutoCompleteMode.CONTAINING);
		ChangeListener<Bruker> valgt_brukeren = new ChangeListener<Bruker>() {
				@Override
			public void changed(ObservableValue<? extends Bruker> arg0,
					Bruker arg1, Bruker arg2) {
				valg = FxUtil.getComboBoxValue(legg_til_gjester);
			}
			
		};
		legg_til_gjester.getSelectionModel().selectedItemProperty().addListener(valgt_brukeren);
		
		ChangeListener<Gruppe> valgt_gruppen = new ChangeListener<Gruppe>() {
				@Override
			public void changed(ObservableValue<? extends Gruppe> arg0,
					Gruppe arg1, Gruppe arg2) {
				valgGruppe = FxUtil.getComboBoxValue(legg_til_gruppe);
			}
			
		};
		legg_til_gruppe.getSelectionModel().selectedItemProperty().addListener(valgt_gruppen);
		
	//LAGER COMBOBOXENE SØKBARE
		
		FxUtil.autoCompleteComboBox(legg_til_gjester, FxUtil.AutoCompleteMode.CONTAINING);
		FxUtil.autoCompleteComboBox(legg_til_gruppe, FxUtil.AutoCompleteMode.CONTAINING);
		
		insertAppDetails();
		
		System.out.println(oppmoteListe);
		
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
		for(Avtale app : Klienten.avtaler){
			if(Klienten.getValgtAvtale().equals(app.getAvtaleid())){
				avtalen = app;
				avtaleid = app.getAvtaleid();
				break;
			}
		}
		avtalenavn.setText(avtalen.getAvtaleNavn());
		startdato.setValue(avtalen.getTid().getDato());
		dato = startdato.getValue();
		timeFra.setValue(timeStringFormat(String.valueOf(avtalen.getTid().getStart().getHour())));
		timeTil.setValue(timeStringFormat(String.valueOf(avtalen.getTid().getSlutt().getHour())));
		minuttFra.setValue(timeStringFormat(String.valueOf(avtalen.getTid().getStart().getMinute())));
		if (avtalen.getTid().getSlutt().getMinute() == 59) {
			minuttTil.setValue(timeStringFormat(String.valueOf((55))));
		}
		else {
			minuttTil.setValue(timeStringFormat(String.valueOf(avtalen.getTid().getSlutt().getMinute())));
		}
		valgt_rom.setText(avtalen.getRom().getNavn());
		valg = new Bruker();
		fysteGongen = true;
		System.out.println("DELTAKERE: " + avtalen.getDeltakere());
		for(Bruker d : avtalen.getDeltakere()){
			for(Bruker b : listeForGjesteCombobox){
				if(b.getEmail().equals(d.getEmail())){
					addGjestenEgentlig(b);
					gjeste_liste.add(b);
					break;
				}
			}
		}
		fysteGongen = false;
		valg = null;
		antall_gjester.setText(String.valueOf((int)inviterte_gjester.size()/2));
		
    }

	private String timeStringFormat(String s){
    	if(s.length()==1){
    		return "0"+s;
    	}
    	return s;
    }


	public void showRom(ArrayList<String> rommene) {
    	møteromliste.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(ObservableValue<? extends String> observable,
    	            String oldValue, String newValue) {
    	    	valgt_rom.setText(newValue);
    	    }
    	});
    	ObservableList<String> rom = FXCollections.observableList(rommene);
    	møteromliste.setItems(rom);
    }
    
    @FXML
    public void slett() throws IOException {
    	Klienten.deleteAvtale(avtalen.getAvtaleid());
    	System.out.println("FØR: "+ Klienten.avtaler);
    	for (Avtale avt : Klienten.avtaler) {
    		if (avt.getAvtaleid().equals(avtaleid)) {
    			Klienten.avtaler.remove(avt);
    			break;
    		}
    	}
    	System.out.println("ETTER: " + Klienten.avtaler);
    	ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());
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
					System.out.println("ÆÆÆÆÆÆÆÆÆ");
				}
			}
    	};
    	startdato.valueProperty().addListener(datoEndring);
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
    
    //LEGGER TIL HBOX I LISTVIEW FOR GJESTER
    public void showGjest(HBox boks) {
    	gjestene_observable.add(boks);
    	gjesteliste.setItems(gjestene_observable);
    	ant_gjester.setValue(ant_gjester.getValue().intValue()+1);
    }
    
    //FJERNER ÉN GJEST FRA LISTVIEW MED GJESTER
    public void removeGjest(HBox boks) {
    	for (Bruker bruker : inviterte_gjester) {
    		Text te = (Text) boks.getChildren().get(0);
    		if (bruker.getNavn().equals(te.getText())) {
    			inviterte_gjester.remove(bruker);
    			gjeste_ComboBox.add(bruker);
    			legg_til_gjester.setItems(gjeste_ComboBox);
    			gjestene_observable.remove(boks);
    	    	gjesteliste.setItems(gjestene_observable);
    	    	ant_gjester.setValue(ant_gjester.getValue().intValue()-1);
    	    	antall_gjester.setText(ant_gjester.getValue().intValue() + "");
    	    	return;
    		}
    		else {
    		}
    	}
    }
    
    
    //LEGGER TIL ÉN GJEST (FRA GRUPPE!) OG KALLER SHOWGJEST MED BOKSEN
    public void addGjest(Bruker bruker) throws IOException {
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
    	String brukernavn = bruker.getNavn();
    	boks.getChildren().add(new Text(brukernavn));
    	boks.getChildren().add(kryss);
    	showGjest(boks);
    	inviterte_gjester.add(bruker);
    	gjeste_ComboBox.remove(bruker);
    	legg_til_gjester.setItems(gjeste_ComboBox);
    }
    
    
    //LEGGER TIL ÉN GJEST (VED Å OPPRETTE HBOX OBJEKT) OG KALLER SHOWGJEST MED BOKSEN
    @FXML
    private void addGjesten(ActionEvent event) throws IOException {
    	Bruker selectedUser = FxUtil.getComboBoxValue(legg_til_gjester);
    	addGjestenEgentlig(selectedUser);
    }
    
    private void addGjestenEgentlig(Bruker selectedUser) throws IOException{
    	if (valg != null && selectedUser != null) {
    		valg = selectedUser;
	    	HBox boks = new HBox();
	    	boksBruker.put(boks, selectedUser.getEmail());
	    	Button kryss = new Button();
	    	kryss.setText("x");
	    	kryss.setOpacity(0.8);
	    	kryss.setOnAction(new EventHandler<ActionEvent>() {
	
				@Override
				public void handle(ActionEvent event) {
					removeGjest(boks);
				}
	    		
	    	});
	    	String brukernavn = valg.getNavn();
	    	boks.getChildren().add(new Text(brukernavn));
	    	ChoiceBox<String> oppmote = new ChoiceBox<String>();
	    	ArrayList<String> valget = new ArrayList<String>();
	    	valget.add("Skal");
	    	valget.add("Ikke svart");
	    	valget.add("Skal ikke");
	    	oppmote.setItems(FXCollections.observableList(valget));
	    	oppmote.setPrefHeight(boks.getPrefHeight());
	    	oppmote.getSelectionModel().selectedIndexProperty().addListener(handleOppmote);
	    	oppmote.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
	    	String status = "0";
	    	if(fysteGongen){
	    		status = Klienten.getStatus(avtalen.getAvtaleid(), valg.getEmail());
	    		status = correction(status);
	    		System.out.println("FYSTEGONGEN FOR "+valg.getEmail()+" "+status);
	    	}
	    	oppmote.setValue(getStatus(status));
	    	oppmoteListe.put(valg.getEmail(), correction(status));
	    	boks.getChildren().add(oppmote);
	    	boks.getChildren().add(kryss);
	    	showGjest(boks);
	    	inviterte_gjester.add(valg);
	    	//gjeste_ComboBox.remove(valg);
	    	legg_til_gjester.setItems(gjeste_ComboBox);
	    	forrigeValg = valg;
    	}
    	else {
    	}
    }
	
	private String correction(String status) {
		String s = "";
		if(status.trim().equals("0")){
			s =  "1";
		}
		else if(status.trim().equals("1")){
			s = "0";
		}
		else{
			s = "null";
		}
		return s;
	}


	private String getStatus(String string) {
		switch(string.trim()){
		case "0":
			return "Skal";
		case "null":
			return "Ikke svart";
		case "1":
			return "Skal ikke";
		}
		return "Skal ikke";
	}


	public void removeGruppe(HBox boks) {
		for (Gruppe gruppe : inviterte_grupper) {
    		Text te = (Text) boks.getChildren().get(0);
    		if (gruppe.getNavn().equals(te.getText())) {
    			for (Bruker bruker : gruppe.getMedlemmer()) {
    				try {
	    				for (HBox gjest_boks : gjestene_observable) {
	    					Text text = (Text) gjest_boks.getChildren().get(0);
	    					if (text.getText().equals(bruker.getNavn())) {
	    						removeGjest(gjest_boks);
	    					}
	    				}
    				}
    				catch (ConcurrentModificationException e) {
    					
    				}
    			}
    			inviterte_grupper.remove(gruppe);
    			gruppe_ComboBox.add(gruppe);
    			legg_til_gruppe.setItems(gruppe_ComboBox);
    			gruppene_observable.remove(boks);
    	    	gruppeliste.setItems(gruppene_observable);
    			return;
    		}
    		else {
    		}
    	}
    	
	}
	
	
	//LEGGER TIL VALGT GRUPPE (FRA COMBOBOX) OG KALLER SHOWGRUPPE
	@FXML
	private void addGruppen() throws IOException, ClassCastException {
		if (valgGruppe != null) {
			if (FxUtil.getComboBoxValue(legg_til_gruppe) != null && ! valgGruppe.equals(FxUtil.getComboBoxValue(legg_til_gruppe))) {
				valgGruppe = FxUtil.getComboBoxValue(legg_til_gruppe);
			}
	    	HBox boks = new HBox();
	    	Button kryss = new Button();
	    	kryss.setText("x");
	    	kryss.setOpacity(0.8);
	    	kryss.setOnAction(new EventHandler<ActionEvent>() {
	
				@Override
				public void handle(ActionEvent event) {
					removeGruppe(boks);
				}
	    		
	    	});
	    	String gruppenavn = valgGruppe.getNavn();
	    	boks.getChildren().add(new Text(gruppenavn));
	    	boks.getChildren().add(kryss);
	    	showGrupper(boks, valgGruppe);
	    	inviterte_grupper.add(valgGruppe);
	    	gruppe_ComboBox.remove(valgGruppe);
	    	if (gruppe_ComboBox.isEmpty()) {
	    		valgGruppe = null;
	    	}
	    	legg_til_gruppe.setItems(gruppe_ComboBox);
    	}
	}
	
	
	//OPPRETTER GRUPPER TIL LISTVIEW FOR GRUPPER
	private void lagGrupper() throws NumberFormatException, IOException {
		ArrayList<Bruker> brukere = new ArrayList<Bruker>();
		if (ledigeGrupperId != null) {
			for (String id : ledigeGrupperId) {
				String gruppenavn = Klienten.getGroupName(id.trim());
				if (gruppenavn.trim().equals("NONE")) {
					break;
				}
				else {	
					brukere = Klienten.getGroupMembers(Integer.parseInt(id.trim()));
					Gruppe gruppe = new Gruppe(gruppenavn.trim(), brukere);
					listeForGruppeCombobox.add(gruppe);
				}
			}
		}
	}
	
	
	//LEGGER BOKS TIL I LISTVIEW FOR GRUPPER, OG GRUPPEMEDLEMMER I LISTVIEW FOR GJESTER
	private void showGrupper(HBox boks, Gruppe gruppe) throws IOException {
		gruppene_observable.add(boks);
		gruppeliste.setItems(gruppene_observable);
    	for (Bruker bruker : gruppe.getMedlemmer()) {
    		for (Bruker bruker_compare : gjeste_ComboBox) {
    			if (bruker.getEmail().equals(bruker_compare.getEmail())) {
    				addGjest(bruker_compare);
    				break;
    			}
    		}
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
	    	rom = Klienten.getRom(dato.toString(), start.toString(), slutt.toString(), inviterte_gjester.size() + "");
	    	ArrayList<String> rommene = new ArrayList<String>();
		    String[] rom_listen = rom.split(" ");
		    for (int i = 0; i < rom_listen.length; i++) {
		    	rommene.add(rom_listen[i]);
			}
		    showRom(rommene);
    	}
    	catch (NullPointerException e) {
    		System.out.println("Må fylle ut avgjørende felter");
    	}
    	System.out.println("ROMMENE: " + rom);
    	ArrayList<String> rommene = new ArrayList<String>();
    	String[] rom_listen = rom.split(" ");
    	for (int i = 0; i < rom_listen.length; i++) {
    		rommene.add(rom_listen[i]);
		}
    	//Må spørre database om ledig(e) rom, midlertidig metode:
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
		if (! feilTekst.isVisible() && avtalenavn.getText() != null){
			handleChanges();
			for (Avtale avtale : Klienten.avtaler) {
				if (avtale.getAvtaleid().equals(avtaleid)) {
					Klienten.avtaler.remove(avtale);
					Klienten.avtaler.add(avtalen);
					break;
				}
			}
			ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());
		}
	}
    
    private void handleChanges() throws IOException{
    	boolean nyRom,nyStart,nySlutt,nyDato;
		Møterom rom = new Møterom(sluttM, "he");
		if(!avtalenavn.getText().equals(avtalen.getAvtaleNavn())){
			Klienten.changeAvtale(avtalen.getAvtaleid(), avtalenavn.getText(), "APPNAME");
			avtalen.setAvtaleNavn(avtalenavn.getText());
		}
    	if(!valgt_rom.getText().equals(avtalen.getRom().getNavn())){
			Klienten.changeAvtale(avtalen.getAvtaleid(), valgt_rom.getText(),"ROOM");
			//avtalen.setRom(Klienten.møterom);
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
		Klienten.changeOppmote(avtaleid, oppmoteListe);
		ArrayList<Bruker> nye_gjester = new ArrayList<Bruker>();
		ArrayList<Bruker> fjerna_gjester = new ArrayList<Bruker>();
		if(!inviterte_gjester.equals(avtalen.getDeltakere())){
			boolean ny;
			for(Bruker b : inviterte_gjester){
				ny = true;
				for(Bruker n : avtalen.getDeltakere()){
					if(b.getEmail().equals(n.getEmail())){
						ny = false;
						break;
					}
				}
				if(ny) {
					nye_gjester.add(b);
					b.inviterTilNyAvtale(avtalen);
					avtalen.addDeltakere(b);
				}
			}
			boolean fjernet;
			for (int i = 0; i < avtalen.getDeltakere().size(); i++) {
				Bruker b;
				if (avtalen.getDeltakere().size() + 1 > i) {
					b = avtalen.getDeltakere().get(i);
					if (b.equals(Klienten.bruker)) {
						continue;
					}
				}
				else {
					break;
				}
				fjernet = true;
				for (int j = 0; j < inviterte_gjester.size(); j++) {
					Bruker n;
					if (inviterte_gjester.size() > j) {
						n = inviterte_gjester.get(j);
					}
					else {
						break;
					}
					if(b.getEmail().equals(n.getEmail())){
						fjernet = false;
						break;
					}
				}
				if(fjernet){
					fjerna_gjester.add(b);
				}
			}
		}
		System.out.println("FJERNA : " + fjerna_gjester);
		for (Bruker b : fjerna_gjester) {
			b.deleteAvtale(avtalen);
			System.out.println("DELETED");
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
			if(((Integer) newValue).equals((Integer) 0)){
				verdi = "1";
			}
			else if(((Integer) newValue).equals((Integer) 1)){
				verdi = "null";
			}
			else{
				verdi = "0";
			}
			oppmoteListe.put(choiceBruker, verdi);
		}
	};
	
	EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
	    public void handle(MouseEvent event) {
	        ChoiceBox<String> boksen = (ChoiceBox<String>) event.getSource();
	        HBox boks = (HBox) boksen.getParent();
	        choiceBruker = boksBruker.get(boks);
	    }
	};
	
	@FXML
	public void avbryt(){
		ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());
	}
}


