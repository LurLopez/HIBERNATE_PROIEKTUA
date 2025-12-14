package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;



import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Ride;
import domain.Traveler;
import domain.User;
import domain.Driver;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;
import dataAccess.DataAccessHibernate;
/**
 * It implements the business logic as a web service.
 */

public class BLFacadeImplementation  implements BLFacade {
	DataAccessHibernate dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new DataAccessHibernate();
		    
		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(DataAccessHibernate da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c=ConfigXML.getInstance();
		
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
    public List<String> getDepartCities(){
    	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	public List<String> getDestinationCities(String from){
		
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
   
   public Ride createRide( String from, String to, Date date, int nPlaces, float price ,String username) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   
		
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price,username);		
		
		return ride;
   };
	
   /**
    * {@inheritDoc}
    */
	
	public List<Ride> getRides(String from, String to, Date date){
		
		List<Ride>  rides=dbManager.getRides(from, to, date);
		
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		
		return dates;
	}
	
	


	/**
	 * {@inheritDoc}	
	 */
    
	
    public User login(String log, String pass) {
    	
    	User u = dbManager.login(log,pass);
    	

    	return u;
	}
    
    public User erregistratu(String username, String pass, String mail, String mota) {
    	return dbManager.erregistratu(username, pass, mail, mota);
    }
	
    public boolean bidaiaErreserbatu(Ride r, Traveler t) {
    	return dbManager.bidaiaErreserbatu(r,t);
    }
    
	
	 public void initializeBD(){
    	
		dbManager.initializeDB();
		
	}
	 
	 
	 public List<User> userrakBilatu(){
		 
		 return dbManager.userrakBilatu();
	 }
	 
	 public List<String> ridakbilatu(String u,Date d){
		 return dbManager.ridakbilatu(u,d);
	 }
	 
}

