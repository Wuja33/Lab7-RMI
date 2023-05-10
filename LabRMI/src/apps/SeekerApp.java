package apps;

import impl.Seeker;
import interfaces.IOffice;
import interfaces.ISeeker;
import interfaces.IWorld;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SeekerApp {
    Seeker seeker;
    SeekerApp()
    {
        IWorld stubWorld;
        IOffice stubOffice;
        ISeeker stubSeeker;
        try {
            seeker = new Seeker();
            stubSeeker = (ISeeker) UnicastRemoteObject.exportObject(seeker,0);
            seeker.setStubSeeker(stubSeeker);
        }
        catch (RemoteException remoteException)
        {
            System.out.println("Seeker App remoteErr: "+remoteException);
        }
    }


    public static void main(String[] args) {
        SeekerApp seekerApp = new SeekerApp();
    }
}

