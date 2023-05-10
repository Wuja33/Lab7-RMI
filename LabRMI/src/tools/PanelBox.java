package tools;

import javax.swing.*;
import java.awt.*;

public class PanelBox extends JPanel {
    MutableComboBoxModel<String> model;
    JComboBox<String> box;
    JLabel title;
    boolean used;
    boolean adding;

    PanelBox(String title)
    {
        this.setLayout(new GridLayout(2,1,0,0));
        this.title = new JLabel(title,SwingConstants.CENTER);
        box = new JComboBox<>();
        model = new DefaultComboBoxModel<>();
        box.setModel(model);
        used = false;
        adding = false;

        this.add(this.title);
        this.add(box);
    }

    public JComboBox<String> getBox() {
        return box;
    }

    public MutableComboBoxModel<String> getModel() {
        return model;
    }

    public JLabel getTitle() {
        return title;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setAdding(boolean adding) {
        this.adding = adding;
    }

    public void setModel(MutableComboBoxModel<String> model) {
        this.model = model;
        box.setModel(model);
    }
}
