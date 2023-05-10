package tools;

import model.Artifact;

import javax.swing.*;
import java.awt.*;

public class PanelRegion extends JPanel
{
    JPanel mainPanel;
    JPanel counterUpper;
    JPanel counterLeft;

    PanelInside[][] panels;
    String region;
    public PanelRegion(boolean border)
    {
        this.panels = new PanelInside[10][10];
        this.setLayout(new GridLayout(10,10,2,2));
        if (border) {
            this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
            this.setBackground(Color.black);
        }
        else
            this.setBackground(new Color(0x36A107));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                panels[i][j] = new PanelInside();
                this.add(panels[i][j]);
            }
        }
    }
    public PanelRegion(String region)
    {
        this.region = region;
        this.setPreferredSize(new Dimension(500,500));
        this.panels = new PanelInside[10][10];
        this.mainPanel = new JPanel();
        this.setLayout(new BorderLayout());
        this.add(mainPanel,BorderLayout.CENTER);

        mainPanel.setLayout(new GridLayout(10,10,2,2));
        mainPanel.setBackground(Color.black);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                panels[i][j] = new PanelInside();
                mainPanel.add(panels[i][j]);
            }
        }

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

        counterUpper.setLayout(new GridLayout(1,10,0,0));
        panelHelp.add(counterUpper,BorderLayout.CENTER);
        counterLeft.setLayout(new GridLayout(10,1));

        //tworzenie numeracji poziomej i pionowej
        int countHelp = 0;
        char charA = 'A';

        for (int i = 10; i >= 1; i--) {
            //tworzenie głównych indeksów
            JPanel panel1 = new JPanel();
            JPanel panel2 = new JPanel();
            JLabel label1 = new JLabel(Character.toString(charA+countHelp),SwingConstants.CENTER);
            JLabel label2 = new JLabel(String.valueOf(i),SwingConstants.CENTER);
            label1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
            label2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));

            panel1.setLayout(new GridBagLayout());
            panel1.add(label1);
            panel2.setLayout(new GridBagLayout());
            panel2.add(label2);

            countHelp++;
            counterUpper.add(panel1);
            counterLeft.add(panel2);
        }
    }
    public synchronized void setArtifact(String field, Artifact artifact) {
        //field   ex.J 10
        String[] arrayField = field.split(" ");
        int fieldNumber = Integer.parseInt(arrayField[1]);
        int fieldChar = arrayField[0].charAt(0);

        //setSearchingStatus
        this.getPanels()[10-fieldNumber][fieldChar-'A'].setBackground(Color.RED);
        try {
            Thread.sleep(artifact.getExcavationTime());
        }
        catch (InterruptedException e)
        {}

        this.getPanels()[10-fieldNumber][fieldChar-'A'].setArtifact(artifact);
    }

    public synchronized PanelInside[][] getPanels() {
        return panels;
    }
}