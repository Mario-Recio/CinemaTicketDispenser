
package Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class Theater implements Serializable {
    
    private int number;
    private int price;    
    private Film film; 
    private List<Session> sessions;
    private Set<Seat> seatSet;
    
    private int maxRow;
    private int maxCol;

    public Theater(int number, int price, Film film) {
        this.number = number;
        this.price = price;
        this.film = film;
        sessions = new ArrayList<Session>();
        seatSet = new TreeSet<Seat>();
        maxRow =0;
        maxCol =0;
    }
    
    public Theater(int number){
        this();
        this.number = number;
    }

    Theater() {
    	sessions = new ArrayList<Session>();
        seatSet = new TreeSet<Seat>();
    }

    
    
    
    
    
    public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}
        
        public void addSession(Session nueva){
            this.sessions.add(nueva);
        }

	public Set<Seat> getSeatSet() {
		return seatSet;
	}

	public void setSeatSet(Set<Seat> seatSet) {
		this.seatSet = seatSet;
	}

        public void addSeat(Seat nuevo){
            this.seatSet.add(nuevo);
            if(nuevo.getCol() > this.maxCol) 
                this.maxCol= nuevo.getCol();
            
            if(nuevo.getRow() > this.maxRow) 
                this.maxRow= nuevo.getRow();
        }
        
        public int getMaxRows(){
           return this.maxRow;
        }
   
        public int getMaxCol(){
            return this.maxCol;
        }

}


