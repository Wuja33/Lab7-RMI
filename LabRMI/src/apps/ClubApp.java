package apps;

import impl.Club;
import interfaces.IClub;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClubApp {
    Club club;
    ClubApp()
    {
        IClub iClub;
        try {
            club = new Club();
            iClub = (IClub) UnicastRemoteObject.exportObject(club,0);
            club.setiClub(iClub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClubApp clubApp = new ClubApp();
    }
}

