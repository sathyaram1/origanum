package com.mycompany.versione1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Test {
    private static JButton selectedButton = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        // Crea il frame
        JFrame frame = new JFrame("Frame con Immagine di Sfondo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Crea il pannello personalizzato per disegnare l'immagine di sfondo
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null); // Usa null layout per posizionamento assoluto

        // Crea il pulsante per passare alla modalità schermo intero
        JButton fullscreenButton = new JButton("[]");
        fullscreenButton.setBounds(10, 10, 30, 30); // Posiziona in alto a sinistra, piccolo e quadrato
        fullscreenButton.addActionListener(e -> toggleFullscreen(frame));
        backgroundPanel.add(fullscreenButton);

        // Percorsi delle icone
        String iconPath = "C:\\Users\\sathy\\Desktop\\origanum prove\\origanumMavenProva\\src\\main\\resources\\icone\\icon1.png";
        String craftIconPath = "C:\\Users\\sathy\\Desktop\\assets origanum\\craft.png";
        String maniIconPath = "C:\\Users\\sathy\\Desktop\\assets origanum\\mani.png";
        String spadaIconPath = "C:\\Users\\sathy\\Desktop\\assets origanum\\spada.png";
        String scudoIconPath = "C:\\Users\\sathy\\Desktop\\assets origanum\\scudo.png";

        BufferedImage originalIcon = loadImage(iconPath);
        BufferedImage craftIcon = loadImage(craftIconPath);
        BufferedImage maniIcon = loadImage(maniIconPath);
        BufferedImage spadaIcon = loadImage(spadaIconPath);
        BufferedImage scudoIcon = loadImage(scudoIconPath);

        // Crea e aggiungi 7 bottoni con icona sulla destra
        JButton[] buttons = new JButton[7];
        for (int i = 0; i < 7; i++) {
            buttons[i] = new JButton();
            buttons[i].setMargin(new Insets(0, 0, 0, 0)); // Rimuovi i margini interni per l'icona
            buttons[i].setContentAreaFilled(false); // Rendi il bottone trasparente
            buttons[i].setBorderPainted(false); // Rendi il bordo trasparente
            backgroundPanel.add(buttons[i]);
        }

        // Assegna le icone specifiche ai bottoni
        buttons[1].setIcon(new ImageIcon(spadaIcon)); // Imposta l'icona della spada
        buttons[2].setIcon(new ImageIcon(scudoIcon)); // Imposta l'icona dello scudo

        // Aggiungi il pannello al frame
        frame.add(backgroundPanel);

        // Listener per ridimensionare i bottoni quando la finestra viene ridimensionata
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeButtons(frame, buttons, originalIcon, craftIcon, spadaIcon, scudoIcon);
            }
        });

        // Aggiungi ActionListener ai bottoni
        for (int i = 1; i < 7; i++) {  // Inizia da 1 per saltare il primo bottone
            int index = i;
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedButton != null && selectedButton != buttons[0]) {
                        // Ripristina l'icona del bottone precedentemente selezionato
                        int previousSize = selectedButton.getWidth();
                        BufferedImage icon = getButtonIcon(selectedButton, buttons[0], originalIcon, craftIcon, spadaIcon, scudoIcon);
                        selectedButton.setIcon(new ImageIcon(getScaledImage(icon, previousSize, previousSize)));
                    }
                    // Cambia l'icona del bottone selezionato
                    int newSize = (int) (buttons[index].getWidth() * 1.40); // Ingrandisci del 40%
                    buttons[index].setIcon(new ImageIcon(getScaledImage(maniIcon, newSize, newSize)));
                    selectedButton = buttons[index];
                }
            });
        }

        // Visualizza il frame
        frame.setVisible(true);
    }

    private static BufferedImage getButtonIcon(JButton button, JButton firstButton, BufferedImage originalIcon, BufferedImage craftIcon, BufferedImage spadaIcon, BufferedImage scudoIcon) {
        if (button == firstButton) {
            return craftIcon;
        } else if (button.getIcon() != null) {
            Icon icon = button.getIcon();
            if (icon instanceof ImageIcon) {
                Image img = ((ImageIcon) icon).getImage();
                if (img.equals(spadaIcon)) {
                    return spadaIcon;
                } else if (img.equals(scudoIcon)) {
                    return scudoIcon;
                }
            }
        }
        return originalIcon;
    }

    private static void resizeButtons(JFrame frame, JButton[] buttons, BufferedImage originalIcon, BufferedImage craftIcon, BufferedImage spadaIcon, BufferedImage scudoIcon) {
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int marginBottom = (int) (frameHeight * 0.10); // 10% dell'altezza del frame come margine inferiore
        int marginRight = (int) (frameWidth * 0.05); // 5% della larghezza della finestra

        // Altezza disponibile per i bottoni
        int availableHeight = frameHeight * 80 / 100;
        int buttonSize = (int) (availableHeight / (7 + 0.2 * 6)); // Calcola la dimensione dei bottoni
        int padding = (int) (buttonSize * 0.2); // Padding tra i bottoni
        int totalHeight = buttonSize * 7 + padding * 6;

        // Se la finestra è troppo piccola, ridimensiona i bottoni e il padding
        if (totalHeight > frameHeight) {
            buttonSize = (int) ((frameHeight * 0.80 - marginBottom) / (7 + 0.2 * 6));
            padding = (int) (buttonSize * 0.2);
        }

        int startX = frameWidth - buttonSize - marginRight;
        int startY = (frameHeight - marginBottom - totalHeight) / 2; // Centra verticalmente con il margine inferiore

        for (int i = 0; i < 7; i++) {
            int currentButtonSize = buttonSize;
            if (i == 0) {
                currentButtonSize = (int) (buttonSize * 1.25); // Il primo bottone è più grande del 25%
                buttons[i].setBounds((int) (startX - currentButtonSize * 0.25), startY + i * (buttonSize + padding), currentButtonSize, currentButtonSize); // Sposta verso sinistra
            } else {
                buttons[i].setBounds(startX, startY + i * (buttonSize + padding), currentButtonSize, currentButtonSize);
            }
            BufferedImage icon = getButtonIcon(buttons[i], buttons[0], originalIcon, craftIcon, spadaIcon, scudoIcon);
            if (buttons[i] == selectedButton) {
                int newSize = (int) (currentButtonSize * 1.40); // Ingrandisci del 40%
                buttons[i].setIcon(new ImageIcon(getScaledImage(icon, newSize, newSize)));
            } else {
                buttons[i].setIcon(new ImageIcon(getScaledImage(icon, currentButtonSize, currentButtonSize)));
            }
        }
    }

    private static BufferedImage getScaledImage(BufferedImage srcImg, int w, int h) {
        if (w <= 0 || h <= 0) return srcImg; // Evita errori di dimensione
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        // Usa RenderingHints per migliorare la qualità dell'immagine
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void toggleFullscreen(JFrame frame) {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (frame.isUndecorated()) {
            // Exit fullscreen
            frame.dispose();
            frame.setUndecorated(false);
            device.setFullScreenWindow(null);
            frame.setVisible(true);
        } else {
            // Enter fullscreen
            frame.dispose();
            frame.setUndecorated(true);
            device.setFullScreenWindow(frame);
        }
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        // Percorso dell'immagine di sfondo
        String imagePath = "C:\\Users\\sathy\\Desktop\\assets origanum\\Senza titolo 87_Ripristinato_20240515165637.png";
        // Carica l'immagine di sfondo
        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Usa RenderingHints per migliorare la qualità dell'immagine
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Disegna l'immagine di sfondo
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
