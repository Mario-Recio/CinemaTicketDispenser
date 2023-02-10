
package Clases;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import sienens.CinemaTicketDispenser;


public class MainMenu extends Operation {
    
  
   

    List <Operation> operationList;

    
    
    public  MainMenu(CinemaTicketDispenser dispenser, Multiplex mul){ //creacion de operaciones para el gestor
        super(dispenser, mul);
        operationList= new ArrayList <>();
        operationList.add(new IdiomSelection(dispenser, mul));
        operationList.add(new MovieTicketSale(dispenser, mul));
        //aqui se crearian nuevas operaciones    operationList.add(new VentaPalomitas(dispenser, mul));
    }
    
    public void doOperation(){ //una vez se elije una operacion, se llama al metodo doOperation implementado en dicha clase
        presentMenu();
        char seleccion;
        seleccion = dispenser.waitEvent(30);
        switch(seleccion){
            case '1':
                doOperation();
            break;
            case '0':
                doOperation();
                break;
            default:
                int pos = seleccion- 'A'; //convierte A->0, B->1
                if(pos < operationList.size())
                    operationList.get(pos).doOperation();
                else doOperation();
                
        }
    
    }
    

    public void presentMenu(){//muestra las operaciones del menu principal
        dispenser.setMenuMode();
        dispenser.setDescription("");
        dispenser.setImage("");
        dispenser.setTitle(getTitle());
        for(int i =0; i<6; i++){ 
            dispenser.setOption(i, "");
        }
        
        int i =0;
        for(Operation o: operationList){
            dispenser.setOption(i++, o.getTitle());
        }
    }
    
    
    public void presentIdioms(){ //muestra los idiomas disponibles
        dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("seleccionIdioma"));
        dispenser.setOption(0, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("castellano"));
        dispenser.setOption(1, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("ingles"));
        dispenser.setOption(2, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("catalan"));
        dispenser.setOption(3, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("euskera"));
        dispenser.setOption(5, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("cancelar"));

    }
    @Override
    public String getTitle() { //mensaje que se muestra en la parte superior de la interfaz
        return ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("selOpt");
    }
    
        
}
        