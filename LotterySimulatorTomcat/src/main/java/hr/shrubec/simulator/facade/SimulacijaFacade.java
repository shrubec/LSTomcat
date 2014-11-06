package hr.shrubec.simulator.facade;

import hr.shrubec.simulator.loto.Loto;
import hr.shrubec.simulator.loto.LotoThread;
import hr.shrubec.simulator.object.Kombinacija;
import hr.shrubec.simulator.object.OdigraniBrojevi;
import hr.shrubec.simulator.object.Simulacija;
import hr.shrubec.simulator.util.SimulacijaResultFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
@ViewScoped
public class SimulacijaFacade implements Serializable{

//	@EJB SimulacijaBean simulacijaBean;
	
	private Integer kombinacijaZaIspuniti=4;
	private Simulacija simulacija=new Simulacija();
	private boolean skip; 
	private boolean disabledBrojKombinacija=false;
	
	private List<SelectItem> brojeviKombinacijeK1=new ArrayList<SelectItem>();
	private List<SelectItem> brojeviKombinacijeK2=new ArrayList<SelectItem>();
	private List<SelectItem> brojeviKombinacijeK3=new ArrayList<SelectItem>();
	
	
	
	private List<SelectItem> kombinacija1=new ArrayList<SelectItem>();
	private List<String> selectedOptions;  
	
	private Integer trajanjeGodina=10;
	private String trenutnaSimulacijaId=null;
	private String finished="false";
	private Integer pojedinacno=1;
	private List<List<Integer>> listaKombinacija;
	
	private Boolean mainVisible=true;
	private Boolean simulacijaVisible=false;
	private Integer simulationSpeed=0; //20
	private PieChartModel pieModel=new PieChartModel();  
	
	
	private Boolean simulationStarted=Boolean.FALSE;
	private Boolean simulationPaused=Boolean.FALSE;
	
	private StreamedContent file;
	
	
	
	public List<String> getSelectedOptions() {  
	    return selectedOptions;  
	}  
	public void setSelectedOptions(List<String> selectedOptions) {  
		this.selectedOptions = selectedOptions;  
	}  
	    
	    
	    
	public SimulacijaFacade() {
		HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session == null) 
			session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}

	
	
	
	public Simulacija getSimulacija() {
		return simulacija;
	}




	public void setSimulacija(Simulacija simulacija) {
		this.simulacija = simulacija;
	}




//	public String onFlowProcess(FlowEvent event) {  
//       
//       if (event.getNewStep().equals("rezultati"))
//    	   validiraj();
//       
//        if(skip) {  
//            skip = false;   //reset in case user goes back  
//            return "confirm";  
//        }  
//        else {  
//            return event.getNewStep();  
//        }  
//        
//       
//        
//    }  
//	
	 public boolean isSkip() {  
	        return skip;  
	 }   
	  
	 public void setSkip(boolean skip) {  
	        this.skip = skip;  
	 }




	public boolean getDisableCustomTip() {
		
		if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 10)
			return false;
		else
			return true;
	}



	public boolean isDisabledBrojKombinacija() {
		
		if (simulacija.getTipIgre() != null && simulacija.getTipIgre().intValue() == 2)
			disabledBrojKombinacija= true;
		else
			disabledBrojKombinacija=false;
	
		return disabledBrojKombinacija;
	}




	public void setDisabledBrojKombinacija(boolean disabledBrojKombinacija) {
		this.disabledBrojKombinacija = disabledBrojKombinacija;
	}  
	    
	
	public void updateTipLota() {
		
	}
	
	
	public void updateBrojeva() {
		 if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 1) {
			 simulacija.setBrojeva(5);
			 simulacija.setOdBrojeva(35);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 2) {
			 simulacija.setBrojeva(5);
			 simulacija.setOdBrojeva(36);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 3) {
			 simulacija.setBrojeva(5);
			 simulacija.setOdBrojeva(40);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 4) {
			 simulacija.setBrojeva(5);
			 simulacija.setOdBrojeva(42);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 5) {
			 simulacija.setBrojeva(5);
			 simulacija.setOdBrojeva(90);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 6) {
			 simulacija.setBrojeva(6);
			 simulacija.setOdBrojeva(39);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 7) {
			 simulacija.setBrojeva(6);
			 simulacija.setOdBrojeva(42);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 8) {
			 simulacija.setBrojeva(6);
			 simulacija.setOdBrojeva(45);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 9) {
			 simulacija.setBrojeva(6);
			 simulacija.setOdBrojeva(49);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 10) {
			 simulacija.setBrojeva(7);
			 simulacija.setOdBrojeva(35);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 11) {
			 simulacija.setBrojeva(7);
			 simulacija.setOdBrojeva(39);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 12) {
			 simulacija.setBrojeva(7);
			 simulacija.setOdBrojeva(49);
		 }
		 else if (simulacija.getVrsta() != null && simulacija.getVrsta().intValue() == 13) {
			 simulacija.setBrojeva(12);
			 simulacija.setOdBrojeva(24);
		 }
		 
		 if (simulacija.getBrojeva() != null)
			 simulacija.setBrojevaZaOdabrati(simulacija.getBrojeva()+1);
		 
		 /*
		 if (simulacija.getTipBrojeva() == Simulacija.SIMULACIJA_TIPBROJEVA_UNOS)
			 simulacija.setListicaPoIzvlacenju(1);*/
		 
	}
	
	public boolean getPrikaziBrojevaZaOdabrati() {
		
		if (simulacija.getTipIgre() != null && simulacija.getTipIgre().intValue() == 2)
			return true;
		else
			return false;
		
	}

	
	public List<SelectItem> getBrojeviKombinacijeK1() {
		brojeviKombinacijeK1=new ArrayList<SelectItem>();
		for (int i=1; i <= simulacija.getOdBrojeva(); i++) {
			brojeviKombinacijeK1.add(new SelectItem(new Integer(i),String.valueOf(i)));
			i=i+2;
		}
		
		return brojeviKombinacijeK1;
	}
	public void setBrojeviKombinacijeK1(List<SelectItem> brojeviKombinacijeK1) {
	
		this.brojeviKombinacijeK1 = brojeviKombinacijeK1;
	}
	public List<SelectItem> getBrojeviKombinacijeK2() {
		brojeviKombinacijeK2=new ArrayList<SelectItem>();
		for (int i=2; i <= simulacija.getOdBrojeva(); i++) {
			brojeviKombinacijeK2.add(new SelectItem(new Integer(i),String.valueOf(i)));
			i=i+2;
		}
		
		return brojeviKombinacijeK2;
	}
	public void setBrojeviKombinacijeK2(List<SelectItem> brojeviKombinacijeK2) {
		this.brojeviKombinacijeK2 = brojeviKombinacijeK2;
	}
	public List<SelectItem> getBrojeviKombinacijeK3() {
		brojeviKombinacijeK3=new ArrayList<SelectItem>();
		for (int i=3; i <= simulacija.getOdBrojeva(); i++) {
			brojeviKombinacijeK3.add(new SelectItem(new Integer(i),String.valueOf(i)));
			i=i+2;
		}
		
		return brojeviKombinacijeK3;
	}
	public void setBrojeviKombinacijeK3(List<SelectItem> brojeviKombinacijeK3) {
		this.brojeviKombinacijeK3 = brojeviKombinacijeK3;
	}
	

	public boolean generirajKombinaciju(int kombinacija) {
		if (kombinacija <=kombinacijaZaIspuniti)
			return true;
		else
			return false;
	}

	public boolean getDisableListicaPoIzvlacenju() {
		
		if (simulacija.getTipBrojeva().intValue() == 1) 
			return false;
		else 
			return true;
		
	}
	
	
	public boolean getRenderOdabirBrojeva() {
		
		if (simulacija.getTipBrojeva().intValue() != 1) 
			return true;
		else 
			return false;
		
	}
	
	public Integer getKombinacijaZaIspuniti() {
		return kombinacijaZaIspuniti;
	}
	public void setKombinacijaZaIspuniti(Integer kombinacijaZaIspuniti) {
		this.kombinacijaZaIspuniti = kombinacijaZaIspuniti;
	}


	private boolean validiraj() {
		
		boolean success=true;
		if (simulacija.getTipBrojeva().intValue() == Simulacija.SIMULACIJA_TIPBROJEVA_UNOS) {
			for (int i=0; i < kombinacijaZaIspuniti; i++) {
				if (!validirajKombinaciju(i))
					success=false;
					
			}	
			
			if(simulacija.getOdigraniBrojevi().getKombinacija1().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija2().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija3().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija4().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija5().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija6().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija7().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija8().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija9().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija10().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija11().getBrojeviKombinacije().isEmpty() &&
					simulacija.getOdigraniBrojevi().getKombinacija12().getBrojeviKombinacije().isEmpty()) {
				
				String poruka="Please select numbers";
				FacesContext.getCurrentInstance().addMessage(":messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, poruka, poruka));
				success=false;
			}
					
			
			
			if (success) {
				
				listaKombinacija=new ArrayList<List<Integer>>();	
				List<Integer> listaBrojeva=new ArrayList<Integer>();
				
				for (int i=0; i < kombinacijaZaIspuniti; i++) {
					
					try {
						OdigraniBrojevi brojevi=simulacija.getOdigraniBrojevi();
						Method method=OdigraniBrojevi.class.getMethod("getKombinacija"+(i+1), null);	
						Kombinacija kom= (Kombinacija)method.invoke(brojevi);
						List lista=new ArrayList(kom.getBrojeviKombinacije());
						if (lista.size() > 0 ) {

							listaBrojeva=new ArrayList<Integer>();
							for (int k=0; k < lista.size(); k++) {
								Integer broj=Integer.valueOf((String)lista.get(k));
								listaBrojeva.add(broj);
							}
							
							listaKombinacija.add(listaBrojeva);
							
						}	
					} catch (Exception e) {
						e.printStackTrace();
					} 

				}	
				
				HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
				Loto loto=new Loto(session,UUID.randomUUID().toString(),simulacija.getBrojeva(),simulacija.getOdBrojeva(),trajanjeGodina,simulacija.getIzvlacenja());
				
				
				Map simulacijaMap=null;				
				if (session.getAttribute("simulacija") == null) {
					simulacijaMap=new HashMap<String,Loto>();	
				}
				else {
					simulacijaMap=(Map)session.getAttribute("simulacija");
				}
				
				simulacijaMap.put(loto.getSimulacijaId(), loto);
				
				session.setAttribute("simulacija", simulacijaMap);
				
				
				trenutnaSimulacijaId=loto.getSimulacijaId();
				
				System.out.println("TRENUTNA SIMULACIJA ID: " + trenutnaSimulacijaId);
				
				
				/*
				LotoThread thread=new LotoThread(loto,listaKombinacija);
				thread.start();*/
				
				//loto.kombinacije(1000, listaKombinacija);
				
				
			}
	
		}
		
		return success;
		
	}
	
	
	public boolean validirajKombinaciju(Integer kombinacija) {
		
		try {
			OdigraniBrojevi brojevi=simulacija.getOdigraniBrojevi();
			Method method=OdigraniBrojevi.class.getMethod("getKombinacija"+(kombinacija+1), null);	
			Kombinacija kom= (Kombinacija)method.invoke(brojevi);
			List lista=new ArrayList(kom.getBrojeviKombinacije());
			if (lista.size() > 0 && lista.size() != simulacija.getBrojeva().intValue()) {
				
				String poruka="Ticket " + (kombinacija+1) + ": please select " + simulacija.getBrojeva() + " numbers";
				FacesContext.getCurrentInstance().addMessage(":messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, poruka, poruka));
				return false;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		return true;
	}
	
	
	public void kombinacijaChanged(ValueChangeEvent evt) {
		
		validirajKombinaciju(1);
		
	}

	
	public void test() {
	}
	
	public void pokreniSimulaciju() {
		
		if (!validiraj()) {
			return;
		}
		
		
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		Loto loto=null;
		Map map=(Map)session.getAttribute("simulacija");
		if (map != null)
			loto=(Loto) map.get(trenutnaSimulacijaId);
		

		if (loto != null) {

//			entity.Simulacija simulation=new entity.Simulacija();
//			simulation.setHash(trenutnaSimulacijaId);
//			simulation.setJackpot(false);
//			simulation.setLotteryType(loto.getLotteryType());
			String s="";
			
			for (String day:simulacija.getIzvlacenja()) {
				s=s+" " + day.toString();
			}
//			simulation.setSelectedDays(s);
			
			String selectedNumbers="";
			int i=1;
			for (List<Integer> list:listaKombinacija) {
				
				if (!list.isEmpty()) {
					String selectedNumbersList="Ticket " +i+": ";
					for (Integer number:list) {
						selectedNumbersList=selectedNumbersList+number.toString()+" ";
					}
					selectedNumbers=selectedNumbers+selectedNumbersList;
					
				}
				i++;
			}
			
//			simulation.setSelectedNumbers(selectedNumbers);
//			simulation.setStartTime(new Date());
//			simulation.setSimulationDate(new Date());
//			simulacijaBean.saveSimulacija(simulation);
			
			SimulacijaResultFile.saveSimulacija(loto.getLotteryType(), s, selectedNumbers);
			
			LotoThread thread=new LotoThread(loto,listaKombinacija);
			thread.start();
			simulationStarted=true;
			
			RequestContext.getCurrentInstance().execute("start();");
		}
		 
		
	}
	
	public String getFinished() {
		
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		
		Map map=(Map)session.getAttribute("simulacija");
		if(map == null)
			return "false";
		
		Loto loto=(Loto) map.get(trenutnaSimulacijaId);
		if (loto != null) {
			System.out.println("FACADE simulacija id: " + trenutnaSimulacijaId + ", finished: " +loto.isFinished());
			if (loto.isFinished() && !loto.isSavedAsFinished()) {
//				simulacijaBean.zavrsiSimulaciju(trenutnaSimulacijaId, false);
				loto.setSavedAsFinished(true);
//				RequestContext.getCurrentInstance().execute("alert('Simulation finished!')");
			}
			return new Boolean(loto.isFinished()).toString();
		}
		
		return "false"; 
		
	}
	
	public String getTrenutnaSimulacijaId() {
		return trenutnaSimulacijaId;
	}
	public void setTrenutnaSimulacijaId(String trenutnaSimulacijaId) {
		this.trenutnaSimulacijaId = trenutnaSimulacijaId;
	}
	public void setFinished(String finished) { 
		this.finished = finished;
	}
	

	public PieChartModel getPieModel() {
		
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		Loto loto=null;
		Map map=(Map)session.getAttribute("simulacija");
		if (map != null)
			loto=(Loto) map.get(trenutnaSimulacijaId);
		
		System.out.println("Chart map: " + map);

		if(loto == null || loto.getTrenutnoKolo() == null) {

			pieModel.set("All numbers missed", 0);
			pieModel.set("Match 1", 0);
			pieModel.set("Match 2", 0);
			pieModel.set("Match 3", 0);
			pieModel.set("Match 4", 0);
			pieModel.set("Match 5", 0);
			pieModel.set("Match 6", 0);
			pieModel.set("Match 7", 0);
			
//			if (simulacija.getBrojeva().intValue() == 12) {
//				pieModel.set("Match 8", 0);
//				pieModel.set("Match 9", 0);
//				pieModel.set("Match 10", 0);
//				pieModel.set("Match 11", 0);
//				pieModel.set("Match 12", 0);
//			}

			return pieModel;
		}
		
		

	     pieModel.set("All numbers missed",  loto.getTrenutnoKolo().getUkupnoPogodjeno0());  
	     pieModel.set("Match 1 ",  loto.getTrenutnoKolo().getUkupnoPogodjeno1());  
	     pieModel.set("Match 2",  loto.getTrenutnoKolo().getUkupnoPogodjeno2());  
	     pieModel.set("Match 3",  loto.getTrenutnoKolo().getUkupnoPogodjeno3());
	     pieModel.set("Match 4",  loto.getTrenutnoKolo().getUkupnoPogodjeno4());
	     pieModel.set("Match 5",  loto.getTrenutnoKolo().getUkupnoPogodjeno5());
	     pieModel.set("Match 6",  loto.getTrenutnoKolo().getUkupnoPogodjeno6());
	     pieModel.set("Match 7",  loto.getTrenutnoKolo().getUkupnoPogodjeno7());
	     
//	 	if (simulacija.getBrojeva().intValue() == 12) {
//	 		 pieModel.set("Match 8",  loto.getTrenutnoKolo().getUkupnoPogodjeno8());
//	 		 pieModel.set("Match 9",  loto.getTrenutnoKolo().getUkupnoPogodjeno9());
//	 		 pieModel.set("Match 10",  loto.getTrenutnoKolo().getUkupnoPogodjeno10());
//	 		 pieModel.set("Match 11",  loto.getTrenutnoKolo().getUkupnoPogodjeno11());
//	 		 pieModel.set("Match 12",  loto.getTrenutnoKolo().getUkupnoPogodjeno12());
//	 	}
		
		pieModel.setLegendPosition("w");
		pieModel.setFill(true);
		pieModel.setShowDataLabels(true);
	 	
		return pieModel;
	} 
	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel; 
	}
	
//	public void odigrajPojedinacno() {
//		
//		if (pojedinacno == 1)
//			validiraj();
//		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//		
//		Loto loto=null;
//		Map map=(Map)session.getAttribute("simulacija");
//		if (map != null)
//			loto=(Loto) map.get(trenutnaSimulacijaId);
//		
//
//		if (loto != null) {
//			loto.kombinacijePojedinacno(pojedinacno, 5000, listaKombinacija);
//		}
//			
//		pojedinacno++;
//		
//		mainVisible=false;
//		simulacijaVisible=true;
//			
//	}
//	
//	
//	public void next() {
//		validiraj();
//	}
//	
	public void noviBrojevi() {
		HttpServletResponse response=(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		try {
			response.sendRedirect("simulacija.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Boolean getMainVisible() {
		return mainVisible;
	}
	public void setMainVisible(Boolean mainVisible) {
		this.mainVisible = mainVisible;
	}
	public Boolean getSimulacijaVisible() {
		return simulacijaVisible;
	}
	public void setSimulacijaVisible(Boolean simulacijaVisible) {
		this.simulacijaVisible = simulacijaVisible;
	}
	public Integer getSimulationSpeed() {
		return simulationSpeed;
	}
	public void setSimulationSpeed(Integer simulationSpeed) {
		this.simulationSpeed = simulationSpeed;
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		Loto loto=null;
		Map map=(Map)session.getAttribute("simulacija");
		if (map != null)
			loto=(Loto) map.get(trenutnaSimulacijaId);
		
		if (loto != null) {
			loto.setSpeed(this.simulationSpeed);
		}
		
	}
	

	public void displaySimulationMessage() {
		
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		Loto loto=null;
		Map map=(Map)session.getAttribute("simulacija");
		if (map != null)
			loto=(Loto) map.get(trenutnaSimulacijaId);
		
		if (loto != null) {
			for (String message:loto.getMessagesList()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
				
			}
			loto.getMessagesList().clear();
		}
	}
	
	
	public Boolean getSimulationStarted() {
		return simulationStarted;
	}
	public void setSimulationStarted(Boolean simulationStarted) {
		this.simulationStarted = simulationStarted;
	}
	public Integer getTrajanjeGodina() {
		return trajanjeGodina;
	}
	public void setTrajanjeGodina(Integer trajanjeGodina) {
		this.trajanjeGodina = trajanjeGodina;
	}
	
	
	
	
	public Boolean getSimulationPaused() {
		return simulationPaused;
	}
	public void setSimulationPaused(Boolean simulationPaused) {
		this.simulationPaused = simulationPaused;
	}
	public void pauseSimulation() {
		simulationPaused=true;
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		Loto loto=null;
		Map map=(Map)session.getAttribute("simulacija");
		if (map != null) {
			loto=(Loto) map.get(trenutnaSimulacijaId);
			loto.pauseSimulation();
		}
	}
	
	public void restartSimulation() {
		pokreniSimulaciju();
	}
	
	public void resumeSimulation() {
		simulationPaused=false;
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		Loto loto=null;
		Map map=(Map)session.getAttribute("simulacija");
		if (map != null) {
			loto=(Loto) map.get(trenutnaSimulacijaId);
			loto.resumeSimulation();
		}
	}
	
	public void nextStep() {
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		Loto loto=null;
		Map map=(Map)session.getAttribute("simulacija");
		if (map != null) {
			loto=(Loto) map.get(trenutnaSimulacijaId);
			loto.nextStep();
			loto.kombinacijePojedinacno();
			RequestContext.getCurrentInstance().execute("nextDraw();");
		}
	}

	public StreamedContent getFile() {
		
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		Loto loto=null;
		Map map=(Map)session.getAttribute("simulacija");
		if (map != null) {
			
			try {
				loto=(Loto) map.get(trenutnaSimulacijaId);
				FileInputStream is=new FileInputStream(loto.getResultFile().getFile());
//				InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/images/optimus.jpg");
				file = new DefaultStreamedContent(is, "text/plain","simulation_results.txt");
				return file;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return null;
		
	}
	
	
	
	public String getSelectedNumbersTitle1() {
		return getSelectedNumbersTitle("1");
	}
	public String getSelectedNumbersTitle2() {
		return getSelectedNumbersTitle("2");
	}
	public String getSelectedNumbersTitle3() {
		return getSelectedNumbersTitle("3");
	}
	public String getSelectedNumbersTitle4() {
		return getSelectedNumbersTitle("4");
	}
	public String getSelectedNumbersTitle5() {
		return getSelectedNumbersTitle("5");
	}
	public String getSelectedNumbersTitle6() {
		return getSelectedNumbersTitle("6");
	}
	public String getSelectedNumbersTitle7() {
		return getSelectedNumbersTitle("7");
	}
	public String getSelectedNumbersTitle8() {
		return getSelectedNumbersTitle("8");
	}
	public String getSelectedNumbersTitle9() {
		return getSelectedNumbersTitle("9");
	}
	public String getSelectedNumbersTitle10() {
		return getSelectedNumbersTitle("10");
	}
	public String getSelectedNumbersTitle11() {
		return getSelectedNumbersTitle("11");
	}
	public String getSelectedNumbersTitle12() {
		return getSelectedNumbersTitle("12");
	}
		
	
	public String getSelectedNumbersColor1() {
		return getSelectedNumbersColor("1");
	}
	public String getSelectedNumbersColor2() {
		return getSelectedNumbersColor("2");
	}
	public String getSelectedNumbersColor3() {
		return getSelectedNumbersColor("3");
	}
	public String getSelectedNumbersColor4() {
		return getSelectedNumbersColor("4");
	}
	public String getSelectedNumbersColor5() {
		return getSelectedNumbersColor("5");
	}
	public String getSelectedNumbersColor6() {
		return getSelectedNumbersColor("6");
	}
	public String getSelectedNumbersColor7() {
		return getSelectedNumbersColor("7");
	}
	public String getSelectedNumbersColor8() {
		return getSelectedNumbersColor("8");
	}
	public String getSelectedNumbersColor9() {
		return getSelectedNumbersColor("9");
	}
	public String getSelectedNumbersColor10() {
		return getSelectedNumbersColor("10");
	}
	public String getSelectedNumbersColor11() {
		return getSelectedNumbersColor("11");
	}
	public String getSelectedNumbersColor12() {
		return getSelectedNumbersColor("12");
	}
	
	
	private int ispunjenoBrojevaUListicu(String listic) {
		try {
			OdigraniBrojevi brojevi=simulacija.getOdigraniBrojevi();
			Method method=brojevi.getClass().getMethod("getKombinacija"+listic, null);
			Kombinacija kombinacija=(Kombinacija) method.invoke(brojevi, null);
			return kombinacija.getBrojeviKombinacije().size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	public String getSelectedNumbersTitle(String ticket) {
		
		String title="Ticket "+ticket;
		Integer brojeva=ispunjenoBrojevaUListicu(ticket);
		if (brojeva.intValue() > 0) {
			title="Ticket "+ticket+" (" + brojeva + " / " + simulacija.getBrojeva()+")";
		}
		
		return title;
		
	}

	public String getSelectedNumbersColor(String ticket) {
		
		String title="";
		Integer brojeva=ispunjenoBrojevaUListicu(ticket);
		if (brojeva.intValue() > 0) {
			if (brojeva.intValue() == simulacija.getBrojeva().intValue()) {
				return "greenColoredPanel";
			}
			else {
				return "redColoredPanel";
			}
		}
		
		return title;
		
	}

	
}
