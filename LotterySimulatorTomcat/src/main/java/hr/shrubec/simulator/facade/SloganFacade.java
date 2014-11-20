package hr.shrubec.simulator.facade;

import hr.shrubec.simulator.random.Ranecu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class SloganFacade {

	private final List<String> lista;
	
	public SloganFacade() {
		lista=new ArrayList<String>();
		lista.add("All it takes is a dollar and another dollar.  Then another dollar.  And another...");
		lista.add("The Lottery... get rich or die trying!");
		lista.add("Hey, crazier things have happened!");
		lista.add("Work?  Save?  Invest?  Screw That!");
		lista.add("The Lottery... if you don’t keep playing we’ll just raise your taxes.");
		lista.add("You play to win the Lottery!  You don’t play just to play it!");
		lista.add("Sure, you’re more likely to get hit by lightning... but the lottery is more fun.");
		lista.add("It could be you! (But don’t get your hopes up).");
		lista.add("There really is a sucker born every minute.");
		lista.add("You know you’d just throw your money away anyway.");
	}

	public String getSlogan() {
		
		Ranecu ranescu=new Ranecu(new Date());
		Integer index=ranescu.choose(0, lista.size()-1);
		return lista.get(index);
	}

	
	
}
