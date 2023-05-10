package tools;

import impl.Seeker;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class PanelSeekerClub extends JPanel {
    Seeker seeker;
    JLabel clubNameLabel;
    JProgressBar progressBar;
    JPanel progressInfo;
    JLabel progress;
    JLabel artifactInfo;
    JButton buttonUnRegister;

    PanelSeekerClub(Seeker seeker)
    {
        this.setName("panelClub");
        this.seeker = seeker;
        this.setLayout(new GridLayout(4,1,0,0));
        clubNameLabel = new JLabel("",SwingConstants.CENTER);
        progressInfo = new JPanel();
        progressInfo.setLayout(new GridLayout(1,2));
        progress = new JLabel("Waiting for task...",SwingConstants.CENTER);
        artifactInfo = new JLabel(" ",SwingConstants.CENTER);
//        artifactInfo.setPreferredSize(new Dimension(60,30));
        progressInfo.add(progress,BorderLayout.CENTER);
        progressInfo.add(artifactInfo,BorderLayout.EAST);

        buttonUnRegister = new JButton("Unregister");
        progressBar = new JProgressBar();
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        buttonUnRegister.setBackground(new Color(0xDE1313));

        buttonUnRegister.addActionListener(e -> {
            try {
                if (seeker.getStubClub().unregister(seeker.getSeekerName()))
                {
                    seeker.setStubClub(null);
                    seeker.setSeekerName("");
                    seeker.getFrameSeeker().setTitle("Unnamed");
                    progress.setText("Waiting for task...");
                    artifactInfo.setText(" ");
                    CardLayout layout = (CardLayout) seeker.getFrameSeeker().mainPanel.getLayout();
                    layout.show(seeker.getFrameSeeker().mainPanel,"panelRegister");

                }
                else
                    JOptionPane.showMessageDialog(this, "Unregister error!", "ERROR", JOptionPane.WARNING_MESSAGE);
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Club has been closed!", "ERROR", JOptionPane.WARNING_MESSAGE);
                seeker.setStubClub(null);
                seeker.setSeekerName("");
                seeker.getFrameSeeker().setTitle("Unnamed");
                progress.setText("Waiting for task...");
                artifactInfo.setText(" ");
                CardLayout layout = (CardLayout) seeker.getFrameSeeker().mainPanel.getLayout();
                layout.show(seeker.getFrameSeeker().mainPanel,"panelRegister");
            }
        });
        this.add(clubNameLabel);
        this.add(progressBar);
        this.add(progressInfo);
        this.add(buttonUnRegister);
    }

    public JLabel getClubNameLabel() {
        return clubNameLabel;
    }

    public JLabel getProgress() {
        return progress;
    }

    public JLabel getArtifactInfo() {
        return artifactInfo;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
}
