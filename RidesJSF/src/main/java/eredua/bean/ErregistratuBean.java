package eredua.bean;

import businessLogic.BLFacade;
import domain.Driver;
import domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("erregistratu")
@ApplicationScoped
public class ErregistratuBean {
	private String izena;
	private String pasahitza;
	private String pasahitza_konfirmatu;
	private String email;
	private BLFacade blfacade;
	private String mota;

	public ErregistratuBean(){
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email=email;
	}
	
	public String getPasahitza_konfirmatu() {
		return pasahitza_konfirmatu;
	}

	public void setPasahitza_konfirmatu(String pasahitza_konfirmatu) {
		this.pasahitza_konfirmatu = pasahitza_konfirmatu;
	}
	
	public void Erregistratu() {
		System.out.println(this.mota);
		
	    if (izena == null || izena.trim().isEmpty() || 
	        email == null || email.trim().isEmpty() || 
	        pasahitza == null || pasahitza.trim().isEmpty() || 
	        pasahitza_konfirmatu == null || pasahitza_konfirmatu.trim().isEmpty() ||
	        mota==null) {
	        
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Balio bat falta da", ""));
	        return;
	    }

	    if (!pasahitza.equals(pasahitza_konfirmatu)) {
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bi pasahitzak desberdinak dira", ""));
	        return;
	    }

	    try {
	        User u = blfacade.erregistratu(izena, pasahitza, email,mota);

	        if (u != null) {
	            System.out.println("Izena: " + u.getName() + " Pasahitza: " + u.getPassword() + " Emaila: " + u.getEmail());
	            FacesContext.getCurrentInstance().addMessage(null, 
	                new FacesMessage(FacesMessage.SEVERITY_INFO, "Erabiltzailea ongi erregistratu da", ""));
	        } else {
	            System.out.println("ez da ongi sortu");
	            FacesContext.getCurrentInstance().addMessage(null, 
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erabiltzailea dagoeneko existitzen da", ""));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Arazo bat egon da", ""));
	    }
	}

	public String getMota() {
		return mota;
	}

	public void setMota(String mota) {
		this.mota = mota;
	}

}
