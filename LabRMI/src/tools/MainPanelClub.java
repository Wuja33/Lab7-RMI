package tools;

import map.Map;

import javax.swing.*;
import java.awt.*;

public class MainPanelClub extends JFrame {
    Map panelMap;
    PanelClub panelClub;

    public MainPanelClub(PanelClub panelClub)
    {
        panelMap = new Map(false);
        this.panelClub = panelClub;
        panelClub.setPreferredSize(new Dimension(150,780));
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.add(panelMap,BorderLayout.CENTER);
        this.add(panelClub,BorderLayout.EAST);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (panelClub.club.getOfficeStub()!=null)
                    panelClub.unRegister();
            }
        });
    }

    public Map getPanelMap() {
        return panelMap;
    }
}
