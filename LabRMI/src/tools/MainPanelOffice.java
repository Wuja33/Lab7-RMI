package tools;

import map.Map;
import model.Report;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainPanelOffice extends JFrame {
    Map panelMap;
    PanelOffice panelOffice;
    List<Color> colorsAvailable;

    public MainPanelOffice(String title)
    {
        panelMap = new Map(false);
        panelOffice = new PanelOffice();
        panelOffice.setPreferredSize(new Dimension(150,780));
        colorsAvailable = new ArrayList<>();

        //tworzenie kolorów
        for (int r = 255; r >= 0; r-=60) {
            for (int g = 255; g >= 0; g-=60) {
                for (int b = 255; b >= 0; b-=60) {
                    colorsAvailable.add(new Color(r,g,b));
                }
            }
        }

        this.setTitle(title);
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        panelMap.setBackground(Color.white);
        this.add(panelMap,BorderLayout.CENTER);
        this.add(panelOffice,BorderLayout.EAST);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void addClub(String clubName)
    {
        if (colorsAvailable.size()>0)
        {
            Random random = new Random();
            int randInt = random.nextInt(colorsAvailable.size());
            panelOffice.addClub(clubName,colorsAvailable.get(randInt));
            colorsAvailable.remove(randInt);
        }
    }
    public void deleteClub(String clubName)
    {
        colorsAvailable.add(panelOffice.getListClubs().get(clubName).getColor()); //zwróć kolor
        panelOffice.deleteClub(clubName); //usuń klub
    }
    public void addClubSector(String clubName,String region)
    {
        String[] array = region.split(" ");
        int regionNumber = Integer.parseInt(array[1]);
        int regionChar = array[0].charAt(0);

        //create Border for club on sector
        panelMap.getArrayOfPanelsMain()[8-regionNumber][regionChar-'A'].setBorder(BorderFactory.createLineBorder(panelOffice.getListClubs().get(clubName).getColor(),3));
    }
    public void deleteClubSector(String region)
    {
        String[] array = region.split(" ");
        int regionNumber = Integer.parseInt(array[1]);
        int regionChar = array[0].charAt(0);

        //delete Border for club on sector
        panelMap.getArrayOfPanelsMain()[8-regionNumber][regionChar-'A'].setBorder(BorderFactory.createEmptyBorder());
    }

    public void addTreasure(Report report)
    {
        String[] array = report.getSector().split(" ");
        int regionNumber = Integer.parseInt(array[1]);
        int regionChar = array[0].charAt(0);

        array = report.getField().split(" ");
        int fieldNumber = Integer.parseInt(array[1]);
        int fieldChar = array[0].charAt(0);

        //if not taken
        if (!panelMap.getArrayOfPanelsMain()[8-regionNumber][regionChar-'A'].getPanels()[10-fieldNumber][fieldChar-'A'].isTaken()) {
            //add to treasure to specific field
            panelMap.getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A'].setArtifact(report.getArtifact());

            panelMap.getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A'].setTaken(true);
        }
    }
}
