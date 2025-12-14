package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Ride;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
@Named("createrides")
@ViewScoped
public class CreateRidesBean implements Serializable {
    private String nondik;
    private String nora;
    private String eserlekuak;
    private String prezioa;
    private Date data;
    private User d;
    private BLFacade blfacade;
    
    
    public CreateRidesBean() {
    	d=LoginBean.getDriver();
    	blfacade = FacadeBean.getBusinessLogic();
    }
    
    public String getNondik() {
        return nondik;
    }

    public void setNondik(String nondik) {
        this.nondik = nondik;
    }

    public String getNora() {
        return nora;
    }

    public void setNora(String nora) {
        this.nora = nora;
    }

    public String getEserlekuak() {
        return eserlekuak;
    }

    public void setEserlekuak(String eserlekuak) {
        this.eserlekuak = eserlekuak;
    }

    public String getPrezioa() {
        return prezioa;
    }

    public void setPrezioa(String prezioa) {
        this.prezioa = prezioa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void bidali() {
    	System.out.println(nondik);
    	System.out.println(nora);
    	System.out.println(eserlekuak);
    	System.out.println(prezioa);
    	System.out.println(data);
    	System.out.println("balioak");
    	
    	if(nondik=="" || nora=="" || eserlekuak=="" || prezioa=="" || data==null) {
    		FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Balio bat falta da","" ));
    	}
    	else {
    	try {
    	blfacade.createRide(nondik, nora, data, Integer.parseInt(eserlekuak), Integer.parseInt(prezioa),d.getName());
    	for(String bidaia:blfacade.getDepartCities()) {
    		System.out.println(bidaia);
    	}
    	
    	FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Bidaia egoki sortu da.", ""));
    	
        nondik = "";
        nora = "";
        eserlekuak = "";
        prezioa = "";
        data = null;
    	
    }catch (RideAlreadyExistException e) {
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bidaia jadanik existitzen da.", ""));
        } catch (RideMustBeLaterThanTodayException e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bidaia gaur baino beranduago izan behar da.","" ));
        } catch (java.lang.NumberFormatException e) {
        	 FacesContext.getCurrentInstance().addMessage(null, 
                     new FacesMessage(FacesMessage.SEVERITY_ERROR, "Prezioak eta Eserlekuak zenbaki positibo bat izan behar dute","" ));
        }
    	catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore ezezagun bat gertatu da.","" ));
        }
    

    	}
    }
    
}
