package tools;

import model.Artifact;
import model.Blank;
import model.Treasure;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PanelInside extends JPanel {
    Artifact artifact;
    boolean taken = false;

    public PanelInside(Artifact artifact)
    {
        this.artifact = artifact;
    }
    public PanelInside()
    {
        this.setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
        switch (artifact.getCategory())
        {
            case EMPTY:
                this.setBackground(Color.WHITE);
                break;
            case OTHER:
                this.setBackground(new Color(0xBB81FA));
                break;
            case BRONZE:
                this.setBackground(new Color(0xB46B04));
                break;
            case IRON:
                this.setBackground(Color.gray);
                break;
            case SILVER:
                this.setBackground(new Color(0x85D2E8));
                break;
            case GOLD:
                this.setBackground(new Color(0xFFDD09));
        }
    }

    public synchronized Artifact getArtifact() {
        this.setBackground(Color.RED); //ODKRYTE MIEJSCE
        return artifact;
    }
    public synchronized Artifact setEmpty() {
        this.artifact = new Blank(1_000);
        this.setBackground(Color.RED); //ODKRYTE MIEJSCE
        return artifact;
    }
    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public boolean isTaken() {
        return taken;
    }
}
