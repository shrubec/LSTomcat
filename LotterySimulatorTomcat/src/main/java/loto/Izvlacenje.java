package loto;

import hr.shrubec.simulacija.util.SimulacijaResultFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import listic.Broj;
import listic.Listic;
import random.RanMT;

public class Izvlacenje {
	
	private Integer kolo;
	private Date datum;
	private List<Listic> listici=new ArrayList<Listic>();
	private Set<Broj> izvucenaKombinacija=new TreeSet<Broj>();	
	private SimulacijaResultFile resultFile=null;

	
	public Izvlacenje (int kolo,Date datum,List<Listic> listici,SimulacijaResultFile resultFile) {
		this.kolo=kolo;
		this.datum=datum;
		this.listici=listici;
		this.resultFile=resultFile;
	}
	
	public void izvuci(int brojeva, int odBrojeva,RanMT ranMT) {
				
		while (izvucenaKombinacija.size() <=brojeva) {
			if (izvucenaKombinacija.size() == brojeva) 
				break;
			Integer odabraniBroj=ranMT.choose(1,odBrojeva);	
			if (!Util.isOdabrani(izvucenaKombinacija,odabraniBroj)) {
					izvucenaKombinacija.add(new Broj(odabraniBroj));	
			}
		}		
	}
	
	
	@SuppressWarnings("unchecked")
	public void provjeriRezultate() {
		
		try {
			resultFile.appendIzvuceno(izvucenaKombinacija,kolo,datum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Listic> provjereniListici=new ArrayList<Listic>();
		for (Listic listic:listici) {
					
			Map<Integer,Set<Broj>> kombinacije=listic.getKombinacije();			
			Map<Integer,Set<Broj>> zaokruzeneKombinacije=new TreeMap<Integer,Set<Broj>>();				
			Set<Integer> oznakeKombinacija=kombinacije.keySet();
			
			for (Integer key:oznakeKombinacija) {		
				
				Set<Broj> zaokruzeno=new TreeSet<Broj>();	
				Set<Broj> kombinacija=kombinacije.get(key);
				//int pogodjeno=0;
				Iterator it=kombinacija.iterator();
				while (it.hasNext()) {
				      Broj broj=(Broj)it.next();
				      if (izvucenaKombinacija.contains(broj)) {
				    	 // pogodjeno++;	
			    		  broj.setPogodjen(true);	 
				      }
				      zaokruzeno.add(broj);
				}
				
				zaokruzeneKombinacije.put(key, zaokruzeno);	
				
				try {
					resultFile.appendOdigrano(zaokruzeno);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
			
			listic.setKombinacije(zaokruzeneKombinacije);	
			provjereniListici.add(listic);	
			
			
			
			
			
		}		
		this.listici=provjereniListici;		
		

	}

	

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}


	public Set<Broj> getIzvucenaKombinacija() {
		return izvucenaKombinacija;
	}

	public void setIzvucenaKombinacija(Set<Broj> izvucenaKombinacija) {
		this.izvucenaKombinacija = izvucenaKombinacija;
	}

	public Integer getKolo() {
		return kolo;
	}

	public void setKolo(Integer kolo) {
		this.kolo = kolo;
	}


	public List<Listic> getListici() {
		return listici;
	}

	public void setListici(List<Listic> listici) {
		this.listici = listici;
	}
	
	

}
