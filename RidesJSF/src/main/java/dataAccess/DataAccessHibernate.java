	package dataAccess;
	
	import javax.persistence.EntityManager;
	import javax.persistence.EntityManagerFactory;
	import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
	
	import configuration.ConfigXML;
	import configuration.UtilDate;
	import domain.Driver;
import domain.Erreserba;
import domain.Ride;
import domain.Traveler;
import domain.User;
import eredua.JPAUtil;
	import exceptions.RideAlreadyExistException;
	import exceptions.RideMustBeLaterThanTodayException;

import java.io.File;
import java.util.*;
	
	public class DataAccessHibernate {
	    
	    private  EntityManagerFactory emf;
	    ConfigXML c=ConfigXML.getInstance();
	    public DataAccessHibernate() {
			if (c.isDatabaseInitialized()) {
				String fileName=c.getDbFilename();

				File fileToDelete= new File(fileName);
				if(fileToDelete.delete()){
					File fileToDeleteTemp= new File(fileName+"$");
					fileToDeleteTemp.delete();

					  System.out.println("File deleted");
					} else {
					  System.out.println("Operation failed");
					}
			}
			
			if  (c.isDatabaseInitialized())initializeDB();
			
			System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());

			

	    	
	    }
	    
	public void initializeDB(){
		System.out.println("Bidaien sorkuntza:");
		Driver driver1=new Driver("driver1@gmail.com","Aitor Fernandez", "Aitor");
		Driver driver2=new Driver("driver2@gmail.com","Ane Gaztañaga", "Ane");
		Driver driver3=new Driver("driver3@gmail.com","Test driver", "Proba");
		
		 Calendar today = Calendar.getInstance();
		   
		   int month=today.get(Calendar.MONTH);
		   int year=today.get(Calendar.YEAR);
		   if (month==12) { month=1; year+=1;}
		
		try {
			driverraErregistratu(driver1);
			driverraErregistratu(driver2);
			driverraErregistratu(driver3);	
			System.out.println("aigu");
		
		createRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 4, 7,"Aitor Fernandez");
		createRide("Donostia", "Gazteiz", UtilDate.newDate(year,month,6), 4, 8,"Aitor Fernandez");
		createRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 4, 4,"Aitor Fernandez");

		createRide("Donostia", "Iruña", UtilDate.newDate(year,month,7), 4, 8,"Aitor Fernandez");
		
		createRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 3, 3,"Ane Gaztañaga");
		createRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 2, 5,"Ane Gaztañaga");
		createRide("Eibar", "Gasteiz", UtilDate.newDate(year,month,6), 2, 5,"Ane Gaztañaga");

		createRide("Bilbo", "Donostia", UtilDate.newDate(year,month,14), 1, 3,"Test driver");
	
		List<String> emaitza=getDepartCities();
		for(String ema:emaitza) {
			System.out.println(ema);
		}
		
		System.out.println("Donostiatik ateratzen diren bidaien helmugak");
		List<String> destcities=getArrivalCities("Donostia");
		for(String dest:destcities) {
			System.out.println(dest);
		}
		
		
		System.out.println("Nondik: Donostia  Nora:Bilbo  Data:"+UtilDate.newDate(year,month,15) + " bidaia datu basean bilatu:");
		List<Ride> bidaia_donostia=getRides("Donostia", "Bilbo", UtilDate.newDate(year,month,15));
		for(Ride bidaia:bidaia_donostia) {
			System.out.println(bidaia.getFrom() + bidaia.getTo() + bidaia.getDate());
		}
		
		System.out.println(month+1 + ". hilabetean Donostia Bilbo dauden bidaiak:");
		List<Date> bidaia_hilabete=getThisMonthDatesWithRides("Donostia", "Bilbo", UtilDate.newDate(year,month,15)); //Egun bera bada, behin bakarrik agertzen da
		System.out.println(bidaia_hilabete.size());
		for(Date bidaia:bidaia_hilabete) {
			System.out.println(bidaia);
		}
	}catch (Exception e1){
		e1.printStackTrace();
	}
	}
		public List<String> getDepartCities(){
		EntityManager em = JPAUtil.getEntityManager(); 
		TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
		List<String> cities = query.getResultList();
		if (em != null && em.isOpen()) 
            em.close();
		
		return cities;
	

	}
	
		public List<String> getArrivalCities(String from){
			EntityManager em = JPAUtil.getEntityManager(); 
			TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",String.class);
			query.setParameter(1, from);
			List<String> arrivingCities = query.getResultList(); 
			if (em != null && em.isOpen()) 
	            em.close();
			return arrivingCities;
			

		}
		
		
		
	
		
	    
	
	    public Ride createRide(String from, String to, Date date, int nPlaces, float price, String username) 
	            throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
	        
	        System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + username + " date " + date);
	        
	 
	        EntityManager em = JPAUtil.getEntityManager(); 
	        
	        try {
	            if (new Date().compareTo(date) > 0) {
	                throw new RideMustBeLaterThanTodayException(
	                    ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday")
	                );
	            }
	
	            em.getTransaction().begin();
	            
	            Driver driver = em.find(Driver.class, username);
	            
	            if (driver == null) {
	                // Driver-a existitzen ez bada, sortu egin dezakegu probetarako
	                // edo errore bat bota. Kode honetan suposatzen dugu existitu behar dela.
	                 throw new NullPointerException("Driver ez da aurkitu DBan: " + username);
	            }
	
	            if (driver.doesRideExists(from, to, date)) {
	                em.getTransaction().commit();
	                throw new RideAlreadyExistException(
	                    ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist")
	                );
	            }
	
	            System.out.println("crea addRide");
	            Ride ride = driver.addRide(from, to, date, nPlaces, price);
	            
	            em.getTransaction().commit();
	            
	            return ride;
	
	        } catch (Exception e) {
	            if (em.getTransaction().isActive()) {
	                em.getTransaction().rollback();
	            }
	            
	            if (e instanceof RideAlreadyExistException) throw (RideAlreadyExistException)e;
	            if (e instanceof RideMustBeLaterThanTodayException) throw (RideMustBeLaterThanTodayException)e;
	            
	            e.printStackTrace();
	            return null;
	            
	        } finally {
	            if (em != null && em.isOpen()) {
	                em.close();
	            }
	        }
	    }
	    
	    
	    
		public User erregistratu(String username, String pass, String mail, String mota) {
			EntityManager db = JPAUtil.getEntityManager(); 
			db.getTransaction().begin();
			User u = db.find(User.class, username);;
			User j = null;
			System.out.println(mota);
			if(u == null) {
				if(mota.equals("Driver")) {
					j = new Driver(mail,username,pass); 
					db.persist(j);
				}else if(mota.equals("Traveler")){
					j = new Traveler(mail,username,pass);
					db.persist(j);
					
				}
			}else {
				System.out.println("Gidari hau erregistratuta dago jada");
			}
			db.getTransaction().commit();
			
			return j;
		}
			
		
		public List<Ride> getRides(String from, String to, Date date) {
	        EntityManager em = JPAUtil.getEntityManager(); 
			System.out.println(">> DataAccess: getRides=> from= "+from+" to= "+to+" date "+date);
	
			List<Ride> res = new ArrayList<Ride>();	
			TypedQuery<Ride> query = em.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3",Ride.class);   
			query.setParameter(1, from);
			query.setParameter(2, to);
			query.setParameter(3, date);
			List<Ride> rides = query.getResultList();
		 	 for (Ride ride:rides){
			   res.add(ride);
			  }
	        if (em != null && em.isOpen()) {
	           em.close();
	        }
		 	return res;
		 	
		
		}
		
		
		public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
			EntityManager em = JPAUtil.getEntityManager(); 
			System.out.println(">> DataAccess: getEventsMonth");
			List<Date> res = new ArrayList<Date>();	
			
			Date firstDayMonthDate= UtilDate.firstDayMonth(date);
			Date lastDayMonthDate= UtilDate.lastDayMonth(date);
					
			
			TypedQuery<Date> query = em.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class);   
			
			query.setParameter(1, from);
			query.setParameter(2, to);
			query.setParameter(3, firstDayMonthDate);
			query.setParameter(4, lastDayMonthDate);
			List<Date> dates = query.getResultList();
		 	 for (Date d:dates){

			   res.add(d);
			  }
		     if (em != null && em.isOpen()) {
			    em.close();
			 }
		 	return res;
		 	
		}
		
		public void driverraErregistratu(Driver d) {
			EntityManager session = JPAUtil.getEntityManager();
			session.getTransaction().begin();
			session.persist(d);
			session.getTransaction().commit();
			session.close();
			
		}
	
		
		public User login(String username, String pass) {
			EntityManager db = JPAUtil.getEntityManager(); 
			db.getTransaction().begin();
			User u = db.find(User.class, username);
			
			if(u == null) {
				System.out.println("Erabiltzaile hau ez dago erregistratuta");
			
				db.getTransaction().commit();
				return u;
			}else {
				if(u.getPassword().equals(pass)) {
					System.out.println("Saioa hasi da");
					db.getTransaction().commit();
					System.out.println(u.getName());
					return u;
				}else {
					System.out.println("Pasahitza eta erabiltzailea ez datoz bat");
					db.getTransaction().commit();
					return null;
				}
			}

		}
		
		public boolean bidaiaErreserbatu(Ride r, Traveler t) {
			EntityManager db = JPAUtil.getEntityManager(); 
			db.getTransaction().begin();
			Ride ride = db.find(Ride.class, r.getRideNumber());
			for(Erreserba e:ride.getErreserbalist()) {
				if(e.erreserbatu(t)) {
					ride.setPlazalibreak(ride.getPlazalibreak()-1);
					db.getTransaction().commit();
					System.out.println("Eserlekua erreserbatu da.");
					return true;
				}
			}
			System.out.println("Arazoren bat egon da");
			return false;
		}
		
		public List<User> userrakBilatu(){
			EntityManager em = JPAUtil.getEntityManager(); 
			TypedQuery<User> query = em.createQuery("SELECT r FROM User r",User.class);   
			List<User> res = new ArrayList<User>();	
			List<User> userrak = query.getResultList();
		 	 for (User u:userrak){
			   res.add(u);
			  }
	        if (em != null && em.isOpen()) {
	           em.close();
	        }
		 	return res;
			
		}
		
		
		public List<String> ridakbilatu(String u, Date d) {
		    EntityManager em = JPAUtil.getEntityManager(); 
		    System.out.println(">> DataAccess: getEventsMonth");


		    Query query = em.createNativeQuery("SELECT DISTINCT r.rides_rideNumber FROM User_Ride r WHERE r.User_erabiltzaileizena = ?1");   
		    
		    query.setParameter(1, u);
		    
	
		    List<Object> rawResult = query.getResultList();
		    List<String> emaitza = new ArrayList<>();
		    

		    for (Object o : rawResult) {
		        if (o != null) {
		            emaitza.add(o.toString());
		        }
		    }

		    if (em != null && em.isOpen()) {
		       em.close();
		    }
		    return emaitza;
		
		}
		
		
		
		
		
		public static void main(String[] args) {
			DataAccessHibernate e = new DataAccessHibernate();
			System.out.println("Bidaien sorkuntza:");
			Driver driver1=new Driver("driver1@gmail.com","Aitor Fernandez","Aitor");
			Driver driver2=new Driver("driver2@gmail.com","Ane Gaztañaga","Ane");
			Driver driver3=new Driver("driver3@gmail.com","Test driver","Proba");
			
			 Calendar today = Calendar.getInstance();
			   
			   int month=today.get(Calendar.MONTH);
			   int year=today.get(Calendar.YEAR);
			   if (month==12) { month=1; year+=1;}
			
			try {
				e.driverraErregistratu(driver1);
				e.driverraErregistratu(driver2);
				e.driverraErregistratu(driver3);	
				System.out.println("aigu");
			
			e.createRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 4, 7,"Aitor Fernandez");
			e.createRide("Donostia", "Gazteiz", UtilDate.newDate(year,month,6), 4, 8,"Aitor Fernandez");
			e.createRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 4, 4,"Aitor Fernandez");
	
			e.createRide("Donostia", "Iruña", UtilDate.newDate(year,month,7), 4, 8,"Aitor Fernandez");
			
			e.createRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 3, 3,"Ane Gaztañaga");
			e.createRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 2, 5,"Ane Gaztañaga");
			e.createRide("Eibar", "Gasteiz", UtilDate.newDate(year,month,6), 2, 5,"Ane Gaztañaga");
	
			e.createRide("Bilbo", "Donostia", UtilDate.newDate(year,month,14), 1, 3,"Test driver");
		
			List<String> emaitza=e.getDepartCities();
			for(String ema:emaitza) {
				System.out.println(ema);
			}
			
			System.out.println("Donostiatik ateratzen diren bidaien helmugak");
			List<String> destcities=e.getArrivalCities("Donostia");
			for(String dest:destcities) {
				System.out.println(dest);
			}
			
			
			System.out.println("Nondik: Donostia  Nora:Bilbo  Data:"+UtilDate.newDate(year,month,15) + " bidaia datu basean bilatu:");
			List<Ride> bidaia_donostia=e.getRides("Donostia", "Bilbo", UtilDate.newDate(year,month,15));
			for(Ride bidaia:bidaia_donostia) {
				System.out.println(bidaia.getFrom() + bidaia.getTo() + bidaia.getDate());
			}
			
			System.out.println(month+1 + ". hilabetean Donostia Bilbo dauden bidaiak:");
			List<Date> bidaia_hilabete=e.getThisMonthDatesWithRides("Donostia", "Bilbo", UtilDate.newDate(year,month,15)); //Egun bera bada, behin bakarrik agertzen da
			System.out.println(bidaia_hilabete.size());
			for(Date bidaia:bidaia_hilabete) {
				System.out.println(bidaia);
			}
			
		}
		catch (Exception e1){
			e1.printStackTrace();
		}
			//
		}
	    
	
	}