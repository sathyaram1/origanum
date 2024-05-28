package com.mycompany.versione1;

import java.util.regex.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class Paragrafo {
   public int pagina;
   
   public String immagine;
   public int percentuale;
   public Frase[] frasi;
   public Set<String> tagAggiunti = new HashSet<>();      //serve quando si va ad un nuovo paragrafo per aggiornare lo GameStatus
   public Set<String> tagRimossi = new HashSet<>();       //serve quando si va ad un nuovo paragrafo per aggiornare lo GameStatus
   public Set<String> oggettiAggiunti = new HashSet<>();  //serve quando si va ad un nuovo paragrafo per aggiornare lo GameStatus
   public Set<String> oggettiRimossi = new HashSet<>();   //serve quando si va ad un nuovo paragrafo per aggiornare lo GameStatus
   

    public Paragrafo(String contenuto) {
            
        //String sm ="(?:@(\\w+\\s*(?:@\\w+\\s*)*))?";
       
        // Espressione regolare spezzata in parti leggibili(?:\\s\\d)*
        String s = "\\s*";
        String numeroPag = "^(\\d+)";              // Cattura uno o più numeri all'inizio       
        String commento= "(?:\\[(?:\\s*\\w*)*\\])?";        // rimuove commenti utili solo a chi scrive il testo
        String pPercentuale = "(%\\d+)?";          // Cattura un numero opzionale seguito da %, se presente
        String pTag =         "(?:((?:§[\\+\\-]\\w+\\s*)+))?"; // Cattura una parola opzionale dopo @, se presente
        String pOggetto =     "(?:((?:@[\\+\\-]\\w+\\s*)+))?"; // Cattura una parola opzionale dopo @, se presente
        String pImmagine =    "(€\\w+)?";    // Cattura una parola opzionale dopo €, se presente
        String testoPattern = "(.+)";              // Cattura il resto del testo
        String patternString = numeroPag + s + commento + s + pPercentuale + s + pTag + s +  pOggetto+ s + pImmagine + s + testoPattern;


        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(contenuto);
        
         
        
        

        if (matcher.find()) { 
            //pagina
            this.pagina = Integer.parseInt(matcher.group(1));
            //probabilità
            if (matcher.group(2) != null) {
                this.percentuale = Integer.parseInt(matcher.group(2).substring(1));
            } else {
                this.percentuale = -1; // Default value if no percentage is provided
            }

            //gestione tag da aggiungere e perdere
            String[] tag = new String[0];
            if (matcher.group(3) != null) {
                tag = matcher.group(3).split("\\s+"); 
            }
            for (String t : tag) {
                t = t.substring(1);
                if(t.matches("^\\+.*"))
                    tagAggiunti.add(t.substring(1));
                else
                    tagRimossi.add(t.substring(1));
            } 

            //gestione oggetti da aggiungere e togliere @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            String[] oggetti = new String[0];
            if (matcher.group(4) != null) {
               oggetti = matcher.group(4).split("\\s+");
            }
            for (String o : oggetti) {
                o = o.substring(1); 
                if(o.matches("^\\+.*"))
                    oggettiAggiunti.add(o.substring(1));
                else
                    oggettiRimossi.add(o.substring(1));   
            } 

            this.immagine = matcher.group(5) != null ? matcher.group(5).substring(1) : null;

            System.out.println("***************************************************************************************"); //per dividere gli output
            System.out.println("Numero: " + this.pagina);
            System.out.println("Percentuale: " + this.percentuale);
            for (String elemento : tagRimossi) {
                System.out.println(elemento);
            }
            System.out.println("Immagine: " + this.immagine);
            System.out.println("***************************************************************************************"); 


            String testo = matcher.group(6) != null ? matcher.group(6) : null;
            // Dividi il contenuto in paragrafi usando il delimitatore "."
            String[] frasiarray = testo.split("\\.");
            frasi = new Frase[frasiarray.length];
            // costruisci frasi
            for (int i = 0; i < frasiarray.length; i++) {
                frasi[i]= new Frase(frasiarray[i].trim(), pagina);
            }
        } 
    }
   
   
    
    



    
    
    
}//fine
