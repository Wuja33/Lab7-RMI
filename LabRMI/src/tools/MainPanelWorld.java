package tools;

import map.Map;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class MainPanelWorld extends JFrame{
    Map panelMap;

    public MainPanelWorld(String title, String filePath)
    {
        panelMap = new Map(true);
        createMap(filePath);
        this.setTitle(title);
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.add(panelMap,BorderLayout.CENTER);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void createMap(String filePath)
    {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String s;
            int xInRegion = 0;  // 0 - 80
            int xRegion = 0;  //0 - 8
            int yInRegion = 0;  // 0 - 80
            int yRegion = 0;  //0 - 8

            while ((s=bufferedReader.readLine()) != null)
            {
                String[] line = s.split("\t"); //ex. 0022000001122....
                int charCount = 0;
                while (charCount<80) {
//                    System.out.println(line[charLeft]);
                    switch (line[charCount]) {
                        case "0": {
                            panelMap.getArrayOfPanelsMain()[yRegion][xRegion].getPanels()[yInRegion][xInRegion].setArtifact(new Blank(1_000));
                            break;
                        }
                        case "1": {
                            panelMap.getArrayOfPanelsMain()[yRegion][xRegion].getPanels()[yInRegion][xInRegion].setArtifact(new Junk(3_000));
                            break;
                        }
                        case "2": {
                            panelMap.getArrayOfPanelsMain()[yRegion][xRegion].getPanels()[yInRegion][xInRegion].setArtifact(new Treasure(6_000,Category.BRONZE));
                            break;
                        }
                        case "3": {
                            panelMap.getArrayOfPanelsMain()[yRegion][xRegion].getPanels()[yInRegion][xInRegion].setArtifact(new Treasure(8_000,Category.IRON));
                            break;
                        }
                        case "4": {
                            panelMap.getArrayOfPanelsMain()[yRegion][xRegion].getPanels()[yInRegion][xInRegion].setArtifact(new Treasure(10_000,Category.SILVER));
                            break;
                        }
                        case "5": {
                            panelMap.getArrayOfPanelsMain()[yRegion][xRegion].getPanels()[yInRegion][xInRegion].setArtifact(new Treasure(14_000,Category.GOLD));
                            break;
                        }
                    }
                    xInRegion++;
                    if (xInRegion%10==0) {
                        xRegion++;
                        xInRegion = 0;
                    }
                    charCount++;
                }

                xRegion=0;
                xInRegion = 0;
                yInRegion++;

                if (yInRegion%10==0)
                {
                    yRegion++;
                    yInRegion = 0;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Fileąąą error: "+e);
        }
    }

    public synchronized Artifact getArtifact(String region, String field) {
        //region  ex.A 1
        //field   ex.J 10
        String[] array = region.split(" ");
        int regionNumber = Integer.parseInt(array[1]);
        int regionChar = array[0].charAt(0);

        String[] arrayField = field.split(" ");
        int fieldNumber = Integer.parseInt(arrayField[1]);
        int fieldChar = arrayField[0].charAt(0);

        Artifact artifact = panelMap.getArrayOfPanelsMain()[8-regionNumber][regionChar-'A'].getPanels()[10-fieldNumber][fieldChar-'A'].getArtifact();
        System.out.println(artifact.getCategory());

        //zmień na pustke
        panelMap.getArrayOfPanelsMain()[8-regionNumber][regionChar-'A'].getPanels()[10-fieldNumber][fieldChar-'A'].setEmpty();
        System.out.println(artifact.getCategory());

        return artifact;
    }


    public Map getPanelMap() {
        return panelMap;
    }
}
