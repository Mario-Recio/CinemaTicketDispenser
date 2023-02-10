
package Clases;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;


public class MultiplexState implements Serializable {

    
    private List<Theater> theaterList;
    private CinemaTicketDispenser dispenser;

    
    
    
    public MultiplexState(CinemaTicketDispenser dispenser){
    
    theaterList = new ArrayList<>();
    this.loadMoviesAndSessions();
    this.dispenser= dispenser;

    }
    
    
    
    public void loadMoviesAndSessions(){ //crea una lista con los archivos de la carpeta de ficheros y carga los datos.
        File carpeta = new File("Ficheros/"); 
            File[] lista = carpeta.listFiles();
        this.cargarDatosSalas(lista);
        this.cargarDatosPeliculas(lista);
 
    }
    
    


    public Theater getTheater(){
        return null; 
    }
    
    public int getNumberOfTheaters(){
        return this.theaterList.size();
    }
 
    
    
    public List<Theater> getTheaterList() {
        return theaterList;
    }
    
    //cargar y crear la lista de salas listaSalas
    
    private void cargarDatosSalas(File[] lista) { // Si el archivo empieza por Theater, habra que cargar la disposicion de los asientos. 
                                     //Esto nos sirve por si en un futuro hay otro archivo con otro nombre que tambien haya que cargar. 
                                     //En dicho caso, basta con añadir un else y poner como condicion que empieze por el nombre del nuevo fichero
    	for(int i=0; i<lista.length; i++) {
    		String nombre = lista[i].getName();
    		if(nombre.startsWith("Theater")) { 
                    Theater sala = cargarSala("Ficheros/" + nombre);
                    this.theaterList.add(sala);

    		}
    	} 
    }
    
    private void cargarDatosPeliculas(File[] lista){ //misma explicacion que el metodo anterior, pero para cargar el fichero de peliculas
    	for(int i=0; i<lista.length; i++) {
    		String nombre = lista[i].getName();
    		if(nombre.startsWith("Movie")) {
                    cargarPelicula("Ficheros/" + nombre);	
    		}
    	}
    }
    
    private Theater cargarSala(String fichero) { //carga la disposicion del fichero y el numero de la sala usando el nombre del fichero. Lee una linea entera, y despues recorre esa linea caracter a caracter. Si detecta un "*" crea un asiento y lo guarda en el Set de asientos
        Theater sala = null;
        try{
            File fich = new File(fichero);
            Scanner sc = new Scanner(fich);
            String valorNum = fichero.split("Theater")[1];
            String valorNum2 = valorNum.substring(0, valorNum.indexOf("."));
            sala = new Theater(Integer.valueOf(valorNum2));
            int fila =0;
            while(sc.hasNext()){
                String linea = sc.nextLine();
                for(int col =0; col< linea.length(); col++){
                    if(linea.charAt(col)=='*'){
                        Seat asiento = new Seat(fila, col);
                        sala.addSeat(asiento);
                    }
                }
                fila++;
            }

                
        }catch(IOException e){
            System.out.println("Imposible abrir el fichero");
        }
        
        return sala;
    }
    
    private void cargarPelicula(String fichero){  //carga la informacion del fichero de peliculas haciendo un split, y viendo la priemra palabra de cada linea no vacia. Dependiendo del valor de ese string, se realizaran unas acciones u otras  
            Theater theater = null;
            File archivo = new File(fichero);
            Scanner lectura = null;

            try {
               // Apertura del fichero y creacion de BufferedReader para poder
               // hacer una lectura.
               lectura = new Scanner(archivo);

               Film film = new Film();

               while(lectura.hasNext()) {
                   String linea = lectura.nextLine();
                   if(!linea.equals("")){
                       String[] partes = linea.split(":");
            {
                           switch(partes[0]){
                               case "Title":
                                   //Title: Ghostbusters: Afterlife
                                   film.setName(linea.substring(6));
                                   break;
                               case "Theatre":
                                   int num = Integer.valueOf(partes[1].trim());
                                   for(Theater sala : theaterList){
                                       if(sala.getNumber()== num){
                                           theater = sala;
                                           theater.setFilm(film);
                                       }
                                   }
                                   break;
                               case "Sessions":
                                   linea = linea.substring(9).trim();
                                   String[] horarios = linea.split(" ");
                                   for(String horario: horarios){
                                      String[] valores = horario.split(":");
                                      Session nueva = new Session(valores[0], valores[1]);
                                      theater.addSession(nueva);
                                   }
                                   break;
                               case "Poster":
                                   film.setPoster(new File(partes[1].trim()));
                                   break;
                               case "Price":
                                   String[] trozos = partes[1].trim().split(" ");
                                   int precio = Integer.valueOf(trozos[0].trim());
                                   theater.setPrice(precio);
                                   break;
                                   
                               default:
                                   film.setDescription(linea);
                           }
                       }
                   }
                  
            }
            }catch(IOException e){
                System.out.println("Imposible abrir el fichero "+fichero);
            } catch(IndexOutOfBoundsException e){
                System.out.println("El fichero prorcionado no tiene el formato correcto "+fichero);
            }catch(NumberFormatException e){
               
                System.out.println("No se puede convertir un valor a número ");           
            }finally{
               // En el finally cerramos el fichero, para asegurarnos
               // que se cierra tanto si todo va bien como si salta 
               // una excepcion.
                 
                

            }
            
    }
    
        /*public void imprimir(){
        for(Theater t: theaterList){
            System.out.println(t);
            System.out.println("");
        }
    }*/
        
        public void setDispenser(CinemaTicketDispenser c){
            this.dispenser= c;
        }
    
}
