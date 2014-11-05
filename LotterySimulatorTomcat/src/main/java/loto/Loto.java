package loto;

import hr.shrubec.simulacija.util.SimulacijaResultFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import listic.Broj;
import listic.Listic;
import random.RanMT;
import random.Ranecu;

public class Loto {

	Logger LOGGER=Logger.getLogger(Loto.class.getName());
	
	private RanMT ranMT;
	private Ranecu ranescu;
	private SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
	private Calendar cal=Calendar.getInstance();
	
	private int brojeva;
	private int odBrojeva;
	
	private String simulacijaId;
	private Integer trenutnoKoloBroj=1;
	private KoloDO trenutnoKolo;
	private boolean finished=false;
	private boolean savedAsFinished=false;
	
	private KoloDO koloDo=new KoloDO();
	
	private IzvlacenjeDO trenutnoIzvlacenje;
	private IzvlacenjeDO izvlacenjeDo=new IzvlacenjeDO();
	
	private Integer speed=500;
	private boolean jackpot=false;
	
	private List<String> messagesList=new ArrayList<String>();
	private List<Integer> izvlacenja;
	private int trajanjeGodina=10;
	private Calendar startTime;
	private boolean paused=false;
//	private boolean nextStepActivated=false;
	private SimulacijaResultFile resultFile;
	private boolean fileClosed=false;
	
	
	private List<List<Integer>> kombinacijeBrojeva;
	private HttpSession session;
	
	
	
	
	public Loto(HttpSession session,String simulacijaId,int brojeva, int odBrojeva,int trajanjeGodina,List<String> izvlacenja) {
		
		this.session=session;
		this.simulacijaId=simulacijaId;
		this.brojeva=brojeva;
		this.odBrojeva=odBrojeva;
		this.trajanjeGodina=trajanjeGodina;
		this.izvlacenja=new ArrayList<Integer>();
		
		resultFile=new SimulacijaResultFile(simulacijaId);	
		startTime=Calendar.getInstance();
		for (String number:izvlacenja) {
			this.izvlacenja.add(Integer.valueOf(number));
		}
		
		ranescu=new Ranecu(new Date());		
		ranMT=new RanMT(new Date());
		
		
		Integer dan=cal.get(Calendar.DAY_OF_WEEK);
		while (!this.izvlacenja.contains(dan)) {
			cal.add(Calendar.DAY_OF_WEEK,1);
			dan=cal.get(Calendar.DAY_OF_WEEK);
		}
		
		
//		if (brojeva == 6 && odBrojeva == 45) {
//			int dan=cal.get(Calendar.DAY_OF_WEEK);
//			while (dan != Calendar.SUNDAY) {
//				cal.add(Calendar.DAY_OF_WEEK,1);
//				dan=cal.get(Calendar.DAY_OF_WEEK);
//			}
//		}
//		else if (brojeva == 7 && odBrojeva == 39) {
//			int dan=cal.get(Calendar.DAY_OF_WEEK);
//			while (dan != Calendar.WEDNESDAY && dan != Calendar.SATURDAY) {
//				cal.add(Calendar.DAY_OF_WEEK,1);
//				dan=cal.get(Calendar.DAY_OF_WEEK);
//			}
//		}
//		
//		

	}
	
//	public void kombinacije(int brojKola,List<List<Integer>> kombinacijeBrojeva) {
//		
//		int i=1;
//		while(true) {
//			
//			Calendar cal=Calendar.getInstance();
//			try {
//				cal.setTimeInMillis(session.getLastAccessedTime());
//			} catch (IllegalStateException e1) {
//				LOGGER.info("Sesija unistena");
//				brojKola=0;
//				finished=true;
//				return;
//			}
//			
//			Calendar cal1=Calendar.getInstance();
//			
//			if ((cal1.getTimeInMillis() - cal.getTimeInMillis()) > 5000) {
//				try {
//					session.invalidate();
//				} catch (IllegalStateException e) {
//					e.printStackTrace();
//				}
//				brojKola=0;
//				finished=true;
//				return;
//			}
//			
//			if (paused) {
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				continue;
//			}
//			
//			
//			
//			if (this.speed == 0) {
//				this.speed=10;
//			}
//			
//			try {
//				Thread.sleep(this.speed);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			
//			
//			List<Listic> listici=new ArrayList<Listic>();
//			for (List<Integer> brojevi:kombinacijeBrojeva) {
//				
//				Listic listic=new Listic(false);				
//				Set<Broj> kombinacija=new TreeSet<Broj>();
//				for (Integer broj:brojevi) {
//					kombinacija.add(new Broj(broj));				
//				}
//		
//				listic.dodajKombinaciju(kombinacija);
//				listici.add(listic);
//			}
//
//			odigraj(i,listici);		
//			
//			if (jackpot) {
//				this.finished=true;
//				LOGGER.info("JACKPOT!!!");
//				resultFile.closeFile();
//				return;
//				
//			}
//			
//			
//			if (this.cal.get(Calendar.YEAR) > (startTime.get(Calendar.YEAR) + trajanjeGodina)) {
//				this.finished=true;
//				
//				String message="Simulation finished!";
//				LOGGER.info(message);
//				resultFile.closeFile();
//				return;
//			}
//		
//			i++;
//			
//			if (nextStepActivated) {
//				paused=true;
//			}
//		}
//	}
	
	
	public void kombinacije(List<List<Integer>> kombinacijeBrojeva) {
		
		this.kombinacijeBrojeva=kombinacijeBrojeva;
		
		while(true) {
			
			Calendar cal=Calendar.getInstance();
			try {
				cal.setTimeInMillis(session.getLastAccessedTime());
			} catch (IllegalStateException e1) {
				LOGGER.info("Sesija unistena");
				finished=true;
				return;
			}
			
			Calendar cal1=Calendar.getInstance();
			
			if ((cal1.getTimeInMillis() - cal.getTimeInMillis()) > 5000) {
				try {
					session.invalidate();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				finished=true;
				return;
			}
			
			if (paused) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			if (this.speed == 0) {
				this.speed=10;
			}
			
//			System.out.println("Speed: " + speed);
			
			
			try {
				Thread.sleep(this.speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			kombinacijePojedinacno();
			
//			if (nextStepActivated) {
//				paused=true;
//			}
		}
	}
	
	public void kombinacijePojedinacno() {
		
		if (this.finished == true) {
			return;
		}
		
		List<Listic> listici=new ArrayList<Listic>();
		for (List<Integer> brojevi:kombinacijeBrojeva) {
			
			Listic listic=new Listic(false);				
			Set<Broj> kombinacija=new TreeSet<Broj>();
			for (Integer broj:brojevi) {
				kombinacija.add(new Broj(broj));				
			}
	
			listic.dodajKombinaciju(kombinacija);
			listici.add(listic);
		}

		odigraj(trenutnoKoloBroj,listici);		
		
		if (jackpot) {
			this.finished=true;
			LOGGER.info("JACKPOT!!!");
			resultFile.closeFile();
			return;
			
		}
		
		
		if (this.cal.get(Calendar.YEAR) > (startTime.get(Calendar.YEAR) + trajanjeGodina)) {
			this.finished=true;
			
			if (!fileClosed) {
				String message="Simulation finished!";
				LOGGER.info(message);
				resultFile.closeFile();
				fileClosed=true;
			}
			
			return;
		}
	
		trenutnoKoloBroj++;
	}
	
	
	
//	
//	public void kombinacijePojedinacno(int izvlacenje,int brojKola,List<List<Integer>> kombinacijeBrojeva) {
//		
//		this.finished=false;
//		if (izvlacenje <= brojKola) {
//			List<Listic> listici=new ArrayList<Listic>();
//			for (List<Integer> brojevi:kombinacijeBrojeva) {
//				
//				Listic listic=new Listic(false);				
//				Set<Broj> kombinacija=new TreeSet<Broj>();
//				for (Integer broj:brojevi) {
//					kombinacija.add(new Broj(broj));				
//				}
//		
//				listic.dodajKombinaciju(kombinacija);
//				listici.add(listic);
//			}
//
//			odigraj(izvlacenje,listici);	
//		}
//
//	}
	
	public void kombinacijeRandom(int brojKola,int listicaUKolu,int brojKombinacija) {
		
		for (int i=1; i <=brojKola; i++) {		
			
			List<Listic> listici=new ArrayList<Listic>();
			for (int k=1; k<=listicaUKolu; k++) {
				Listic listic=new Listic(false);
				for (int z=1; z <= brojKombinacija; z++)
					listic.dodajRandomKombinaciju(ranescu, brojeva, odBrojeva);
				listici.add(listic);
			}
			

			odigraj(i,listici);			
		}
		
	}
	
	public void sistem(int brojKola,List<List<Integer>> kombinacijeBrojeva) {
		for (int i=1; i <=brojKola; i++) {	
			
			List<Listic> listici=new ArrayList<Listic>();
			for (List<Integer> brojevi:kombinacijeBrojeva) {
				
				Listic listic=new Listic(true);				
				Set<Broj> kombinacija=new TreeSet<Broj>();
				for (Integer broj:brojevi) {
					kombinacija.add(new Broj(broj));				
				}
		
				listic.dodajKombinaciju(kombinacija);
				listici.add(listic);
			}

			odigraj(i,listici);			
		}
	}
	
	public void sistemRandom(int brojKola,int listicaUKolu,int brojevaUKombinaciji) {
		
		for (int i=1; i <=brojKola; i++) {			
			
			List<Listic> listici=new ArrayList<Listic>();
			for (int k=1; k<=listicaUKolu; k++) {
				Listic listic=new Listic(true);
				listic.dodajRandomKombinaciju(ranescu, brojevaUKombinaciji, odBrojeva);
				listici.add(listic);
			}
	
			odigraj(i,listici);			
		}
		
		
	
	}

	private void odigraj(int kolo,List<Listic> listici) {
		
		List<Listic> osvjezeniListici=new ArrayList<Listic>();
		osvjezeniListici.addAll(listici);
		
//		if (brojeva == 6 && odBrojeva == 45) {
//			cal.add(Calendar.DAY_OF_WEEK,7);
//		}
//		else if (brojeva == 7 && odBrojeva == 39) {
//			int dan=cal.get(Calendar.DAY_OF_WEEK);
//			if (dan == Calendar.WEDNESDAY ) 
//				cal.add(Calendar.DAY_OF_WEEK,3);
//			else
//				cal.add(Calendar.DAY_OF_WEEK,4);
//		}
//		
		
		cal.add(Calendar.DAY_OF_WEEK,1);
		int dan=cal.get(Calendar.DAY_OF_WEEK);
		while (!izvlacenja.contains(dan)) {
			cal.add(Calendar.DAY_OF_WEEK,1);
			dan=cal.get(Calendar.DAY_OF_WEEK);
		}
		
		
		
	
		Izvlacenje izvlacenje=new Izvlacenje(kolo,cal.getTime(),osvjezeniListici,resultFile);
		izvlacenje.izvuci(brojeva, odBrojeva, ranMT);
		izvlacenje.provjeriRezultate();

		izvlacenjeDo=new IzvlacenjeDO();
		List<Listic> dobitniListici=izvlacenje.getListici();
		for (Listic listic:dobitniListici) {
			Map<Integer,Set<Broj>> dobitneKombinacije=listic.getDobitneKombinacije(0);
			Set<Integer> oznakeKombinacija=dobitneKombinacije.keySet();
			
			
			
			for (Integer key:oznakeKombinacija) {
				
				KoloDO prethodnoKolo=koloDo;
				
				koloDo=new KoloDO();
				koloDo.setKolo(izvlacenje.getKolo());
				koloDo.setDatum(izvlacenje.getDatum());
				
				
				Set<Broj> kombinacija=dobitneKombinacije.get(key);
				
				KombinacijaDO kombinacijaDo=new KombinacijaDO();
				
//				LOGGER.info("Odabrano:");
				for (Broj broj:kombinacija) {
//					LOGGER.info(broj.getBroj().intValue()+ ", ");
					kombinacijaDo.getOdigrano().add(broj.getBroj());
				}
//				LOGGER.info("Pogo�eno:");
				
				int pogodjeno=0;
				for (Broj broj:kombinacija) {
					if (broj.getPogodjen()) {
//						LOGGER.info(broj.getBroj().intValue()+ ", ");
						kombinacijaDo.getPogodjeno().add(broj.getBroj());
						pogodjeno++;
					}
				}
				
				
				koloDo.setUkupnoOdigrano(prethodnoKolo.getUkupnoOdigrano()+1);
				koloDo.setUkupnoPogodjeno0(prethodnoKolo.getUkupnoPogodjeno0());
				koloDo.setUkupnoPogodjeno1(prethodnoKolo.getUkupnoPogodjeno1());
				koloDo.setUkupnoPogodjeno2(prethodnoKolo.getUkupnoPogodjeno2());
				koloDo.setUkupnoPogodjeno3(prethodnoKolo.getUkupnoPogodjeno3());
				koloDo.setUkupnoPogodjeno4(prethodnoKolo.getUkupnoPogodjeno4());
				koloDo.setUkupnoPogodjeno5(prethodnoKolo.getUkupnoPogodjeno5());
				koloDo.setUkupnoPogodjeno6(prethodnoKolo.getUkupnoPogodjeno6());
				koloDo.setUkupnoPogodjeno7(prethodnoKolo.getUkupnoPogodjeno7());
				koloDo.setUkupnoPogodjeno8(prethodnoKolo.getUkupnoPogodjeno8());
				koloDo.setUkupnoPogodjeno9(prethodnoKolo.getUkupnoPogodjeno9());
				koloDo.setUkupnoPogodjeno10(prethodnoKolo.getUkupnoPogodjeno10());
				
				koloDo.povecajPogodjeno(pogodjeno);
//				LOGGER.info("Izvu�eno:");
				for (Broj broj:izvlacenje.getIzvucenaKombinacija()) {
//					LOGGER.info(broj.getBroj().intValue()+ ", ");
					koloDo.getIzvuceno().add(broj.getBroj());
				}
				
				koloDo.getKombinacije().add(kombinacijaDo);
				
				izvlacenjeDo.getKola().add(koloDo);
				
//				LOGGER.info("KOLA SIZE: "  +izvlacenjeDo.getKola().size() );
//				LOGGER.info(izvlacenje.getKolo() +". KOLO, DATUM " + sdf.format(izvlacenje.getDatum()) + ", UKUPNO POGO�ENO: " + listic.getBrojPogodjenihUKombinaciji(key) + ", ");
				
				setTrenutnoKolo(koloDo);
				
				
				if (listic.getBrojPogodjenihUKombinaciji(key).intValue() >= (brojeva)) {
					jackpot=true;
					messagesList.add( sdf.format(izvlacenje.getDatum()) + " Jackpot!");
				}
				
				if (listic.getBrojPogodjenihUKombinaciji(key).intValue() >= (brojeva - 1)) {
					messagesList.add( sdf.format(izvlacenje.getDatum()) + " Jackpot missed by one number!");
				}
				
				if (listic.getBrojPogodjenihUKombinaciji(key).intValue() >= (brojeva - 2)) {
					messagesList.add( sdf.format(izvlacenje.getDatum()) + " Jackpot missed by two numbers!");
				}
				
			}
			
			
			
		}
		
		setTrenutnoIzvlacenje(izvlacenjeDo);

	}

	public String getSimulacijaId() {
		return simulacijaId;
	}

	public void setSimulacijaId(String simulacijaId) {
		this.simulacijaId = simulacijaId;
	}

	public KoloDO getTrenutnoKolo() {
		return trenutnoKolo;
	}

	public void setTrenutnoKolo(KoloDO trenutnoKolo) {
		this.trenutnoKolo = trenutnoKolo;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public IzvlacenjeDO getTrenutnoIzvlacenje() {
		return trenutnoIzvlacenje;
	}

	public void setTrenutnoIzvlacenje(IzvlacenjeDO trenutnoIzvlacenje) {
		this.trenutnoIzvlacenje = trenutnoIzvlacenje;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		Integer s=100-speed;
		this.speed = s*2;
		
		if (speed.intValue() == 0) {
			this.speed=500;
		}
		
	}

	public List<String> getMessagesList() {
		return messagesList;
	}

	public void setMessagesList(List<String> messagesList) {
		this.messagesList = messagesList;
	}
	

	public String getLotteryType() {
		return String.valueOf(brojeva) +" / " + String.valueOf(odBrojeva);
	}

	public int getBrojeva() {
		return brojeva;
	}

	public void setBrojeva(int brojeva) {
		this.brojeva = brojeva;
	}

	public boolean isSavedAsFinished() {
		return savedAsFinished;
	}

	public void setSavedAsFinished(boolean savedAsFinished) {
		this.savedAsFinished = savedAsFinished;
	}

	public int getTrajanjeGodina() {
		return trajanjeGodina;
	}

	public void setTrajanjeGodina(int trajanjeGodina) {
		this.trajanjeGodina = trajanjeGodina;
	}

	
	public void pauseSimulation() {
		this.paused=true;
	}
	
	public void resumeSimulation() {
		this.paused=false;
//		nextStepActivated=false;
	}
	
	public void nextStep() {
//		nextStepActivated=true;
//		paused=false;
		kombinacijePojedinacno();
	}
	
	public void deleteFile() {
		
		System.out.println("Brisem file.. ");
		try {
			resultFile.deleteFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SimulacijaResultFile getResultFile() {
		return resultFile;
	}

	public boolean isJackpot() {
		return jackpot;
	}

	public void setJackpot(boolean jackpot) {
		this.jackpot = jackpot;
	}
	
	
	
}
