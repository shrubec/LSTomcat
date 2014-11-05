package hr.shrubec.simulator.facade;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class KontaktFacade implements Serializable{

	
	private String name;
	private String email;
	private String message;

	public void saveMessage() {
		 File file = new File("//home//simulator//kontakt.txt");
		 BufferedWriter output=null;
		 try {
			output = new BufferedWriter(new FileWriter(file,true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		 SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy HH:mm");
		 try {
			output.write(System.getProperty("line.separator")); 
			output.write(System.getProperty("line.separator")); 
			output.write("----------------------------------------"); 
			output.write(System.getProperty("line.separator")); 
			output.write("DATE: "+sdf.format(new Date()));
			output.write(System.getProperty("line.separator")); 
			output.write("NAME: "+name);
			output.write(System.getProperty("line.separator")); 
			output.write("EMAIL: "+email);
			output.write(System.getProperty("line.separator")); 
			output.write("MESSAGE: ");
			output.write(System.getProperty("line.separator")); 
			output.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		try {
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FacesMessage msg = new FacesMessage("Your message has been sent!", "Your message has been sent!");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        
        name=null;
        email=null;
        message=null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
