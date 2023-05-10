package impl;

import interfaces.IClub;
import interfaces.IOffice;
import interfaces.ISeeker;
import model.Artifact;
import model.Treasure;
import tools.MainPanelClub;
import model.Report;
import tools.PanelClub;

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Club implements IClub {
    MainPanelClub mapClub;
    PanelClub panelClub;
    IOffice officeStub;
    IClub iClub;
    HashMap<String,ISeeker> listSeekers;
    final Object lockReports = new Object();
    final Object lockSeekers = new Object();
    List<Report> listReportsToOffice;
    String clubName = null;

    public Club() {
        panelClub = new PanelClub(this);
        SwingUtilities.invokeLater(()->
        {
            mapClub = new MainPanelClub(panelClub);
        });
        listSeekers = new HashMap<>();
        listReportsToOffice = new ArrayList<>();
    }

    @Override
    public boolean register(ISeeker ic) throws RemoteException {
        synchronized (lockSeekers)
        {
            if (listSeekers.size()<100&&!listSeekers.containsKey(ic.getName()))
            {
                listSeekers.put(ic.getName(),ic);
                panelClub.setBoxSeekersAdding(true);
                panelClub.getSeekersAvailableBox1().addElement(ic.getName());
                panelClub.getSeekersAvailableBox2().addElement(ic.getName());
                panelClub.setBoxSeekersAdding(false);
                return true;
            }
            else
                return false;
        }
    }

    @Override
    public boolean unregister(String seekerName) throws RemoteException {
        synchronized (lockSeekers)
        {
            if (listSeekers.containsKey(seekerName))
            {
                listSeekers.remove(seekerName);
                panelClub.setBoxSeekersAdding(true);
                panelClub.getSeekersAvailableBox1().removeElement(seekerName);
                panelClub.getSeekersAvailableBox2().removeElement(seekerName);
                panelClub.setBoxSeekersAdding(false);
                return true;
            }
            else
                return false;
        }
    }

    @Override
    public String getName() throws RemoteException {
        return clubName;
    }

    @Override
    public boolean report(Report report, String seekerName) throws RemoteException {
        synchronized (lockReports)
        {
            if (report.getArtifact() instanceof Treasure)
                listReportsToOffice.add(report);

            panelClub.increaseCounterGottenTasks();
            addArtifact(report.getSector(), report.getField(), report.getArtifact());
        }
        synchronized (lockSeekers)
        {
            panelClub.setBoxSeekersAdding(true);
            panelClub.getSeekersAvailableBox1().addElement(seekerName);
            panelClub.getSeekersAvailableBox2().addElement(seekerName);
            panelClub.setBoxSeekersAdding(false);
        }
        return true;
    }

    public void addSector(String region)
    {
        String[] array = region.split(" ");
        int regionNumber = Integer.parseInt(array[1]);
        int regionChar = array[0].charAt(0);

        //create Border for club on sector
        mapClub.getPanelMap().getArrayOfPanelsMain()[8-regionNumber][regionChar-'A'].setBorder(BorderFactory.createLineBorder(Color.green,3));
    }
    public void deleteSector(String region)
    {
        String[] array = region.split(" ");
        int regionNumber = Integer.parseInt(array[1]);
        int regionChar = array[0].charAt(0);

        //delete Border for club on sector
        mapClub.getPanelMap().getArrayOfPanelsMain()[8-regionNumber][regionChar-'A'].setBorder(BorderFactory.createEmptyBorder());
    }

    public void addArtifact(String sector, String field, Artifact artifact)
    {
        String[] array = sector.split(" ");

        int regionNumber = Integer.parseInt(array[1]);
        int regionChar = array[0].charAt(0);

        array = field.split(" ");
        int fieldNumber = Integer.parseInt(array[1]);
        int fieldChar = array[0].charAt(0);
        synchronized (mapClub.getPanelMap().getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A']) {
            mapClub.getPanelMap().getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A'].setArtifact(artifact);
            //przestań pokazywać przeszukiwane pole
            mapClub.getPanelMap().getArrayOfPanelsMain()[8 - regionNumber][regionChar - 'A'].getPanels()[10 - fieldNumber][fieldChar - 'A'].setBorder(BorderFactory.createEmptyBorder());
        }
    }

    public String getClubName() {
        return clubName;
    }

    public IOffice getOfficeStub() {
        return officeStub;
    }

    public MainPanelClub getMapClub() {
        return mapClub;
    }

    public synchronized Object getLockSeekers() {
        return lockSeekers;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public void setiClub(IClub iClub) {
        this.iClub = iClub;
    }

    public boolean setOfficeStub() {
        IOffice officeStub = null;
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(2000);
            officeStub = (IOffice) registry.lookup("office");
            this.officeStub = officeStub;
            return true;
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(mapClub, "Cant connect to office!", "ERROR", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public HashMap<String, ISeeker> getListSeekers() {
        return listSeekers;
    }

    public List<Report> getListReportsToOffice() {
        return listReportsToOffice;
    }

    public synchronized Object getLockReports() {
        return lockReports;
    }
}
