
package Clases;
import sienens.CinemaTicketDispenser;



public  abstract class Operation { //clase abstracta de la que heredan todas las operaciones. Tiene dos metodos abstractos, uno para realizar la operacion, y otro para
                                   // obtener el nombre de dicha operacion
  
    protected Multiplex atm ;
    protected CinemaTicketDispenser dispenser;
    //UrjcBankServer bank = new UrjcBankServer();
    
    
    public abstract void doOperation();
    
    public Operation(CinemaTicketDispenser dispenser,Multiplex atm ){
    this.atm = atm;
    this.dispenser = dispenser;
    
   
    }
    
    
    public CinemaTicketDispenser getDispenser(){
    
    
     return dispenser;
    }
    
    
    public abstract String getTitle();
    
    public Multiplex getMultiplex(){
    
    
    	return atm;
    }
    
    
}
