package klient;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.controlsfx.control.CheckComboBox;

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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ny_avtale_controller {
	
	
	Property<Number> ant_gjester = new SimpleIntegerProperty();
	
	//LISTENE SOM INNEHOLDER GJESTER OG GRUPPER (IKKE TIL LISTVIEW, TIL COMBOBOX)
	private ObservableList<Bruker> gjeste_ComboBox;
	private ObservableList<Gruppe> gruppe_ComboBox;
	
	//LISTER OVER INVITERTE BRUKERE OG GRUPPER (UAVHENGIGE LISTER)
	private ArrayList<Bruker> inviterte_gjester;
	private ArrayList<Gruppe> inviterte_grupper;
	private List<Bruker> listeForGjesteCombobox;
	private List<Gruppe> listeForGruppeCombobox;

	//TID OG DATO
	private LocalTime start;
	private LocalTime slutt;
	private LocalDate dato;
	private LocalDate tilDato;
	private int startT;
	private int startM;
	private int sluttT;
	private int sluttM;
	private LocalTime evigheten;
	private ArrayList<String> repDays;
	private ArrayList<LocalDate> repDates;
	
	//LEDIGE BRUKERE OG GRUPPER
	private String[] ledigeBrukerEmailer;
    private String[] ledigeGrupperId;
	
	//LISTENER TIL LISTVIEW SINE GJELDENDE VALG (MULIG IKKE FUNGERER)
	private Bruker valg;
    private Gruppe valgGruppe;
	
	
	//LISTER OG OBSERVABLELISTER TIL LISTVIEW
	private List<HBox> gjeste_listen;
    private List<HBox> gruppe_listen;
    private ObservableList<HBox> gjestene_observable;
    private ObservableList<HBox> gruppene_observable;
	
	//REPETISJON
	private ArrayList<String> repRooms;
	private boolean isrep;
	
	@FXML
	CheckBox hele_dagen, gjentakelse;
	@FXML
	Text feilDato, feilTekst;
    @FXML
    DatePicker startdato, sluttdato;
    @FXML
    ChoiceBox<String> timeFra, timeTil, minuttFra, minuttTil;
    @FXML
    ComboBox<Bruker> legg_til_gjester;
    @FXML
    ListView<HBox> gjesteliste, gruppeliste;
    @FXML
    Button søk_møterom, forkast_knapp, lagre_knapp, addGruppeBtn, leggTil, addGjestBtn;
    @FXML
    ListView<String> møteromliste;
    @FXML
    Slider møteromliste_slider;
    @FXML
    TextField antall_gjester, valgt_rom, avtalenavn;
    @FXML
    CheckComboBox<String> repeatDays;
    @FXML
    ComboBox<Gruppe> legg_til_gruppe;
    
	
	public void initialize() throws IOException {
		
		
	//INITIALISERER LISTER OVER INVITERTE GRUPPER/BRUKERE
		
		inviterte_gjester = new ArrayList<Bruker>();
		inviterte_grupper = new ArrayList<Gruppe>();
		
	//MYE RART HER
		
		repDates = new ArrayList<LocalDate>();
		isrep = false;
		sluttdato.setDisable(true);
		antall_gjester.setDisable(false);
		antall_gjester.setEditable(true);
		ant_gjester.setValue(0);
		
		
	//LEGGER TIL DIVERSE LISTENERS
		
		ChangeListener<Number> antall = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if (ant_gjester.getValue().intValue() < gjestene_observable.size()) {
					ant_gjester.setValue(gjestene_observable.size());
				}
				antall_gjester.setText(ant_gjester.getValue().toString());
			}
			
		};
		
		ant_gjester.addListener(antall);
		
		ChangeListener<String> tekstStr = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				try {
					int str = Integer.parseInt(antall_gjester.getText());
					if (str < gjestene_observable.size()) {
						antall_gjester.setText(gjestene_observable.size()+"");
						antall_gjester.end();
					}
				}
				catch (Exception e) {
					antall_gjester.setText(gjestene_observable.size()+"");
					antall_gjester.end();
				}
			}
			
		};
		antall_gjester.textProperty().addListener(tekstStr);
		
		sluttdato.setDisable(true);
		ChangeListener<Boolean> aktiver = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (gjentakelse.isSelected()) {
					sluttdato.setDisable(false);
					repeatDays.disableProperty().set(false);
					isrep = true;
				}
				else {
					sluttdato.setValue(null);
					slutt = null;
					sluttdato.setDisable(true);
					repeatDays.disableProperty().set(true);
					isrep = false;
				}
			}
			
		};
		gjentakelse.selectedProperty().addListener(aktiver);
		
		
	//INSTATNSIERER SELVE LISTENE SOM INNEHOLDER GRUPPER/BRUKERE TIL COMBOBOXENE
		
		listeForGjesteCombobox = new ArrayList<Bruker>();
		listeForGruppeCombobox = new ArrayList<Gruppe>();
		
		
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
		FxUtil.autoCompleteComboBox(legg_til_gruppe, FxUtil.AutoCompleteMode.CONTAINING);
		
	//SETTER OPP CHECKCOMBOBOXEN
		setUpCCB();
		
		
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
					timeFra.setValue(null);
					timeTil.setValue(null);
					minuttFra.setValue(null);
					minuttTil.setValue(null);
					start = LocalTime.of(0, 0);
					slutt = LocalTime.of(23, 59);
					sluttdato.setValue(null);
					sluttdato.setDisable(true);
				}
				else {
					timeFra.setDisable(false);
					timeTil.setDisable(false);
					minuttFra.setDisable(false);
					minuttTil.setDisable(false);
					sluttdato.setDisable(false);
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
		timeFra.setValue("00");
		timeTil.setValue("00");
		minuttFra.setValue("00");
		minuttTil.setValue("00");
		timeFra.getSelectionModel().selectedIndexProperty().addListener(handleFraTime);
		timeTil.getSelectionModel().selectedIndexProperty().addListener(handleTilTime);
		minuttFra.getSelectionModel().selectedIndexProperty().addListener(handleFraMinutt);
		minuttTil.getSelectionModel().selectedIndexProperty().addListener(handleTilMinutt);
		startT = 0;
		startM = 0;
		sluttT = 0;
		sluttM = 0;
		evigheten = LocalTime.of(00, 00);
		start = evigheten;
		slutt = evigheten;
		repDays = new ArrayList<String>();
	}
    
    
    private void setUpCCB() {
		final ObservableList<String> strings = FXCollections.observableArrayList();
		strings.add("Mandag");
		strings.add("Tirsdag");
		strings.add("Onsdag");
		strings.add("Torsdag");
		strings.add("Fredag");
		strings.add("Lørdag");
		strings.add("Søndag");
		repeatDays.getItems().addAll(strings);
		repeatDays.getCheckModel().getCheckedItems().addListener(handleRepDay);
		repeatDays.disableProperty().set(true);
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
    public void reset() {
    	ScreenNavigator.loadScreen(ScreenNavigator.AVTALE);
    }
    
    @FXML
    public void handleDato() {
    	ChangeListener<LocalDate> datoEndring = new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0,
					LocalDate arg1, LocalDate arg2) {
				if (sjekkDato(startdato.getValue(), sluttdato.getValue())) {
					dato = startdato.getValue();
					findDates();
				}
				else {
					repDates.add(startdato.getValue());
				}
			}
    	};
    	startdato.valueProperty().addListener(datoEndring);
    	sluttdato.valueProperty().addListener(datoEndring);
    }
    
    public boolean sjekkDato(LocalDate start, LocalDate slutt) {
    	try {
    		if (gjentakelse.isSelected()) {
    			if (startdato.getValue().isBefore(sluttdato.getValue())) {
    				feilDato.setVisible(false);
    				dato = start;
    				tilDato = slutt;
    				return true;
    			}
    			else {
    				sluttdato.setValue(startdato.getValue());
    				feilDato.setVisible(true);
    				return false;
    			}
    		}
    		else {
    			dato = start;
    			return false;
    		}
    	}
    	catch (NullPointerException e) {
    		feilDato.setVisible(false);
    		return false;
    	}
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
    	case "Lørdag":
    		return DayOfWeek.SATURDAY;
    	case "Søndag":
    		return DayOfWeek.SUNDAY;
    	}
    	return null;
    }
    
    private void findDates() {
    	LocalDate i = dato;
    	repDates = new ArrayList<LocalDate>();
    	repDates.add(i);
    	for(String day : repDays){
    		if(findWeekDay(day).getValue()-i.getDayOfWeek().getValue()>0){
    			int diff = findWeekDay(day).getValue()-i.getDayOfWeek().getValue();
    			i = i.plusDays(diff);
    			repDates.add(i);
    		}
    	}
    	while(i.isBefore(tilDato)) {
    		for(String day : repDays){
    			DayOfWeek dag = findWeekDay(day);
    			System.out.println(dag);
    			int diff = dag.getValue()-i.getDayOfWeek().getValue();
    			if(diff<=0){
    				diff += 7;
    			}
    			i = i.plusDays(diff);
    			repDates.add(i);
    		}
    	}
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
	
	ListChangeListener<? super String> handleRepDay = new ListChangeListener<String>() {
		@Override
		public void onChanged(ListChangeListener.Change<? extends String> c) {
			repDays.clear();
			repDays.addAll(repeatDays.getCheckModel().getCheckedItems());
			findDates();
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
	    	if (isrep){
	    		repRooms = new ArrayList<String>();
	    		for(LocalDate date : repDates){
	    			String[] besteRom = Klienten.getRom(date.toString(), start.toString(), slutt.toString(), gjeste_ComboBox.size() + "").split(" ");
	    			repRooms.add(besteRom[0]);
	    		}
	    		ArrayList<String> beskjed = new ArrayList<String>();
	    		beskjed.add("Beste rom har blitt automatisk valgt");
	    		showRom(beskjed);
	    		return;
	    	}
	    	else {
	    		rom = Klienten.getRom(dato.toString(), start.toString(), slutt.toString(), gjeste_ComboBox.size() + "");
	    		ArrayList<String> rommene = new ArrayList<String>();
		    	String[] rom_listen = rom.split(" ");
		    	for (int i = 0; i < rom_listen.length; i++) {
		    		rommene.add(rom_listen[i]);
				}
		    	showRom(rommene);
	    	}
    	}
    	catch (NullPointerException e) {
    		System.out.println("Må fylle ut avgjørende felter");
    	}
    	ArrayList<String> rommene = new ArrayList<String>();
    	String[] rom_listen = rom.split(" ");
    	for (int i = 0; i < rom_listen.length; i++) {
    		rommene.add(rom_listen[i]);
		}
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
		Møterom rom =  new Møterom(100, valgt_rom.getText());
		if (startdato.getValue() != null && repDates.isEmpty()) {
			repDates.add(startdato.getValue());
		}
		for (LocalDate datoen : repDates) {
			if (! feilTekst.isVisible() && ! feilDato.isVisible() && avtalenavn.getText() != null) {
				if (isrep) {
					int i = repDates.indexOf(datoen);
					rom = new Møterom(100, repRooms.get(i));
				}
				String avtaleid = Klienten.lagAvtale(new TidsIntervall(start, slutt, datoen), rom, avtalenavn.getText());
				Avtale avtale = new Avtale(getBruker(), inviterte_gjester, new TidsIntervall(start, slutt, datoen), rom, avtaleid);
				for (Bruker deltaker : inviterte_gjester) {
					deltaker.inviterTilNyAvtale(avtale); 
				}
				getBruker().inviterTilNyAvtale(avtale);
				Klienten.changeStatus(avtaleid, "1");
				for (Dag dag : KalenderController.dager) {
					if (dag.getDato().equals(datoen)) {
						dag.addAvtale(avtale);
					}
				}
				Klienten.setChanged(true);
				ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());
			}
		}
	}
	
	public Bruker getBruker() {
		return Klienten.bruker;
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
    private void addGjesten(ActionEvent event) {
    	if (valg != null && FxUtil.getComboBoxValue(legg_til_gjester) != null) {
    		valg = FxUtil.getComboBoxValue(legg_til_gjester);
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
	    	String brukernavn = valg.getNavn();
	    	boks.getChildren().add(new Text(brukernavn));
	    	boks.getChildren().add(kryss);
	    	showGjest(boks);
	    	inviterte_gjester.add(valg);
	    	gjeste_ComboBox.remove(valg);
	    	legg_til_gjester.setItems(gjeste_ComboBox);
    	}
    	else {
    	}
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
	
	@FXML
	public void avbryt(){
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
	
	
		
}


