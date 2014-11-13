package hr.shrubec.simulator.util;

import hr.shrubec.simulator.listic.Broj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class SimulacijaResultFile {

	private File file = null; 
	private BufferedWriter output=null;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
//	final static String path="//home/simulator//";
	final static String path="C:\\";
	
	public SimulacijaResultFile(String filename) {
		
		String folder=path + sdf.format(new Date())+"\\";
		File dir = new File(folder);
		dir.mkdir();
		file = new File(folder + filename+ ".txt");
		
		file = new File(folder+filename+".txt");
		try {
			output = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void appendIzvuceno(Set<Broj> izvucenaKombinacija,Integer kolo,Date datum) throws IOException {
		output.write("\r\n");
		output.write("\r\n");
		output.write("-----------------------------------------------------------------------------------");
		output.write("\r\n");
		output.write("\r\n");
		output.write("Drawing " + kolo + ", " + sdf.format(datum));
		output.write("\r\n");
		output.write("Winning numbers: ");
		String s="";
		for (Broj broj:izvucenaKombinacija) {
			s=s+broj.getBroj()+", ";
		}
		output.write(s.substring(0,s.lastIndexOf(",")));
		output.write("\r\n");
		
	}
	
	public void appendOdigrano(Set<Broj> zaokruzeno) throws IOException {
		
		output.newLine();
		output.write("My numbers: ");
		
		String s="";
		for (Broj broj:zaokruzeno) {
			s=s+broj.getBroj()+", ";
		}
		output.write(s);
		output.write("Match: ");
		s="";
		for (Broj broj:zaokruzeno) {
			if (broj.getPogodjen()) {
				s=s+broj.getBroj()+", ";
			}
		}
		
		if (s.contains(",")) 
			s=s.substring(0,s.lastIndexOf(","));
		output.write(s);
		output.flush();
	}
	
	public void closeFile() {
		try {
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFile() throws IOException {
		boolean deleted=false;
		if (file.exists()) {
			closeFile();
			try {
				output = null;
				System.gc();
				deleted=file.delete();
			} catch (Exception e) {
				System.out.println("GRESKA KOD BRISANJA");
				e.printStackTrace();
			}
		}
		
	}



	public File getFile() {
		return file;
	}
	
	
	
	public static void saveSimulacija(String tip,String dani,String selectedNumbers) {
		SimpleDateFormat format=new SimpleDateFormat("dd.MM.yyyy HH:mm");
		BufferedWriter output=null;
		try {
			 File file = new File(path+"simulacije_info.txt");
			output = new BufferedWriter(new FileWriter(file,true));
			output.write("\r\n");
			output.write(format.format(new Date()) + ", "
					+ tip + ", "
					+ dani + ", "
					+ selectedNumbers);
			output.write("\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.flush();
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
