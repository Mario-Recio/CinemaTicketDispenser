package Clases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;


class Multiplex {
    
    private String idiom;        
    private CinemaTicketDispenser dispenser ;
 
    MainMenu menu;    
    //UrjcBankServer bank = new UrjcBankServer();
    
    int mode = 0;
    
    void start() {         
        dispenser = new CinemaTicketDispenser();
        menu = new MainMenu(dispenser, this);
        idiom = "ES";
        
        mostrarMenu();
        
 
        

                    
           
    }
    public void mostrarMenu(){
        menu.doOperation();
    }
    
    public void mostrarIdiomas(){
        menu.presentIdioms();
    
    }
    
    public String getIdiom(){
        
        return idiom;
    }
    
    public void setIdiom(String value){
        this.idiom = value;
        
    }
    
    
   

}


