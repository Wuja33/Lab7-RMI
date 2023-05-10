package map;

import tools.PanelRegion;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel
{
    JPanel mainPanel;
    JPanel counterUpper;
    JPanel counterLeft;
    PanelRegion[][] arrayOfPanelsMain;
    public Map(boolean borders)
    {
        this.setPreferredSize(new Dimension(780,780));
        this.setLayout(new BorderLayout());
        this.arrayOfPanelsMain = new PanelRegion[8][8];
        this.mainPanel = new JPanel();
        this.add(mainPanel,BorderLayout.CENTER);
        mainPanel.setLayout(new GridLayout(8,8,6,6));
        mainPanel.setBackground(Color.black);

        //tworzenie dodatkowego panelu, aby wyrównać lewy-górny róg
        JPanel panelHelp = new JPanel();
        JPanel panelEmpty = new JPanel();
        panelHelp.setLayout(new BorderLayout());
        panelEmpty.setPreferredSize(new Dimension(25,30));
        panelHelp.add(panelEmpty,BorderLayout.WEST);

        this.counterUpper = new JPanel();
        this.counterLeft = new JPanel();
        this.add(panelHelp,BorderLayout.NORTH);
        this.add(counterLeft,BorderLayout.WEST);

        panelHelp.add(counterUpper,BorderLayout.CENTER);

        counterUpper.setLayout(new GridLayout(1,8));
        counterLeft.setLayout(new GridLayout(8,1));
        //tworzenie paneli do tablicy paneli
        //ZROBIENIE 64 JPaneli do zrobienia tablicy  (???)

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (borders)
                    arrayOfPanelsMain[i][j] = new PanelRegion(true);
                else
                    arrayOfPanelsMain[i][j] = new PanelRegion(false);
                mainPanel.add(arrayOfPanelsMain[i][j]);
            }
        }

        //tworzenie numeracji poziomej i pionowej
        int countHelp = 0;
        char charA = 'A';
        char charA2 = 'A';

        for (int i = 8; i >= 1; i--) {
            //tworzenie głównych indeksów
            JPanel panel1 = new JPanel();
            JPanel panel2 = new JPanel();
            JLabel label1 = new JLabel(Character.toString(charA+countHelp),SwingConstants.CENTER);
            JLabel label2 = new JLabel(String.valueOf(i),SwingConstants.CENTER);
            label1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
            label2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));

            panel1.setLayout(new BorderLayout());
            panel1.add(label1,BorderLayout.CENTER);
            panel2.setLayout(new BorderLayout());
            panel2.add(label2,BorderLayout.CENTER);

            countHelp++;

            JPanel panel3 = new JPanel();
            panel3.setLayout(new GridLayout(1,10,0,0));
            int countHelp2 = 0;
            for (int j = 1; j <= 10; j++) {
                JLabel label3 = new JLabel(Character.toString(charA2+countHelp2),SwingConstants.CENTER);
                label3.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
                panel3.add(label3);
                countHelp2++;
            }
            panel1.add(panel3,BorderLayout.SOUTH);

            JPanel panel4 = new JPanel();
            panel4.setLayout(new GridLayout(10,1,0,0));
            for (int j = 10; j >= 1; j--) {
                JLabel label3 = new JLabel(String.valueOf(j),SwingConstants.CENTER);
                label3.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
                panel4.add(label3);
            }

            panel2.add(panel4,BorderLayout.EAST);
            counterUpper.add(panel1);
            counterLeft.add(panel2);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public synchronized PanelRegion[][] getArrayOfPanelsMain() {
        return arrayOfPanelsMain;
    }
}