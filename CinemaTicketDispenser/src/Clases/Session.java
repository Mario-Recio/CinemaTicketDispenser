
package Clases;


import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;


public class Session implements Serializable {
 
    private Pair<Integer, Integer> hour;
  
    private Set <Seat> occupiedSeatSet;
    
    public Session() {
    	occupiedSeatSet= new TreeSet<>();
    }         
    
    public Session(int hora, int minutos) {
        hour = new Pair<>(hora, minutos);

        occupiedSeatSet= new TreeSet<Seat>();
    }
    
    public Session(String hora, String minutos){
        this(Integer.valueOf(hora), Integer.valueOf(minutos));
    }
    
    
    public boolean isOccupied(int row, int col){    
    	return occupiedSeatSet.contains(new Seat(row, col));
    }
    
    
    public void occupiesSeat(int row, int col){
        
        occupiedSeatSet.add(new Seat(row, col));
    
    }
    
    public void unocupiesSeat(int row, int col){
        occupiedSeatSet.remove(new Seat(row, col));
    
    }
    
    public Pair<Integer, Integer> getHour(){
        return hour;
    }
   
    @Override
    public String toString(){ //necesario para poder mostrar la hora en el formato correcto. Hay que añadir un 0 manualmente si los minutos son < 10
        String complemento ="";
        if(hour.snd <10) complemento ="0";
        return hour.fst+":"+complemento+hour.snd;
    }
    
    
    
    class Pair<E,T> implements Serializable{
        
        E fst;
        T snd;
        
        Pair(E fst, T snd){
            this.fst= fst;
            this.snd= snd;
        }
    }
   
    
    
    
    
    
    
    
    
    
 
    
}
