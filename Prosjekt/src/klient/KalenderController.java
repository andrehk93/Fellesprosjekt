package klient;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class KalenderController {
	
	private ArrayList<Dag> dager;
	@FXML private GridPane ruter;
	@FXML private Button forrigeManed;
	@FXML private Button nesteManed;
	@FXML private Label manedLabel;
	@FXML private Label arLabel;
	private int maned;
	private int aar;
	private String[] avtale_liste;
	
	public void initialize() throws IOException{
		setMonth(LocalDate.now().getMonthValue());
		setYear(LocalDate.now().getYear());
		flushView();
	}
	
	private void flushView() throws IOException{
		dager = new ArrayList<Dag>();
		ruter.getChildren().clear();
		getDays();
		loadGrid();	
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
		avtale_liste = Klienten.mineAvtaler(Klienten.bruker.getEmail()).split(" ");
		for (int k = 0; k < avtale_liste.length; k++) {
			if (k%2 != 0) {
				String dato = avtale_liste[k];
				String avtaleid = avtale_liste[k-1];
				createAvtale(dato, avtaleid);
			}
		}
		
	}
	
	
	// VELDIG MYE TULL, MEN FUNKER
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
		String[] deltakere = Klienten.getDeltakere(avtaleid).split(" ");
		if (! deltakere.toString().equals(null) && ! deltakere.equals("NONE")) {
			for (String epost : deltakere) {
				if (epost.trim().equals("NONE")) {
					break;
				}
				else {
					Bruker deltaker = new Bruker(Klienten.getBruker(epost), epost);
					deltaker_liste.add(deltaker);
				}
			}
		}
		Avtale avtale = new Avtale(Klienten.bruker, deltaker_liste, tid, rom);
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
	private void nextPaneMakeAppointment(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.AVTALE);
	}
	
	@FXML
	private void nextMonth(ActionEvent event) throws IOException {
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
	private void previousMonth(ActionEvent event) throws IOException {
		if(getMonth()-1<=0){
			setMonth(12);
			setYear(getYear()-1);
		}
		else{
			setMonth(getMonth()-1);
		}
		flushView();
	}
}
