package it.polito.tdp.meteo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	
	private List<Citta> leCitta;

	private List<Citta> best;
	private MeteoDAO dao;
	public Model() {
		dao = new MeteoDAO();
		this.leCitta = dao.getAllCitta();

	}
	

	public String getUmiditaMedia(int mese) {

		return dao.getUmiditaMedia(mese);
	}

	public List<Citta> calcolaSequenza(){
		List<Citta> parziale= new ArrayList<Citta>();
		this.best=null;
		cerca(parziale, 0);
		return null;
		
	}
	
	private void cerca(List<Citta> parziale, int livello) {
		Double costo= calcolaCosto(parziale);
		//caso terminale
		if(livello==this.NUMERO_GIORNI_TOTALI) {
			if (best == null || costo < calcolaCosto(best)) {
				// System.out.format("%f %s\n", costo, parziale) ;

				best = new ArrayList<>(parziale);
			}
		}
		for(Citta prova: leCitta) {
			if(aggiuntaValida(prova, parziale)) {
				parziale.add(prova);
				
				cerca(parziale, livello+1);
				
				//backtrack
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		int count=0;
		//verifica dei giorni massimi 
		for(Citta precedente: parziale) {
			if(precedente.equals(prova)) {
				count++;
			}
		}
		if(count>= this.NUMERO_GIORNI_CITTA_MAX)
			return false;
		//verifica giorni minimi
		//giorno iniziale
		if(parziale.size()==0) {
			return true;
		}
		//secondo o terzo giorno
		if(parziale.size()==1 || parziale.size()==2 ) {
			return parziale.get(parziale.size()-1).equals(prova);
		}
		if(parziale.get(parziale.size()-1).equals(prova)) { //giorni successivi e posso rimanere
			return true;
		}
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-1))) {
			return true;
		}
		return false;
	}


	private Double calcolaCosto(List<Citta> parziale) {
		double costo= 0.0;
		for(int i=0; i<=this.NUMERO_GIORNI_TOTALI; i++) {
			Citta c= parziale.get(i);
			//che umidità ho nella città in quel giorno
			double umidita= c.getRilevamenti().get(i).getUmidita();
			costo+= umidita;
		}
		for(int i=2; i<=this.NUMERO_GIORNI_TOTALI; i++) {
			if(parziale.get(i).equals(parziale.get(i-1))) {
				costo+=100;
			}
		}
		return costo;
	}


	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

	public HashSet<Integer> getMesi() {
		return dao.getMesi() ;
	}

}
