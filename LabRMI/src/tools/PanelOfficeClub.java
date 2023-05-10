package tools;

import javax.swing.*;
import java.awt.*;

public class PanelOfficeClub extends JPanel {
    Color color;
    PanelOfficeClub(Color color, String clubName)
    {
        // ex. (RED SQUARE) Club1
        this.setLayout(new BorderLayout());
        this.color = color;
        JLabel clubLabel = new JLabel(clubName,SwingConstants.CENTER);
        JPanel colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(10,10));
        colorPanel.setBackground(color);
        colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));

        this.add(colorPanel,BorderLayout.WEST);
        this.add(clubLabel,BorderLayout.CENTER);
    }

    public Color getColor() {
        return color;
    }
}
