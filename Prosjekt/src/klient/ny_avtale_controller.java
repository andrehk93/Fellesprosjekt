package klient;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.sun.javafx.scene.layout.region.Margins.Converter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ny_avtale_controller {
	
	private ArrayList<Node> alle_enheter;
	private ArrayList<Bruker> deltakere;
	private Møterom rom;
	private LocalTime start;
	private LocalTime slutt;
	private LocalDate dato;
	
	public ny_avtale_controller() {
		alle_enheter = new ArrayList<Node>();
		deltakere = new ArrayList<Bruker>();
		alle_enheter.add(avtalenavn);
		alle_enheter.add(startdato);
		alle_enheter.add(sluttdato);
		alle_enheter.add(fratid);
		alle_enheter.add(tiltid);
		alle_enheter.add(legg_til_gjester);
		alle_enheter.add(gjesteliste);
		alle_enheter.add(søk_møterom);
		alle_enheter.add(møteromliste);
		alle_enheter.add(møteromliste_slider);
		alle_enheter.add(antall_gjester);
		alle_enheter.add(valgt_rom);
		alle_enheter.add(forkast_knapp);
		alle_enheter.add(lagre_knapp);
		
		
	}
	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }
    
    
    
    @FXML
    TextField avtalenavn = new TextField();
    @FXML
    DatePicker startdato = new DatePicker();
    @FXML
    DatePicker sluttdato = new DatePicker();
    @FXML
    TextField fratid = new TextField();
    @FXML
    TextField tiltid = new TextField();
    @FXML
    TextField legg_til_gjester = new TextField();
    @FXML
    ListView gjesteliste = new ListView();
    @FXML
    Button søk_møterom = new Button();
    @FXML
    ListView møteromliste = new ListView();
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
    
    
    
    public void showRom() {
    	
    }
    
    public void reset() {
    	ScreenNavigator.loadScreen(ScreenNavigator.AVTALE);
    }
    
    public void handleDato() {
    	
    }
    
    
    public void handleTid(KeyEvent event) {
    	String tall = "0123456789";
    	String tekst = fratid.getText();
    	if (tekst.length() == 1) {
    		if (tekst.equals("0") || tekst.equals("1") || tekst.equals("2")) {
    			System.out.println("ok");
    		}
    		else {
    			fratid.setText(null);
    		}
    	}
    	if (tekst.length() == 2) {
    		if (tekst.substring(0,1).equals("0") || tekst.substring(0,1).equals("1")) {
    			if (tall.contains(tekst.substring(1,2))) {
    				System.out.println("MHMH");
    			}
    			else {
    				fratid.setText(tekst.substring(0,1));
    				fratid.end();
    			}
    		}
    		else {
    			if (tall.substring(0,4).contains(tekst.substring(1,2))) {
    				System.out.println("MHMH");
    			}
    			else {
    				fratid.setText(tekst.substring(0,1));
    				fratid.end();
    			}
    		}
    	}
    	if (tekst.length() == 3) {
    		if (tekst.charAt(2) == ':') {
    			System.out.println("jippi");
    		}
    		else {
    			fratid.setText(tekst.substring(0,2));
    			fratid.end();
    		}
    	}
    	if (tekst.length() == 4) {
    		if (tall.substring(0,6).contains(tekst.substring(3,4))) {
    			System.out.println("Nice");
    		}
    		else {
    			fratid.setText(tekst.substring(0,3));
    			fratid.end();
    		}
    	}
    	if (tekst.length() == 5) {
    		if (tall.contains(tekst.substring(4,5))) {
    			System.out.println("ferdig");
    		}
    		else {
    			fratid.setText(tekst.substring(0,4));
    			fratid.end();
    		}
    	}
    }
    
    public void addGjest() {
    	
    }
    
    public void finnRom() {
    	
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
		boolean farge_sjekk = true;
		for (Node enhet : alle_enheter) {
			if (enhet.getStyle().equals("-fx-text-box-border : red ")) {
				farge_sjekk = false;
			}
		}
		
		if (farge_sjekk) {
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


