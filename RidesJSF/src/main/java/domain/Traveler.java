package domain;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Traveler extends User {
	
	public Traveler() {
		super();
	}
	
	
	public Traveler(String mail, String izena, String pasahitza) {
		super(izena,pasahitza,mail);
		
	}
}
