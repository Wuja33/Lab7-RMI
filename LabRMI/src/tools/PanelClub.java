package tools;

import impl.Club;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PanelClub extends JPanel {
    JButton registerButton;
    JButton unRegisterButton;
    JButton permissionButton;
    MutableComboBoxModel<String> seekersAvailableBox1;
    MutableComboBoxModel<String> seekersAvailableBox2;
    HashMap<String,Integer[][]> fieldSearched;
    PanelBox panelBoxEndPermission;
    PanelBox panelBoxSector1;
    PanelBox panelBoxSector2;
    Club club;
    List<String> listPermissions;
    ScheduledThreadPoolExecutor poolExecutor;

    int countSentTaks;
    int countGottenReports;

    public PanelClub(Club club)
    {
        countSentTaks = 0;
        countGottenReports = 0;

        this.club = club;
        this.listPermissions = new ArrayList<>();
        seekersAvailableBox1 = new DefaultComboBoxModel<>();
        seekersAvailableBox2 = new DefaultComboBoxModel<>();
        fieldSearched = new HashMap<>();

        this.setPreferredSize(new Dimension(200,780));
        this.setLayout(new GridLayout(6,1,20,20));

        //tworzenie przycisków
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0x12CE12));
        unRegisterButton = new JButton("Unregister");
        unRegisterButton.setBackground(Color.gray);
        unRegisterButton.setEnabled(false);
        permissionButton = new JButton("Ask for Permission");
        permissionButton.setEnabled(false);
        panelBoxEndPermission = new PanelBox("End permission");
        panelBoxSector1 = new PanelBox("SECTOR");
        panelBoxSector2 = new PanelBox("SECTOR");
        panelBoxSector1.setModel(seekersAvailableBox1);
        panelBoxSector2.setModel(seekersAvailableBox2);

        //taskBUTTON

        //dodawania funkcjonalności przycisków
        registerButton.addActionListener(e -> {
            if(club.setOfficeStub()) {
                if (register()) {
                    registerButton.setEnabled(false);
                    registerButton.setBackground(Color.gray);
                    unRegisterButton.setEnabled(true);
                    unRegisterButton.setBackground(new Color(0xE30303));
                    permissionButton.setEnabled(true);
                }
            }
        });
        unRegisterButton.addActionListener(e -> {
            if (unRegister())
            {
                registerButton.setEnabled(true);
                registerButton.setBackground(new Color(0x12CE12));
                unRegisterButton.setEnabled(false);
                unRegisterButton.setBackground(Color.gray);
                permissionButton.setEnabled(false);
            }
        });
        permissionButton.addActionListener(e -> {
            getPermission();
        });

        //interakcje BOXES
        panelBoxEndPermission.getBox().addActionListener(e ->
        {
            if (!panelBoxEndPermission.adding) {
                String selected = String.valueOf(panelBoxEndPermission.getBox().getSelectedItem());
                deletePermission(selected);
            }
        });
        panelBoxSector1.getBox().addActionListener(e -> {
            synchronized (club.getLockSeekers()){
                    if (!isBoxSeekersAdding()) {
                        if (!panelBoxSector1.getTitle().getText().equals("SECTOR")) {
                                String selected = String.valueOf(panelBoxSector1.getBox().getSelectedItem());
                        String field = giveField(); //wczytywanie pola
                        //jeśli użytkownik podał null, nie rób nic (prawdopodobnie zrezygnował z opcji)
                        if (field != null) {
                            try {
                                if (!isFieldUsed(panelBoxSector1.getTitle().getText(), field)) {
                                    if (club.getListSeekers().get(selected).exploreTask(panelBoxSector1.getTitle().getText(), field)) {
                                        countSentTaks++;
                                        setBoxSeekersAdding(true);
                                        setFieldUsed(panelBoxSector1.getTitle().getText(), field); //ustaw pole na znalezione
                                        seekersAvailableBox1.removeElement(selected);  //usuń seekera z listy dostępnych
                                        seekersAvailableBox2.removeElement(selected);  //usuń seekera z listy dostępnych
                                        setBoxSeekersAdding(false);
                                    } else {
                                        JOptionPane.showMessageDialog(club.getMapClub(), "Seeker error", "ERROR", JOptionPane.WARNING_MESSAGE);
                                    }
                                } else
                                    JOptionPane.showMessageDialog(club.getMapClub(), "Field is searched or is used by another seeker!", "ERROR", JOptionPane.WARNING_MESSAGE);

                            } catch (RemoteException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else
                        JOptionPane.showMessageDialog(club.getMapClub(), "Get permission for sector!", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panelBoxSector2.getBox().addActionListener(e -> {
            synchronized (club.getLockSeekers()) {
                if (!isBoxSeekersAdding()) {
                    if (!panelBoxSector2.getTitle().getText().equals("SECTOR")&&!isBoxSeekersAdding()) {
                        String selected = String.valueOf(panelBoxSector2.getBox().getSelectedItem());
                        String field = giveField(); //wczytywanie pola
                        //jeśli użytkownik podał null, nie rób nic (prawdopodobnie zrezygnował z opcji)
                        if (field!=null) {
                            try {
                                if (!isFieldUsed(panelBoxSector2.getTitle().getText(), field)) {
                                    if (club.getListSeekers().get(selected).exploreTask(panelBoxSector2.getTitle().getText(), field)) {
                                        countSentTaks++;
                                        setBoxSeekersAdding(true);
                                        setFieldUsed(panelBoxSector2.getTitle().getText(), field); //ustaw pole na znalezione
                                        seekersAvailableBox2.removeElement(selected);  //usuń seekera z listy dostępnych
                                        seekersAvailableBox1.removeElement(selected);  //usuń seekera z listy dostępnych
                                        setBoxSeekersAdding(false);
                                    } else {
                                        JOptionPane.showMessageDialog(club.getMapClub(), "Seeker error", "ERROR", JOptionPane.WARNING_MESSAGE);
                                    }
                                } else
                                    JOptionPane.showMessageDialog(club.getMapClub(), "Field is searched or is used by another seeker!", "ERROR", JOptionPane.WARNING_MESSAGE);
                            } catch (RemoteException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else
                        JOptionPane.showMessageDialog(club.getMapClub(), "Get permission for sector!", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        //add components
        this.add(registerButton);
        this.add(unRegisterButton);
        this.add(permissionButton);
        this.add(panelBoxEndPermission);
        this.add(panelBoxSector1);
        this.add(panelBoxSector2);
    }


    public boolean register()
    {
        boolean registry = false;
        do {
            String name = JOptionPane.showInputDialog(club.getMapClub(),"Give club name:","Register",JOptionPane.WARNING_MESSAGE);
            if (name==null)
                return false;
            if (name.equals("")) {
                JOptionPane.showMessageDialog(club.getMapClub(), "Empty name has given!", "ERROR", JOptionPane.WARNING_MESSAGE);
                continue;
            }
            try {
                club.setClubName(name);
                registry = club.getOfficeStub().register(club);
                if (registry)
                {
                    //uruchom wątek do wysyłania raportów
                    Runnable reports =  new Runnable() {
                        @Override
                        public void run() {
                            //jeśli są jakieś raporty, to wyślij
                            if (club.getListReportsToOffice().size()>0) {
                                try {
                                    club.getOfficeStub().report(club.getListReportsToOffice(),club.getClubName());
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            club.getListReportsToOffice().clear(); //wyczyść liste raportów
                        }
                    };
                    poolExecutor = new ScheduledThreadPoolExecutor(1);
                    poolExecutor.scheduleAtFixedRate(reports,3,15, TimeUnit.SECONDS);

                    //zarejestrowano się
                    club.getMapClub().setTitle(name);
                    break;
                }
                else {
                    JOptionPane.showMessageDialog(club.getMapClub(), "Name is already used or Office is full!", "ERROR", JOptionPane.WARNING_MESSAGE);
                    return false;
                }

            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(club.getMapClub(), "Office is closed! ", "ERROR", JOptionPane.WARNING_MESSAGE);
                club.setClubName("Unnamed");
                return false;
            }
        }while (true);

        return true;
    }
    public boolean unRegister()
    {
        synchronized (club.getLockReports()) {
            //sprawdź czy ilość zadań wywowałanych jest zwróconym raportom
            if (countGottenReports != countSentTaks) {
                JOptionPane.showMessageDialog(club.getMapClub(), "Wait for all tasks to end!", "ERROR", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        boolean help = false;
        try {
           help = club.getOfficeStub().unregister(club.getClubName());
           if (help)
           {
               deleteAllPermission();
               club.getMapClub().setTitle("");
               club.setClubName(null);
               poolExecutor.shutdown();
               countSentTaks = 0;
               countGottenReports = 0;
               JOptionPane.showConfirmDialog(club.getMapClub(), "Club has been unregistered!", "INFO", JOptionPane.DEFAULT_OPTION);
               return true;
           }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(club.getMapClub(), "Office is closed! ", "ERROR", JOptionPane.WARNING_MESSAGE);
            deleteAllPermission();
            club.getMapClub().setTitle("");
            club.setClubName(null);
            poolExecutor.shutdown();
            countSentTaks = 0;
            countGottenReports = 0;
        }
        JOptionPane.showMessageDialog(club.getMapClub(), "Unregister error", "ERROR", JOptionPane.WARNING_MESSAGE);
        return false;
    }
    public String giveField()
    {
        boolean correct = false;
        do {
            String input = JOptionPane.showInputDialog(club.getMapClub(), "Give field (A 1 - J 10) ex.H 6:", "Task", JOptionPane.WARNING_MESSAGE);
            if (input==null)
                return null;
            String[] array = input.split(" ");
            int help = 0;

            if (array.length == 2) // 1)A 2)10
            {
                try {
                    help = Integer.parseInt(array[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (array[0].length() == 1) // ex. AA 10 is incorrect
                {
                    if (array[0].charAt(0) >= 'A' && array[0].charAt(0) <= 'J' && help >= 1 && help <= 10) {
                        correct = true;
                    }
                }
            }

            if (correct)
                return input;
            else
                JOptionPane.showMessageDialog(club.getMapClub(), "Incorrect name!", "ERROR", JOptionPane.WARNING_MESSAGE);
        }while (true);
    }
    public String giveSector()
    {
        boolean correct = false;
        do {
            String input = JOptionPane.showInputDialog(club.getMapClub(), "Give sector (A 1 - H 8) ex.H 6:", "Task", JOptionPane.WARNING_MESSAGE);
            String[] array = input.split(" ");
            int help = 0;

            if (array.length == 2) // 1)A 2)10
            {
                try {
                    help = Integer.parseInt(array[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (array[0].length() == 1) // ex. AA 10 is incorrect
                {
                    if (array[0].charAt(0) >= 'A' && array[0].charAt(0) <= 'H' && help >= 1 && help <= 8) {
                        correct = true;
                    }
                }
            }

            if (correct)
                return input;
            else
                JOptionPane.showMessageDialog(club.getMapClub(), "Incorrect name!", "ERROR", JOptionPane.WARNING_MESSAGE);
        }while (true);
    }
    public void getPermission() {
        boolean permission = false;
        if (listPermissions.size() < 2) {
            try {
                String sector = JOptionPane.showInputDialog(club.getMapClub(), "Give sector (A 1 - H 8) ex.H 6:", "Register", JOptionPane.WARNING_MESSAGE);
                if (sector==null)
                    return;
                permission = club.getOfficeStub().permissionRequest(club.getClubName(), sector);
                if (permission) {
                    panelBoxEndPermission.setAdding(true);
                    JOptionPane.showConfirmDialog(club.getMapClub(), "Sector " + sector + " has been added!", "Permission", JOptionPane.DEFAULT_OPTION);
                    listPermissions.add(sector);
                    panelBoxEndPermission.getModel().addElement(sector);
                    club.addSector(sector);

                    if(!panelBoxSector1.isUsed()) { //dodawanie tabelki dla poszukiwaczy
                        panelBoxSector1.getTitle().setText(sector);
                        panelBoxSector1.setUsed(true);
                    }
                    else
                    {
                        panelBoxSector2.getTitle().setText(sector);
                        panelBoxSector2.setUsed(true);
                    }

                    panelBoxEndPermission.setAdding(false);
                } else
                    JOptionPane.showMessageDialog(club.getMapClub(), "Sector " + sector + " can't be added!", "ERROR", JOptionPane.WARNING_MESSAGE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else
            JOptionPane.showMessageDialog(club.getMapClub(), "You already have 2 permissions! Delete one permission to get new one.", "ERROR", JOptionPane.WARNING_MESSAGE);
    }
    public void deletePermission(String sector)
    {
        try {
            boolean delete = club.getOfficeStub().permissionEnd(club.getClubName(),sector);

            if (delete) {
                club.deleteSector(sector);
                panelBoxEndPermission.getModel().removeElement(sector);
                listPermissions.remove(sector);

                if(panelBoxSector1.getTitle().getText().equals(sector)) { //usuwanie sektora dla poszukiwaczy
                    panelBoxSector1.getTitle().setText("SECTOR");
                    panelBoxSector1.setUsed(false);
                }
                else
                {
                    panelBoxSector2.getTitle().setText("SECTOR");
                    panelBoxSector2.setUsed(false);
                }

                JOptionPane.showConfirmDialog(club.getMapClub(), "Sector " + sector + " has been deleted!", "Permission", JOptionPane.DEFAULT_OPTION);
            }
            else
                JOptionPane.showMessageDialog(club.getMapClub(), "Sector "+sector+" can't be deleted!", "ERROR", JOptionPane.WARNING_MESSAGE);
        }catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
    public void deleteAllPermission() {
        panelBoxEndPermission.setAdding(true);
        for (String s :
               listPermissions ) {
            try {
                boolean delete = club.getOfficeStub().permissionEnd(club.getClubName(),s);

                if (delete) {
                    club.deleteSector(s);
                    panelBoxEndPermission.getModel().removeElement(s);

                    if(panelBoxSector1.getTitle().getText().equals(s)) { //usuwanie sektora dla poszukiwaczy
                        panelBoxSector1.getTitle().setText("SECTOR");
                        panelBoxSector1.setUsed(false);
                    }
                    else
                    {
                        panelBoxSector2.getTitle().setText("SECTOR");
                        panelBoxSector2.setUsed(false);
                    }
                }
                else
                    JOptionPane.showMessageDialog(club.getMapClub(), "Sector "+s+" can't be deleted!", "ERROR", JOptionPane.WARNING_MESSAGE);
            }catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        listPermissions.clear();
        panelBoxEndPermission.setAdding(false);
    }

    public void setBoxSeekersAdding(boolean x)
    {
        panelBoxSector1.setAdding(x);
        panelBoxSector2.setAdding(x);
    }
    public boolean isBoxSeekersAdding()
    {
        if (!panelBoxSector1.adding && !panelBoxSector2.adding)
        {
            return false;
        }
        else
            return true;
    }
    public boolean isFieldUsed(String sector, String field) {
        String[] array = sector.split(" ");

        int regionNumber = Integer.parseInt(array[1]);
        int regionChar = array[0].charAt(0);

        array = field.split(" ");
        int fieldNumber = Integer.parseInt(array[1]);
        int fieldChar = array[0].charAt(0);

        //create Border for club on sector
        synchronized (club.getMapClub().getPanelMap().getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A']) {
            if (club.getMapClub().getPanelMap().getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A'].isTaken())
                return true;
            else
                return false;
        }
    }
    public void setFieldUsed(String sector, String field) {
        String[] array = sector.split(" ");
        int regionNumber = Integer.parseInt(array[1]);
        int regionChar = array[0].charAt(0);

        array = field.split(" ");
        int fieldNumber = Integer.parseInt(array[1]);
        int fieldChar = array[0].charAt(0);

        //create Border for club on sector

        synchronized (club.getMapClub().getPanelMap().getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A']) {
            club.getMapClub().getPanelMap().getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A'].setTaken(true);
            //pokaż przeszukiwane przez seekera aktualnie pole
            club.getMapClub().getPanelMap().getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A'].setBorder(BorderFactory.createLineBorder(new Color(0xFF0000)));
        }

    }

    public PanelBox getPanelBoxEndPermission() {
        return panelBoxEndPermission;
    }
    public void increaseCounterGottenTasks()
    {
        countGottenReports++;
    }
    public synchronized MutableComboBoxModel<String> getSeekersAvailableBox1() {
        return seekersAvailableBox1;
    }
    public synchronized MutableComboBoxModel<String> getSeekersAvailableBox2() {
        return seekersAvailableBox2;
    }
}
