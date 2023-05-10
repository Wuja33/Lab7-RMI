package impl;

import interfaces.IClub;
import interfaces.IOffice;
import model.Blank;
import model.Treasure;
import tools.MainPanelOffice;
import model.Report;

import java.rmi.RemoteException;
import java.util.*;

public class Office implements IOffice {
    MainPanelOffice mapOffice;
    HashMap<String, IClub> listClubs;
    HashMap<String, String> listPermissions; //sector,clubName

    public Office() {
        listClubs = new HashMap<>();
        mapOffice = new MainPanelOffice("Office");
        listPermissions = new HashMap<>();
    }

    @Override
    public boolean register(IClub ic) throws RemoteException {
        synchronized (listClubs) {
            if (listClubs.size() < 64 && !listClubs.containsKey(ic.getName())) //jeśli jest miejsce i nazwa klubu nie jeszcze użyta, zarejestruj
            {
                listClubs.put(ic.getName(), ic);
                mapOffice.addClub(ic.getName());
                return true;
            } else
                return false;
        }
    }

    @Override
    public boolean unregister(String clubName) throws RemoteException {
        boolean returnValue;
        synchronized (listClubs) {
            if (listClubs.containsKey(clubName)) //jeśli jest zarejestrowany, usuń
            {
                mapOffice.deleteClub(clubName);
                listClubs.remove(clubName);
                returnValue = true;
            } else
                returnValue = false;
        }

        return returnValue;
    }

    @Override
    public boolean permissionRequest(String clubName, String sector) throws RemoteException {
        synchronized (listPermissions) {
            String[] array = sector.split(" ");
            int help;

            if (array.length == 2) // 1)A 2)10
            {
                try {
                    help = Integer.parseInt(array[1]);
                } catch (NumberFormatException e) {
                    return false;
                }
                if (array[0].length() == 1) // ex. AA 10 is incorrect
                {
                    if (array[0].charAt(0) >= 'A' && array[0].charAt(0) <= 'H' && help >= 1 && help <= 8) {
                        if (!listPermissions.containsKey(sector)) //jeśli sector nie jest zajęty, daj permisje
                        {
                            listPermissions.put(sector, clubName);
                            mapOffice.addClubSector(clubName, sector);
                            return true;
                        } else
                            return false;
                    }
                    return false;
                } else
                    return false;
            } else
                return false;
        }
    }

    @Override
    public boolean permissionEnd(String clubName, String sector) throws RemoteException {
        synchronized (listPermissions) {
            if (listPermissions.containsKey(sector)) //jeśli sector zajęty, usuń
            {
                if (listPermissions.get(sector).equals(clubName)) {
                    listPermissions.remove(sector);
                    mapOffice.deleteClubSector(sector);
                    return true;
                }
                else
                    return false;
            } else
                return false;
        }
    }

    @Override
    public boolean report(List<Report> reports, String clubName) throws RemoteException {
        for (Report report :
                reports) {
            //jeśli jest to skarb, to dodaj
//            if (report.getArtifact() instanceof Treasure)
//                mapOffice.addTreasure(report);
//            else //w przeciwnym razie dodaj pustkę
//                mapOffice.addTreasure(new Report(report.getSector(),report.getField(),new Blank(1_000)));

                mapOffice.addTreasure(report);
        }
        return false;
    }

    @Override
    public List<IClub> getClubs() throws RemoteException {
        synchronized (listClubs) {
            Collection<IClub> values = listClubs.values();

            List<IClub> listOfClubs = new ArrayList<>(values);
            System.out.println(listOfClubs);

            return listOfClubs;
        }
    }
}
