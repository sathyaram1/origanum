package com.mycompany.versione1;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;



//import java.util.Arrays;

public class GameStatus {
        public Paragrafo[] paragrafi;
        Set<String> tag = new HashSet<>();
        Stack<Integer> cronologia = new Stack<>();
        public String selezionato= "sei";
        
        
        
        
    public GameStatus (String path) {
        StringBuilder contenuto = new StringBuilder();

        // Leggi il file di testo
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenuto.append(linea).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Dividi il contenuto in paragrafi usando il delimitatore "#"
        String[] paragrafiArray = contenuto.toString().split("#");
        
        // Inizializza l'array di paragrafi con la stessa dimensione dell'array ottenuto dallo split
            paragrafi = new Paragrafo[paragrafiArray.length];

            // Rimuovi gli spazi bianchi superflui dai paragrafi
            for (int i = 0; i < paragrafiArray.length; i++) {
                paragrafi[i]= new Paragrafo(paragrafiArray[i].trim());
                //system.out.println("XXX");//log
            }
        cronologia.push(0);
        cronologia.push(0);
    }  
    
    
    
    
    //
     public void Mostra(int pagina, JFrame frame) {
        // pulisce il frame
        frame.revalidate();
        frame.repaint();
        // Crea una mappa per le azioni
        Map<String, Runnable> azioni = new HashMap<>();

        // Pannello principale
        JPanel panel = new JPanel(new BorderLayout());

        // Costruisci il contenuto HTML
        StringBuilder htmlContent = new StringBuilder("<html><body style='text-align: left; font-family: serif; color: black;'>");
        int i = 0;//prova
        Paragrafo par = paragrafi[pagina];
        for (Frase f :par.frasi) {
            if (f.controlla(this)){
                String linkId = Integer.toString(i);
                //System.out.println(f.testo);
                String linkText = String.format("<a href='%s' style='text-decoration: none; color: black;'>%s</a>", linkId, f.testo +".  ");
                htmlContent.append(linkText);
            }
            i++;
        }
        htmlContent.append("</body></html>");
        
        
        // Crea il JEditorPane per visualizzare il testo HTML
        JEditorPane editorPane = new JEditorPane("text/html", htmlContent.toString());
        editorPane.setEditable(false);
        editorPane.setOpaque(false);

        // Aggiungi un HyperlinkListener per gestire i click sui link
        editorPane.addHyperlinkListener((HyperlinkEvent e) -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { //se vengo cliccato allora fai questo:
                
                int fraseCliccata = Integer.parseInt(e.getDescription());
                
                //è stata cliccata la fraseCliccata frase
                int a=0;
                int destinazione = 0;
                for (String o : par.frasi[fraseCliccata].oggetti){
                    if (o.equals(selezionato)||o.equals("")){
                        //rimuovi i vecchi contenuti
                        frame.getContentPane().remove(panel);
                        //aggiorna i tag
                        tag.addAll(par.tagAggiunti);
                        tag.removeAll(par.tagRimossi);
                        destinazione = par.frasi[fraseCliccata].bersagli[a];
                        if(par.pagina < 1000) // aggiungi il paragrafo allo stack. i paragrafi maggiorni di 1000 sono paragrafi di analisi e non vengono aggiuni
                            cronologia.push(par.pagina);
                        Mostra(destinazione, frame); 
                        break;  
                    }
                    a++;
                }
            }
        });

        // Aggiungi il JEditorPane al pannello
        panel.add(new JScrollPane(editorPane), BorderLayout.CENTER);

        // Aggiungi il pannello al frame
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

}
