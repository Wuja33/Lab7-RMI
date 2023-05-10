package apps;

import impl.World;
import interfaces.IWorld;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class WorldApp {
    IWorld iWorld;
    private WorldApp()
    {
        try {
            LocateRegistry.createRegistry(2000);
            iWorld = new World();
            IWorld stub = (IWorld) UnicastRemoteObject.exportObject(iWorld, 0);

            Registry registry = LocateRegistry.getRegistry(2000);
            registry.rebind("world",stub);
        }
        catch (Exception e)
        {
            System.out.println("Error (WorldApp): "+e);
        }
    }

    public static void main(String[] args) {
        WorldApp worldApp = new WorldApp();
    }

}

