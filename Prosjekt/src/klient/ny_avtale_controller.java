package klient;

import java.io.IOException;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class ny_avtale_controller {
	
	private ArrayList<Møterom> alle_møterom;
	private ArrayList<Bruker> gjeste_liste;
	private LocalTime start;
	private LocalTime slutt;
	private LocalDate dato;
	
	@FXML
	CheckBox hele_dagen = new CheckBox();
	@FXML
	Button leggTil = new Button();
	@FXML
	Text feilDato = new Text();
	@FXML
	CheckBox gjentakelse = new CheckBox();
	@FXML
    TextField avtalenavn = new TextField();
	@FXML
    Text feilTekst = new Text();
    @FXML
    DatePicker startdato = new DatePicker();
    @FXML
    DatePicker sluttdato = new DatePicker();
    @FXML
    ChoiceBox<String> timeFra = new ChoiceBox<String>();
    @FXML
    ChoiceBox<String> timeTil = new ChoiceBox<String>();
    @FXML
    ChoiceBox<String> minuttFra = new ChoiceBox<String>();
    @FXML
    ChoiceBox<String> minuttTil = new ChoiceBox<String>();
    @FXML
    TextField legg_til_gjester = new TextField();
    @FXML
    ListView<String> gjesteliste = new ListView<String>();
    @FXML
    Button søk_møterom = new Button();
    @FXML
    ListView<String> møteromliste = new ListView<String>();
    @FXML
    Slider møteromliste_slider = new Slider();
    @FXML
    TextField antall_gjester = new TextField();
    @FXML
    TextField valgt_rom = new TextField();
    @FXML
    Button forkast_knapp = new Button();
    @FXML
    Button lagre_knapp = new Button();
    List<String> gjestelisten;
    ObservableList<String> gjestene;
	
	public void initialize() {
		sluttdato.setDisable(true);
		ChangeListener<Boolean> aktiver = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (gjentakelse.isSelected()) {
					sluttdato.setDisable(false);
				}
				else {
					sluttdato.setValue(null);
					slutt = null;
					sluttdato.setDisable(true);
				}
			}
			
		};
		gjentakelse.selectedProperty().addListener(aktiver);
		
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
		
		
		
		gjeste_liste = new ArrayList<Bruker>();
		gjestelisten = new ArrayList<String>();
    	gjestene = FXCollections.observableList(gjestelisten);
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
		System.out.println(timer);
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
		System.out.println(minutter);
		minuttFra.setItems(minutter);
		System.out.println(minuttFra.getItems());
		minuttTil.setItems(minutter);
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
    
    public void reset() {
    	ScreenNavigator.loadScreen(ScreenNavigator.AVTALE);
    }
    
    public void handleDato() {
    	ChangeListener<LocalDate> datoEndring = new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0,
					LocalDate arg1, LocalDate arg2) {
				if (sjekkDato(startdato.getValue(), sluttdato.getValue())) {
					dato = startdato.getValue();
				}
				else {
					
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
    		}
    	}
    	catch (NullPointerException e) {
    		feilDato.setVisible(false);
    		return false;
    	}
    	return false;
    }
    
    //Holder kontroll på tidene og at de er etter hverandre
    public void handleTid() {
    	ChangeListener<String> tider = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				try {
					slutt = LocalTime.of(Integer.parseInt(timeTil.getValue().toString()),
							Integer.parseInt(minuttTil.getValue().toString()));
			    	start = LocalTime.of(Integer.parseInt(timeFra.getValue().toString()),
			    			Integer.parseInt(minuttFra.getValue().toString()));
					if (sjekkTid(start, slutt)) {
						feilTekst.setVisible(false);
					}
					else {
						feilTekst.setVisible(true);
					}
				}
				catch (NullPointerException e) {
				}
			}
    	};
    	minuttTil.valueProperty().addListener(tider);
    	minuttFra.valueProperty().addListener(tider);
    	timeTil.valueProperty().addListener(tider);
    	timeFra.valueProperty().addListener(tider);
    }
    
    public boolean sjekkTid(LocalTime start, LocalTime slutt) {
    	if (start.isBefore(slutt)) {
    		return true;
    	}
    	else {
    		timeTil.setValue(timeFra.getValue());
    		minuttTil.setValue(minuttFra.getValue());
    		slutt = LocalTime.of(Integer.parseInt(timeTil.getValue().toString()),
    				Integer.parseInt(minuttTil.getValue().toString()));
    		return false;
    		
    	}
    }
    
    public void showGjest(Bruker gjest) {
    	gjestene.add(gjest.getNavn());
    	this.gjesteliste.setItems(gjestene);
    	
    }
    
    public void addGjest(ActionEvent event) throws IOException {
    	
    	//Må finne brukeren i databasen
    	String brukernavn = Klienten.getBruker(legg_til_gjester.getText());
    	System.out.println("Brukernavn: " + brukernavn + "EPOST: " +  legg_til_gjester.getText());
    	Bruker gjest = new Bruker(brukernavn, legg_til_gjester.getText());
    	showGjest(gjest);
    	gjeste_liste.add(gjest);
    }
    
    public void finnRom(ActionEvent event) throws IOException {
    	String rom = "";
    	if (hele_dagen.isSelected()) {
    		rom = Klienten.getRom(dato.toString(), "00:00", "23:59", gjeste_liste.size() + "");
    	}
    	else {
    		rom = Klienten.getRom(dato.toString(), start.toString(), slutt.toString(), gjeste_liste.size() + "");
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
	
	public void lagre(ActionEvent event) throws IOException {
		System.out.println(start);
		System.out.println(slutt);
		System.out.println(dato);
		Møterom rom =  new Møterom(100, valgt_rom.getText());
		if (! feilTekst.isVisible() && ! feilDato.isVisible()) {
			Avtale avtale = new Avtale(getBruker(), gjeste_liste, new TidsIntervall(start, slutt, dato), rom);
		}
		
	}
	
	public Bruker getBruker() {
		Bruker meg = new Bruker("Andreas Kvistad", "ahk9339@gmail.com");
		return meg;
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
		
		
}


