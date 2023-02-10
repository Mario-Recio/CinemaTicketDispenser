
package Clases;

import java.io.Serializable;


public class Seat implements Serializable, Comparable<Seat> {

    private int row;
    private int col;    
    
    public Seat(int row,int col){
            this.row = row;
            this.col = col;
    }

    @Override
    public boolean equals(Object obj) { //hay que implementar el metodo equals para los asientos, ya que habra que comprar si dos asientos son iguales si el usuario selecciona un asiento dos veces
                                        // en cuyo caso no tendria sentido permitirlo e imprimir dos entradas iguales
        if(this == obj) return true;
        if(!(obj instanceof Seat)) return false;
        if(obj.getClass()!= Seat.class) return false;
        Seat otroAsiento = (Seat)obj;
        return this.row == otroAsiento.row && this.col == otroAsiento.col;
    }

    
    
    @Override
    public int compareTo(Seat o) {  //como consecuencia del metodo anterior, para poder utilizar el metodo equals, ese clase debe ser comparable, asi que implementamos lel metodo compareTo
                                    // donde primero mirara que fila es mayor, y si son iguales, mirara las columnas. Si todo coincide, sera el mismoa asiento.
      if(this.row < o.row) 
          return -1;
      if(this.row > o.row) 
          return +1;
      //si no son de la misma fila
      if(this.col < o.col) 
          return -1;
      if(this.col > o.col) 
          return +1;
      else
        return 0;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    
    @Override
    public String toString(){
        return "["+row+","+col+"]";
    }
    
    
}
