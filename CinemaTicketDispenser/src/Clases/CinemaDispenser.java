package Clases;

import java.io.IOException;
import java.io.FileNotFoundException;

public class CinemaDispenser {

    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Multiplex atm = new Multiplex();
        atm.start();
    	
    }
    
}
