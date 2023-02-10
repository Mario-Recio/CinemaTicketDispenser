
package Clases;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.CommunicationException;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;





public class PerformPayment extends Operation {
    private UrjcBankServer bank;
    private int precio;
    private String texto;


    
 public  PerformPayment( CinemaTicketDispenser cine, Multiplex mul, int precio, String texto){
    super(cine, mul);
    bank= new UrjcBankServer();
    this.texto = texto;
    this.precio =precio;
}   
    
    
 public void doOperation(){ //realiza el pago. Primero intenta comunicarse con el banco, y arroja una excepcion si no es posible. En caso contrario se pide la tarjeta, y si hay dinero suficiente, se confirma la operacion.
    dispenser.setMessageMode();
    dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("comunicando"));
    dispenser.setDescription("Espere unos instantes...");
    dispenser.setOption(0,"");
    dispenser.setOption(1,"");
           
           int veces =10;
           while(veces >0 && !bank.comunicationAvaiable()){ //intenta comunicarse con el banco, y avisa al usuario si no es posible
                veces--;
                try {
                    Thread.sleep(500); 
                } catch (InterruptedException ex) {
                    Logger.getLogger(PerformPayment.class.getName()).log(Level.SEVERE, null, ex);
                }
            }//cuando termine hay que ver si se ha podido comunicar

            if(veces==0){
                dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("error"));
                dispenser.setDescription(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("noBanco"));
                dispenser.setOption(0, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString(""));
                dispenser.setOption(1, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString(""));
                devolverTarjeta();
                atm.mostrarMenu();
            }else{
                dispenser.setMessageMode();
                dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("inserteTarjeta"));
                dispenser.setDescription("");
                dispenser.setOption(0,"");
                dispenser.setOption(1, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("cancelar"));
                char seleccion; 
                do{
                    seleccion = dispenser.waitEvent(30);
                    if(seleccion =='1'){
                        dispenser.retainCreditCard(false);
                        try {
                            if(bank.doOperation(dispenser.getCardNumber(), precio)){
                                dispenser.setDescription(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("imprimiendo"));
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException ex) {
                                Logger.getLogger(PerformPayment.class.getName()).log(Level.SEVERE, null, ex);
                                }
                     dispenser.setDescription("");
                     throw new RuntimeException("OK");

                            }else{
                                dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("imposible"));
                                dispenser.setDescription(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("noDinero"));
                                dispenser.setOption(0, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("cancelar"));
                                dispenser.setOption(1, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString(""));
                                devolverTarjeta();
                                atm.mostrarMenu();
                            }
                            } catch (CommunicationException ex) {
                                dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("error"));
                                dispenser.setDescription(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("noBanco"));
                                dispenser.setOption(0, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("cancelar"));
                                dispenser.setOption(1, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString(""));
                                devolverTarjeta();
                                atm.mostrarMenu();
                            }
                        }
                        else if (seleccion=='B'){
                            dispenser.setDescription("");
                            atm.mostrarMenu();
                    }
                }while(seleccion!='1' && seleccion!='B');
            }
    }


    @Override
    public String getTitle() {
        return "Realizar pago";
    }

    //cambie esto a publico
    public void devolverTarjeta() { //expulsa la tarjeta y la retiene si no se recoje
        dispenser.setOption(1,"");
        dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("recoger"));
        boolean devuelta = dispenser.expelCreditCard(30);
        if( !devuelta){
            dispenser.retainCreditCard(true);
        }
    }




    
}
