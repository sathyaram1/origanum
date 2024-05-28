/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.versione1;

//import static com.mycompany.versione1.Paragrafo.*;

import javax.swing.JFrame;


/**
 *
 * @author sathy
 */
public class menu {
    public static void main(String[] args){
    GameStatus game = new GameStatus("C:\\Users\\sathy\\Desktop\\origanum prove\\origanumMavenProva\\src\\main\\resources\\testo.txt");
            JFrame frame = new JFrame("Frame con Testo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
    
        game.Mostra(1, frame);
}
}