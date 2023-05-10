package apps;

import impl.Office;
import interfaces.IOffice;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class OfficeApp {
    IOffice iOffice;
    OfficeApp()
    {
        try {
            iOffice = new Office();
            IOffice stub = (IOffice) UnicastRemoteObject.exportObject(iOffice, 0);

            Registry registry = LocateRegistry.getRegistry(2000);
            registry.rebind("office",stub);
        }
        catch (Exception e)
        {
            System.out.println("Error (OfficeApp): "+e);
        }
    }

    public static void main(String[] args) {
        OfficeApp officeApp = new OfficeApp();
    }
}

