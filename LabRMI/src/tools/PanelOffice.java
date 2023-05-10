package tools;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class PanelOffice extends JPanel {
    HashMap<String, PanelOfficeClub> listClubs;
    public PanelOffice()
    {
        this.listClubs = new HashMap<>();
//        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        this.setLayout(new GridLayout(64,1));
    }

    public synchronized void addClub(String clubName, Color color)
    {
        PanelOfficeClub panelClub = new PanelOfficeClub(color, clubName);
        listClubs.put(clubName,panelClub);
        this.add(panelClub);
        this.revalidate();
    }
    public synchronized void deleteClub(String clubName)
    {
        this.remove(listClubs.get(clubName));
        System.out.println(listClubs.get(clubName));
        this.revalidate();
        this.repaint();
        listClubs.remove(clubName);
        System.out.println(listClubs);
    }
    public HashMap<String, PanelOfficeClub> getListClubs() {
        return listClubs;
    }
}
