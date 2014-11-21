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

	@SuppressWarnings("serial")
	private static final List<String> lista = new ArrayList<String>() {
	    {
	    	add("\"All it takes is a dollar and another dollar.  Then another dollar.  And another...\"");
			add("\"The Lottery... get rich or die trying!\"");
			add("\"Hey, crazier things have happened!\"");
			add("\"Work?  Save?  Invest?  Screw That!\"");
			add("\"The Lottery... if you don’t keep playing we’ll just raise your taxes.\"");
			add("\"You play to win the Lottery!  You don’t play just to play it!\"");
			add("\"Sure, you’re more likely to get hit by lightning... but the lottery is more fun.\"");
			add("\"It could be you! (But don’t get your hopes up).\"");
			add("\"There really is a sucker born every minute.\"");
			add("\"You know you’d just throw your money away anyway.\"");
	    }
	};
	

	public String getSlogan() {
		Ranecu ranescu=new Ranecu(new Date());
		Integer index=ranescu.choose(0, lista.size()-1);
		return lista.get(index);
	}

	
}
