package klient;

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
import javafx.scene.Node;
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
	
	private ArrayList<Node> alle_enheter;
	private ArrayList<Bruker> deltakere;
	private Møterom rom;
	private LocalTime start;
	private LocalTime slutt;
	private LocalDate dato;
	
	
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
		gjestelisten = new ArrayList<String>();
    	gjestene = FXCollections.observableList(gjestelisten);
		feilTekst.setVisible(false);
		feilDato.setVisible(false);
		System.out.println("LETS CREATE");
		alle_enheter = new ArrayList<Node>();
		deltakere = new ArrayList<Bruker>();
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
    
    
    
    public void showRom(ArrayList<Møterom> rommene) {
    	ChangeListener<Number> romvalg = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				System.out.println("Valg");
				valgt_rom.setText(møteromliste.getItems().get(møteromliste.getEditingIndex()));
			}
    	};
    	
    	System.out.println("Lager rom: ");
    	List<String> rom_liste = new ArrayList<String>();
    	for (Møterom rom : rommene) {
    		rom_liste.add(rom.getNavn() + " - " + rom.getKapasitet() + " plasser");
    	}
    	ObservableList<String> rom = FXCollections.observableList(rom_liste);
    	møteromliste.setItems(rom);
    	møteromliste.editingIndexProperty().addListener(romvalg);
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
    				return true;
    			}
    			else {
    				sluttdato.setValue(startdato.getValue());
    				feilDato.setVisible(true);
    				return false;
    			}
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
					System.out.println("må fylle ut alle");
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
    
    public void addGjest(ActionEvent event) {
    	//Må finne brukeren i databasen
    	Bruker gjest = new Bruker(legg_til_gjester.getText(), "ahk9339@gmail.com");
    	showGjest(gjest);
    }
    
    public void finnRom(ActionEvent event) {
    	//Må spørre database om ledig(e) rom, midlertidig metode:
    	Møterom rom = new Møterom(20, "gobi");
    	Møterom rom2 = new Møterom(30, "moki");
    	ArrayList<Møterom> rommene = new ArrayList<Møterom>();
    	rommene.add(rom);
    	rommene.add(rom2);
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
	
	public void lagre(ActionEvent event) {
		if (! feilTekst.isVisible() && ! feilDato.isVisible()) {
			Avtale avtale = new Avtale(getBruker(), deltakere, new TidsIntervall(start, slutt, dato), rom);
		}
	}
	
	public Bruker getBruker() {
		return null;
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


