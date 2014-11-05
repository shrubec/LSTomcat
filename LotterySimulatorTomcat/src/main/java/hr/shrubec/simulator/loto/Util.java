package hr.shrubec.simulator.loto;

import hr.shrubec.simulator.listic.Broj;

import java.util.Set;

public class Util {

	public static boolean isOdabrani(Set<Broj> brojevi,Integer odabrani) {
		
		for (Broj broj:brojevi) {
			if (broj.getBroj().intValue() == odabrani.intValue()) return true;
		}
		return false;
		
	}
	
}
