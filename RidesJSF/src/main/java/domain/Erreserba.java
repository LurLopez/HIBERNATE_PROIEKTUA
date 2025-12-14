package domain;

import javax.persistence.*;
@Entity
public class Erreserba {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
	private int erreserbaId;
	
	private boolean eskatuta;
	
	@ManyToOne
	private Traveler traveler;
	
	public Ride getBidaia() {
		return bidaia;
	}


	public void setBidaia(Ride bidaia) {
		this.bidaia = bidaia;
	}

	@ManyToOne
	private Ride bidaia;

	
	





	public Erreserba() {
		eskatuta = false;
		traveler = null;

	}
	
	
	public Erreserba(Ride kodea) {
		eskatuta = false;
		traveler = null;
		this.bidaia=kodea;
	}
	
	public Erreserba(Ride kodea,Traveler t,boolean erreserbatuta) {     //ESKAERAK SORTZEKO ERAIKITZAILEA
		traveler = t;
		this.bidaia=kodea;
		this.eskatuta=erreserbatuta;
	}
	
	

	
	
	

	
	
	public int getId() {
		return erreserbaId;
	}


	public void setId(int id) {
		erreserbaId = id;
	}


	public Ride getDriverKodea() {
		return bidaia;
	}


	public void setDriverKodea(Ride driverKodea) {
		bidaia = driverKodea;
	}

	
	
	
	public Traveler getTraveler() {
		return traveler;
	}


	
	
	
	public boolean getErreserbatuta() {
		return this.eskatuta;
	}
	

	
	public void setErreserbatuta(boolean ezarri) {
		this.eskatuta = ezarri;
	}
	public void setTraveler(Traveler t) {
		this.traveler = t;
	}
	
	public boolean erreserbatu(Traveler t) {
		if(eskatuta) {
			return false;
		}else {
			this.setErreserbatuta(true);
			this.setTraveler(t);
			return true;
		}
	}
	
	public String toString(){
		if(traveler !=null) 
		return erreserbaId+";;"+traveler.getName()+";";
		
		else return erreserbaId+";;";
	}
	
}
