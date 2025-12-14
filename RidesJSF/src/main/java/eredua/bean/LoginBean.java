package eredua.bean;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Traveler;
import domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("login")
@ApplicationScoped
public class LoginBean {
	private String izena;
	private String pasahitza;
	private BLFacade blfacade;
	private static User d;
	public LoginBean(){
		blfacade = FacadeBean.getBusinessLogic();
	}
	public String getIzena() {
		return izena;
	}
	public void setIzena(String izena) {
		this.izena = izena;
	}
	public String getPasahitza() {
		return pasahitza;
	}
	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}
	
	public String loginEgin() {

	    if (izena == null || izena.trim().isEmpty() || 
	        pasahitza == null || pasahitza.trim().isEmpty()) {
	        
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erabiltzailea eta pasahitza beharrezkoak dira", ""));
	        return null;
	    }

	    try {
	        d = blfacade.login(izena, pasahitza);

	        if (d == null) {
	            System.out.println("Login okerra saiakera: " + izena);
	            FacesContext.getCurrentInstance().addMessage(null, 
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erabiltzailea edo pasahitza okerrak dira", ""));
	            return null;
	        } else {
	        	System.out.println("Login zuzena: " + d.getName() + " (" + d.getEmail() + ")");
	        	if (d instanceof Driver) {
	        		return "Menu?faces-redirect=true";
	        	}
	            if (d instanceof Traveler) {
	            	System.out.println("traveler");
	            	return "Bidaia_Erreserbatu?faces-redirect=true";
	            }
	            
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errorea sisteman saioa hastean", ""));
	        return null;
	    }
	}
	public static User getDriver() {
		return d;
	}
}
