package com.mycompany.versione1;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

public class Frase {
    public String testo;
    public int bersagli[];
    public String oggetti[];
    Set<String> requisiti = new HashSet<>();
    Set<String> requisitiNegativi = new HashSet<>();
    
        
    public boolean controlla (GameStatus game){
        Set<String> tag = new HashSet<>();
        tag.addAll(game.tag);
        tag.add("P" + String.valueOf(game.cronologia.peek())); //aggiunge un tag che corrisponde alla tagina precedente 
        return tag.containsAll(requisiti) && Collections.disjoint(tag, requisitiNegativi); //per ora va bene ma poi va aggiunto controllo paragrafo precedente e paragrafo già visitato
    }   
    
    
    
    
    
    
    public Frase(String contenuto, int Npar) {
    // Espressione regolare per catturare le parole alfanumeriche separate da trattini e precedute da &, e la sequenza opzionale tra parentesi graffe
    String patternRequisiti = "^(?:((?:&!?\\w+\\s*)+))?";
    String s = "\\s*";
    String patternSpostamento = "(?:\\{(\\w*>\\d+(?:,\\w*>\\d+)*)\\})?";
    String patternTesto = "(.+)";
    
    //unione espressione regolare e sua applicazione 
    String patternString = patternRequisiti + s + patternSpostamento + s + patternTesto;           
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(contenuto);
        
    if (matcher.find()) {  
        
        // Cattura i tag e li aggiunge
        
        String[] tag = new String[0];
            if (matcher.group(1) != null) {
                tag = matcher.group(1).split("\\s+"); 
            }
            for (String t : tag) {
                t = t.substring(1);
                if(t.matches("^!.*"))
                    requisitiNegativi.add(t.substring(1));
                else
                    requisiti.add(t);
            } 
        
        
        
        
        
        
        
        
        
        
        
        
        // Cattura i comandi e li aggiunge
        String[] comandi = new String[0];
        String[] coppia = new String[0];
        int i=0;
        if (matcher.group(2) != null) {
            comandi = matcher.group(2).split(",");
        }
        oggetti = new String[comandi.length];
        bersagli = new int[comandi.length];
        for (String parola : comandi) {
            coppia = parola.split(">"); //System.out.println("LOG " + coppia[0]);
            oggetti[i]= coppia[0];
            bersagli[i]= Integer.parseInt(coppia[1]);
            i++;
        } 
        
        // Cattura il testo restante
        testo = matcher.group(3);
        
        
        //controllo corretta compilazione
        char[] caratteriSpeciali = {'@', '§', '%', '&', '{', '}'};
        for (char c : caratteriSpeciali) {
            if (testo.indexOf(c) >= 0 ) {
                JOptionPane.showMessageDialog(null, "errore nel parsing del testo al paragrafo " + Npar +" ricontrolla il testo" );
            }
        }
        
        
        
        // LOG                                                                  LOG
        System.out.print("requisiti: ");
        for (String parola : requisiti) {
            System.out.println(parola);
        }
        
        System.out.print("oggetto sequenza:");
        for (String parola : oggetti) {
            System.out.println(parola);
        }

        System.out.println("Resto del testo:");
        System.out.println(testo);
        
    }
    
    
}
    
}