
package Clases;

import java.util.ResourceBundle;
import sienens.CinemaTicketDispenser;


public class IdiomSelection extends Operation {
    
    
    
    public void doOperation(){ //inicializa el idioma del sistema a la seleccion del usuario
    
       this.atm.mostrarIdiomas();
       char seleccion;
         
            do{
              seleccion = dispenser.waitEvent(30);
              switch(seleccion){
               
                  case 'A':
                    this.atm.setIdiom("ES");
                  break;
                  case 'B':
                    this.atm.setIdiom("EN");
                  break;
                  case 'C':
                    this.atm.setIdiom("CA");
                  break;
                  case 'D':
                    this.atm.setIdiom("EU");
                  break;
                  case 'F':
                    this.atm.mostrarMenu();
                  break;
                  case '0':
                    this.atm.mostrarMenu();
                  break;
              }
            } while(seleccion=='E' || seleccion=='1');
        this.atm.mostrarMenu();
              
    }
    
    public  IdiomSelection(CinemaTicketDispenser dispenser, Multiplex mul){
        super(dispenser, mul);
    
    }
    
    
    public String getTitle(){
    
    
    return ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("seleccionIdioma");
    }
    
    
}
