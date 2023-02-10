
package Clases;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import sienens.CinemaTicketDispenser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;




public class MovieTicketSale extends Operation   {
    
   private MultiplexState state;


    




    public void doOperation (){
        cargarState();
        this.selectTheater();
        
    }
    
    
    
    
    
    
    public  MovieTicketSale(CinemaTicketDispenser dispenser, Multiplex atm){
        super(dispenser, atm);
        cargarState();
    }
    
    
    
  
    
    
    private void selectSeats(Theater sala, Session sesion){ //muestra el estado de los asientos para esa sala y esa sesion y permite seleccionar hasta 4 asientos libres, que se añadiran a una lista de asientos selecconados, para poder calcular la cantidad a pagar, e imprimir las entradas
        dispenser.setTheaterMode(sala.getMaxRows()+1, sala.getMaxCol()+1);
        vaciarOpciones();
        dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("selAsiento"));
        dispenser.setOption(0, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("continuar"));
        dispenser.setOption(1, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("cancelar"));
        
        //primero hacemos que no aparezca ningun asiento
        for(int fila =1; fila <= sala.getMaxRows()+1; fila++)
            for(int col =1; col <=sala.getMaxCol()+1; col++)
                dispenser.markSeat(fila, col, 0);
        
        //ahora marcamos solo los que sabemos que existen
        for(Seat s: sala.getSeatSet())
            if(sesion.isOccupied(s.getRow()+1, s.getCol()+1))
                dispenser.markSeat(s.getRow()+1, s.getCol()+1, 1);
            else{

                dispenser.markSeat(s.getRow()+1, s.getCol()+1, 2);
            }
        
        char seleccion;
        List<Seat> seleccionados = new ArrayList<>();
        do{    
            seleccion = dispenser.waitEvent(30);
            while(seleccion!= 'A' && seleccion!= 'B' && seleccion!= '0' && seleccion!='1' && seleccionados.size()<4){
                byte col = (byte)(seleccion & 0xFF);
                byte row = (byte)((seleccion & 0xFF00) >> 8);
                dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("fila")+": " + row +" "+ ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("col")+": " + col);
                if(!sesion.isOccupied(row, col) && !seleccionados.contains(new Seat(row,col))){
                    seleccionados.add(new Seat(row, col));
                    dispenser.markSeat(row, col, 1);
                }
                seleccion = dispenser.waitEvent(30);
            }

            if(seleccion=='0')
                 this.atm.mostrarMenu();

            if(seleccionados.size()==4){
                dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("maxSeat"));
                 for(Seat s: sala.getSeatSet())
                    dispenser.markSeat(s.getRow()+1, s.getCol()+1, 0);
                 do
                    seleccion = dispenser.waitEvent(30);
                 while (seleccion=='1');
            }

            if(seleccion=='A'){
                if(seleccionados.size()>0)
                    confirmarPago(sala, sesion, seleccionados);
                else
                    seleccion='1'; //para que repita el bucle si no ha seleccionado asientos
            }else if (seleccion=='B' || seleccion=='0'){
                atm.mostrarMenu();
            }
        
    }while(seleccion=='1');   
       
        
    }
    
    
        
    
    
    private void selectSession(Theater sala){ //muestra las sesiones disponibles para esa sala, y permite seleccionar una
           int i=0;

        dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("selSession"));
        vaciarOpciones();
        for(Session s: sala.getSessions()){
           dispenser.setOption(i++, s.toString());
        }
        dispenser.setDescription(sala.getFilm().getDescription());
        dispenser.setImage("Ficheros/"+sala.getFilm().getPoster().toString());
       
        dispenser.setOption(5, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("cancelar"));
        char seleccion;
        do{
            seleccion = dispenser.waitEvent(30);
            try{
            if( seleccion != '0' && seleccion != '1')
                selectSeats(sala, sala.getSessions().get(seleccion-'A'));
            
            }catch(IndexOutOfBoundsException e){
            
            if (seleccion=='F' || seleccion=='0')
                this.atm.mostrarMenu();
            }
        }while(seleccion=='1' || (seleccion-'A'>=sala.getSessions().size()));
  }      
    
    
    
    
    private boolean confirmarPago(Theater sala, Session sesion, List<Seat> seleccionados){ //calcula el precio a pagar, y pide al usuario que confirme la compra
        int total = computePrice(sala, seleccionados);
        dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("confirmacionPago")+ sala.getFilm().getName()+", "+sesion+", "+total+" €");
        vaciarOpciones();
        dispenser.setOption(0, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("continuar"));
        dispenser.setOption(1, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("cancelar"));

            for(int fila =1; fila <= sala.getMaxRows()+1; fila++)
                for(int col =1; col <=sala.getMaxCol()+1; col++)
                    dispenser.markSeat(fila, col, 0);
        for(Seat s: seleccionados)
            dispenser.markSeat(s.getRow(),s.getCol(), 1);
        char seleccion;
        do{
             seleccion = dispenser.waitEvent(30);
             if(seleccion == 'A')
                pagar(sala,sesion, seleccionados, total);
             
             else if (seleccion=='0' || seleccion=='B')        
                atm.mostrarMenu();
        }while(seleccion=='1');
    return true;
    }
    
   
    
    
    protected void pagar(Theater sala, Session sesion, List<Seat> seleccionados, int total){ //llama al metodo encargado de realizar el pago, imprime las entradas y serializa los cambios
        
            String texto = ""+seleccionados.size()+" "+ResourceBundle.getBundle("res/"+ this.atm.getIdiom()).getString("infoTicket")+sala.getFilm().getName()+" "+sesion+": "+total+" €";
            PerformPayment pago = new PerformPayment(dispenser, atm, total, texto);
            try{
                 pago.doOperation();
            }catch(RuntimeException e){
                if(e.getMessage().equals("OK")){ //si la excepcion devuelta indica que la operacion se ha realizado con exito, se imprimen las entradas y se serializa
                    for(Seat s: seleccionados){
                        sesion.occupiesSeat(s.getRow(), s.getCol());
                        
                        List<String> entrada = new ArrayList();
                        entrada.add(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("infoTicket")+sala.getFilm().getName());
                        entrada.add("===================");
                        entrada.add(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("sala")+" "+sala.getNumber());
                        entrada.add(sesion.toString());
                        entrada.add(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("fila")+" "+s.getRow());
                        entrada.add(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("col")+" "+s.getCol());
                        entrada.add(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("precio")+" "+sala.getPrice()+" €");
                       dispenser.print(entrada);
                    }
                    this.serializeMultiplexState();
                    pago.devolverTarjeta();
                }
                atm.mostrarMenu();
                
            }    
    }
   
    private int computePrice(Theater sala, List<Seat> seleccionados){ //calcula el precio a pagar
        return sala.getPrice()*seleccionados.size();
    
 
    }
    
    
    private void selectTheater() { //muestra y permite seleccionar peliculas

    int i=0;
        dispenser.setTitle(ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("selPel"));
        vaciarOpciones();
       for(Theater t: state.getTheaterList()){
           dispenser.setOption(i++, t.getFilm().getName());
       }
       dispenser.setOption(5, ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("cancelar"));
        char seleccion;
        do{
            seleccion = dispenser.waitEvent(30);
            try{
            if( seleccion != '0' && seleccion != '1')
                selectSession(state.getTheaterList().get(seleccion-'A'));
            
            }catch(IndexOutOfBoundsException e){
                if (seleccion=='F' || seleccion=='0')
                    this.atm.mostrarMenu();
            }
        }while(seleccion=='1' || (seleccion-'A'>=state.getTheaterList().size()));
        
        
    }
    
    
    
    
    public void serializeMultiplexState(){ //serializa el multiplexState en un archivo en la carpeta. El archivo contiene en su nombre la fecha actual, asi que si no existe uno para un determinado dia, detectara que no hay un state registrado, y lo creara. Sobre ese archivo se crea el fileOutPutStream, y se escribira el state actual en el.
         String fecha = LocalDate.now().toString();
       try {
           
           File nuevo = new File("Ficheros/multiplex"+fecha);
           if(!nuevo.exists()) 
               nuevo.createNewFile();
           FileOutputStream os = new FileOutputStream(nuevo);
           ObjectOutputStream salida = new ObjectOutputStream(os);
           state.setDispenser(null);
           salida.writeObject(this.state);
           salida.close();
           
       } catch (FileNotFoundException ex) {
           System.out.println("Fichero de copia de seguridad no encontrado");
       } catch (IOException ex) {
           System.out.println("Error al escribir el fichero de copia de seguridad");
       }
        
    
    
    }
    
    public void vaciarOpciones(){ //formatea las opciones del menu 
        for(int i =0; i<6; i++){
            dispenser.setOption(i, "");
        }
    }
    
    public void deserializeMultiplexState(File fichero){ //se le pasa el archivo donde serializamos la informacion, y ObjectInputStream guarda en el state lo que se lea de dicho archivo, haciendo un casting a MultiplexState  
       try {
           FileInputStream is = new FileInputStream(fichero);
           ObjectInputStream entrada = new ObjectInputStream(is);
           this.state = (MultiplexState)entrada.readObject();
           state.setDispenser(this.dispenser);
           entrada.close();
       } catch (FileNotFoundException ex) {
           System.out.println("No existe el fichero");
       } catch (IOException ex) {
           System.out.println("No se pudo abrir el fichero de copia de seguridad");
           ex.printStackTrace();
       } catch (ClassNotFoundException ex) {

       }
        
    
    }

    private void cargarState() { //crea la ruta donde esta guardado el archivo con la informacion, y llama a deserializeMultiplexState para que cargue la informacion que encuentre. Si no existia ningun archivo con esa ruta, crea un state nuevo
        String fecha = LocalDate.now().toString();              // dispenser.setOption(i, LocalDate.now().plusDays((i+1)).toString());
        File copiaSeguridad = new File("Ficheros/multiplex"+fecha);
        if(copiaSeguridad.exists()){
            deserializeMultiplexState(copiaSeguridad);
        }else{
            this.state = new MultiplexState(dispenser);
        }
    }

    @Override
    public String getTitle() {
        return ResourceBundle.getBundle("res/"+this.atm.getIdiom()).getString("ventaTickets");
    }

   

    

    
        
    
    
    
    
    
    
    


    
}
