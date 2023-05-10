package tools;

import impl.Seeker;
import interfaces.IClub;
import interfaces.IOffice;
import interfaces.IWorld;
import model.Artifact;
import model.Report;

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FrameSeeker extends JFrame {
    Seeker seeker;
    HashMap<String, IClub> clubList;
    MutableComboBoxModel<String> model;
    JComboBox<String> clubBox;
    JPanel mainPanel;
    JPanel panelRegister;
    PanelSeekerClub panelClub;
    JButton updateButton;
    JLabel label;
    boolean adding = false;
    boolean waitStatus = true;
    public FrameSeeker(Seeker seeker)
    {
        label = new JLabel("Club List (click to Register)",SwingConstants.CENTER);
        panelRegister = new JPanel();
        panelClub = new PanelSeekerClub(seeker);
        mainPanel = new JPanel();
        clubList = new HashMap<>();
        clubBox = new JComboBox<>();
        updateButton = new JButton("UPDATE");
        updateButton.setBackground(new Color(0x1ABE1A));
        model = new DefaultComboBoxModel<>();

        clubBox.setModel(model);

        this.seeker = seeker;
        this.setPreferredSize(new Dimension(300,200));
        this.setTitle("Unnamed");
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        mainPanel.setLayout(new CardLayout());

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        clubBox.addActionListener(e -> {
            if (!adding)
            {
                String club = String.valueOf(clubBox.getSelectedItem());
                //jeśli użytkownik nie zrezygnował
                if (setName()) {
                    try {
                        if (clubList.get(club).register(seeker.getStubSeeker())) {
                            panelClub.getClubNameLabel().setText(club);
                            seeker.setStubClub(clubList.get(club));

                            CardLayout layout = (CardLayout) mainPanel.getLayout();
                            layout.show(mainPanel, "panelClub");
                        } else
                            JOptionPane.showMessageDialog(this, "Register error!", "ERROR", JOptionPane.WARNING_MESSAGE);
                    } catch(RemoteException ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
        updateButton.addActionListener(e -> {
            try {
                importListClubs();
            } catch (RemoteException | NotBoundException ex) {
                JOptionPane.showMessageDialog(this, "Can't connect to office or world!", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        });
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    if (seeker.getStubClub()!=null)
                        seeker.getStubClub().unregister(seeker.getSeekerName());
                } catch (RemoteException e) {
                }
            }
        });


        panelRegister.setLayout(new GridLayout(3,1,0,0));
        panelRegister.add(label);
        panelRegister.add(updateButton);
        panelRegister.add(clubBox);
        mainPanel.add(panelRegister,"panelRegister");
        mainPanel.add(panelClub,"panelClub");
        this.add(mainPanel,BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.pack();
    }

    public void importListClubs() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(2000);
        IOffice stubOffice = (IOffice) registry.lookup("office");
        IWorld stubWorld = (IWorld) registry.lookup("world");

        seeker.setStubOffice(stubOffice);
        seeker.setStubWorld(stubWorld);
        adding = true;
        try {
            clubList.forEach((key,value)->{
                model.removeElement(key);
            });
            clubList.clear(); //wyczyść poprzednią listę
            List<IClub> listClubs = seeker.getStubOffice().getClubs();
            //uzupełnij listę klubów
            for (IClub i :
            listClubs) {
                clubList.put(i.getName(),i);
                model.addElement(i.getName());
            }
            adding = false;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public boolean setName()
    {
        do {
            String name = JOptionPane.showInputDialog(this,"Give seeker name:","Register",JOptionPane.WARNING_MESSAGE);
            if (name==null)
                return false;
            if (name.equals("")) {
                JOptionPane.showMessageDialog(this, "Empty name has given!", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                //jeśli wszystko w porządku, zmień imie
                seeker.setSeekerName(name);
                this.setTitle(name);
            }
                break;
        }while (true);

        return true;
    }

    public PanelSeekerClub getPanelClub() {
        return panelClub;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setWaitStatus(boolean waitStatus) {
        this.waitStatus = waitStatus;
    }

    public void doTask(String sector, String field, Artifact artifact)
    {
        final Thread threadTask = new Thread(new Runnable() {
            @Override
            public void run() {
                //utwórz SwingWorkera, który wykona oczekiwanie
                panelClub.getArtifactInfo().setText(" ");
                panelClub.getProgress().setText("Task executing... ");
                TaskWorker taskWorker = new TaskWorker(seeker,panelClub.getProgressBar(),artifact.getExcavationTime(),Thread.currentThread());
                taskWorker.execute();
                while (waitStatus) {
                    try {
                        synchronized (Thread.currentThread()) {
                            System.out.println("przed wait");
                            Thread.currentThread().wait();
                            // TODO - wyświetlanie poprawnej wartości po task i czy nie za szybko się to wykonuje
                            System.out.println("PO wait");
                        }
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println("Seeker interrupted!");
                    }
                }
                setWaitStatus(true);
                panelClub.getProgress().setText("Found: ");
                panelClub.getArtifactInfo().setText(String.valueOf(artifact.getCategory()));
                switch (artifact.getCategory())
                {
                    case GOLD:
                        panelClub.getArtifactInfo().setForeground(new Color(0xE1C40C));
                        break;
                    case IRON:
                        panelClub.getArtifactInfo().setForeground(new Color(0xc3ced1));
                        break;
                    case BRONZE:
                        panelClub.getArtifactInfo().setForeground(new Color(0xAB5E19));
                        break;
                    case SILVER:
                        panelClub.getArtifactInfo().setForeground(new Color(0xC0C0C0));
                        break;
                    case OTHER:
                        panelClub.getArtifactInfo().setForeground(new Color(0x6E6E6E));
                        break;
                    case EMPTY:
                        panelClub.getArtifactInfo().setForeground(new Color(0x000000));
                        break;
                }
                System.out.println(panelClub.getProgress().getText());
                try {
                    //zwróć raport do klubu
                    seeker.getStubClub().report(new Report(sector,field,artifact),seeker.getSeekerName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        threadTask.start();
    }
}


class TaskWorker extends SwingWorker<Void, Integer> {
    Seeker seeker;
    JProgressBar progressBar;
    int executeTime;
    final Thread thread;

    public TaskWorker(Seeker seeker,JProgressBar progressBar, int executeTime, Thread thread) {
        this.seeker = seeker;
        this.progressBar = progressBar;
        this.thread = thread;
        progressBar.setVisible(true);
        this.executeTime = executeTime/100;
        System.out.println(this.executeTime);
    }

    @Override
    protected void process(List<Integer> chunks) {
        int i = chunks.get(chunks.size()-1);
        progressBar.setValue(i); // The last value in this array is all we care about.
    }

    @Override
    protected Void doInBackground() throws Exception {
        for(int i = 0; i < 100; i++) {
            Thread.sleep(executeTime); // Illustrating long-running code.
            publish(i);
        }
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
//            JOptionPane.showMessageDialog(progressBar.getParent(), "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
            progressBar.setVisible(false);
            //powiadom wątek poszukiwacza o skończonym oczekiwaniu
            seeker.getFrameSeeker().setWaitStatus(false);
            synchronized (thread) {
                thread.notify();
                System.out.println("po notify");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}