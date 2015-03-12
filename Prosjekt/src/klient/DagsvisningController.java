package klient;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DagsvisningController {
	
	
	@FXML private Label brukernavn;
	private String[] notifikasjonene;
	private boolean ingenInvitasjoner;
	@FXML private ListView<String> notifikasjoner_lv;
	private List<String> list;
	private ArrayList<Varsel> oppdelte_notifikasjoner;
	private ObservableList<String> items;
	private String[] avtaleListe;
	
	public void initialize() throws Exception {
		list = new ArrayList<String>();
		showBruker();
		this.notifikasjonene = KalenderController.notifikasjonene;
		settUpListView();
		showInvitasjoner();
		showNotifications();
		setItems();
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
						ScreenNavigator.loadScreen(ScreenNavigator.SE_AVTALE);
					}
					else {
						try {
							KalenderController.pop(newValue);
						} catch (Exception e) {
							System.out.println("FEIL: " + e);
						}
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
	private void nextPaneLogOut(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.INNLOGGING);
	}
	

}
