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
@Named("antidopping")
@SessionScoped
public class AntidoppingBean implements Serializable {
	private List<String> userlist = new ArrayList<String>();
	private String aukeratutakouserra;
    private Date data;
    private BLFacade blfacade;
    private List<String> bidaialist = new ArrayList<String>();
    
    public AntidoppingBean() {
    	blfacade = FacadeBean.getBusinessLogic();
    }
    
    

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }







	public String getAukeratutakouserra() {
		return aukeratutakouserra;
	}



	public void setAukeratutakouserra(String aukeratutakouserra) {
		System.out.println("aaaaaaa");
		this.aukeratutakouserra = aukeratutakouserra;
	}



	public List<String> getUserlist() {
		List<User> userrak=blfacade.userrakBilatu();
		List<String> userstring=new ArrayList<String>();
		for(User u:userrak) {
			userstring.add(u.getErabiltzaileIzena());
		}
		return userstring;
	}



	public void setUserlist(List<String> userlist) {
		
		this.userlist = userlist;
	}


	public void bidaiakerakutsi() {
		System.out.println(data);
		System.out.println(aukeratutakouserra);
		bidaialist=blfacade.ridakbilatu(aukeratutakouserra, data);	
	}



	public List<String> getBidaialist() {
		return bidaialist;
	}



	public void setBidaialist(List<String> bidaialist) {
		this.bidaialist = bidaialist;
	}
   
    
}
