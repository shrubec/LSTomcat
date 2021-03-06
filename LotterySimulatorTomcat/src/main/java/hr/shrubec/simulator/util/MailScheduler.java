package hr.shrubec.simulator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.io.FileUtils;

@SuppressWarnings("serial")
public class MailScheduler extends HttpServlet{

	Logger LOGGER=Logger.getLogger(MailScheduler.class.getName());
	final String path="//home/simulator//";
//	final String path="D:\\";
	final String username = "lotosimulator@gmail.com ";
	final String password = "simulator0810";
	final int HOUR=8;
	final int MINUTE=0;
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public void init() throws ServletException {
		LOGGER.info("----------");
		LOGGER.info("---------- MailScheduler inicijaliziran ----------");
		LOGGER.info("----------");
		start();
	}
	 
	 public void start() {
			final Runnable thread = new Runnable() {
				public void run() {
					if (sendMail(readFiles())) {
						clearFiles();
					}
					deleteSimulationFolders();
				}
			};
			scheduler.scheduleAtFixedRate(thread, getDelay(), 43200, TimeUnit.SECONDS);
		}
	 
	 private String readFiles() {
		 	String s0=memorija();
		 	String s1= readFile("kontakt.txt");
			String s2= readFile("simulacije_info.txt");
			if (s1.trim().isEmpty()) s1="Nema novih kontakata";
			if (s2.trim().isEmpty()) s2="Nema novih simulacija";
			String s3=s0+System.getProperty("line.separator")+s1+System.getProperty("line.separator")+s2;
			return s3;
		 }
	 
	 private String readFile(String fileName){
		 String message="";
		 File file = new File(path+fileName);
		 if (file != null && file.exists())  {
	    		try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					while (line != null) {
						sb.append(line);
						sb.append(System.getProperty("line.separator"));
						line = br.readLine();
					}
					message = message+System.getProperty("line.separator")+sb.toString();
					br.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		
	    	}
	    	else {
	    		message=message+System.getProperty("line.separator")+"Datoteka " + fileName +" ne postoji";
	    		
	    	}
		 
		 LOGGER.info(message);
		 return message;
	 }
	 
	 
	 
	 
	 private Long getDelay() {
		 Calendar cal=Calendar.getInstance();
		 cal.set(Calendar.HOUR, HOUR);
		 cal.set(Calendar.MINUTE, MINUTE);
		 cal.set(Calendar.SECOND, 0);
		 Calendar cal1=Calendar.getInstance();
		 if (cal1.after(cal)) {
			 cal.add(Calendar.DAY_OF_YEAR, 1);
		 }

		 Long delay=  (cal.getTimeInMillis()-cal1.getTimeInMillis()) / 1000;
		 LOGGER.info("Mail Scheduler Delay: " + delay);
		 return delay;
		 
	 }
	 
	private String memorija() {
		long allocatedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
		long presumableFreeMemory = Runtime.getRuntime().maxMemory()- allocatedMemory;
		String s0 = "Trenutno vrijeme: " + new Date();
		String s1 = "Ukupno memorije: " + Runtime.getRuntime().totalMemory()/ 1000;
		String s2 = "Alocirano: " + allocatedMemory / 1000;
		String s3 = "Pretpostavljeno slobodno: " + presumableFreeMemory / 1000;
		String s4 = "Slobodno: " + Runtime.getRuntime().freeMemory() / 1000;
		StringBuilder sb = new StringBuilder();
		sb.append(s0);
		sb.append(System.getProperty("line.separator"));
		sb.append(s1);
		sb.append(System.getProperty("line.separator"));
		sb.append(s2);
		sb.append(System.getProperty("line.separator"));
		sb.append(s3);
		sb.append(System.getProperty("line.separator"));
		sb.append(s4);
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}
	
	
	private boolean sendMail(String poruka) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("shrubec@gmail.com"));
			message.setSubject("Lottery Simulator Info");
			message.setText(poruka);
			Transport.send(message);
			LOGGER.info("Mail uspjesno poslan");
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private void clearFiles() {
		clearFile("simulacije_info.txt");
//		clearFile("kontakt.txt");
	}
	
	private void clearFile(String fileName) {
		File file = new File(path+fileName);
		if (file.exists()) {
			try {
				FileOutputStream writer = new FileOutputStream(file);
				writer.write(0);
				writer.flush();
				writer.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	

	private void deleteSimulationFolders() {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			for (int i=1; i <=10; i++) {
				int k=i*-1;
				Calendar cal=Calendar.getInstance();
				cal.add(Calendar.DAY_OF_YEAR,k);
				String folder=path+sdf.format(cal.getTime());
				File file=new File(folder);
				if (file.exists()) {
					LOGGER.info("Brisem folder " + folder);
					try {
						FileUtils.deleteDirectory(file);
						LOGGER.info("Folder " + folder + " obrisan!");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
}
