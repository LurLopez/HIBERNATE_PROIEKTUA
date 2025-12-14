package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.primefaces.event.DateViewChangeEvent;
import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("queryRides")
@ViewScoped
public class QueryRidesBean implements Serializable {
	//private List<Ride> rides;
	private BLFacade blfacade;
	private String nondik;
	private String nora;
	private List<String> nondiklist = new ArrayList<String>();
	private List<String> noralist = new ArrayList<String>();
	private List<Ride> rides = new ArrayList<Ride>();
	private Date data;
    private User d;
    private Ride aukeratutakoRide;
	public QueryRidesBean() {
		d=LoginBean.getDriver();
		blfacade = FacadeBean.getBusinessLogic();
		System.out.println(d.getName());
	}
   
	//public List<Ride> getRides() {
		//	rides = blfacade.getRides(from, to, data);
			//System.out.println(rides);
			//return rides;
		
//	}

	public List<String> getNondiklist() {
		nondiklist = blfacade.getDepartCities();
		if (nondik == null) {
			nondik = nondiklist.get(0);
		}
		
		return nondiklist;
	}

	public List<String> getNoralist() {
		noralist = new ArrayList<String>();
		if (nondik != null) {
			noralist = blfacade.getDestinationCities(nondik);
			if (noralist.size() > 0) {
				nora = noralist.get(0);	
			}
		}
		return noralist;
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


	
	public void bidaiabilatu() {
		System.out.println(nondik);
		System.out.println(nora);
		System.out.println(data);
		List<Ride> bidaiak=blfacade.getRides(nondik, nora, data);
		System.out.println(bidaiak);
		rides=new ArrayList<Ride>();
		
		if(bidaiak.size()!=0) {
			for(Ride r:bidaiak) {
				
				//rides.add("nondik:"+ r.getFrom() + " nora:"+r.getTo()+ " data" + r.getDate() + " eserlekuak" + r.getnPlaces() + " prezioa" + r.getPrice() + " gidaria:" + r.getDriver().getEmail() +  " plaza libre kopurua:" + r.getPlazalibreak());
				rides.add(r);
				}
				
				
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null, 
			        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ez dago bidaiarik", "Saiatu beste data batekin"));
			
		}
		
	}
	
	public void bidaiaErreserbatu() {
		
		if (aukeratutakoRide !=null) {
			if(aukeratutakoRide.getPlazalibreak()>0) {
				blfacade.bidaiaErreserbatu(aukeratutakoRide,(Traveler) d);
				
				this.nondik = null;          
		        this.nora = null;            
		        this.data = null;           
		        this.rides = null;           
		        this.aukeratutakoRide = null; 
				FacesContext.getCurrentInstance().addMessage(null, 
		                new FacesMessage(FacesMessage.SEVERITY_INFO, "Bidaia ongi erreserbatu da", ""));
				return;
		        
			}
			else {
				FacesContext.getCurrentInstance().addMessage(null, 
		                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ez da eserleku librerik gelditzen", ""));
				return;
			}
		}
		else {
		FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aukeratu bidaia bat", ""));
	}
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public Ride getAukeratutakoRide() {
		return aukeratutakoRide;
	}

	public void setAukeratutakoRide(Ride aukeratutakoRide) {
		this.aukeratutakoRide = aukeratutakoRide;
	}


}
