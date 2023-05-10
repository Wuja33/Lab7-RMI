package impl;

import interfaces.IWorld;
import model.Artifact;
import tools.MainPanelWorld;

import java.awt.*;
import java.rmi.RemoteException;

public class World implements IWorld {
    MainPanelWorld map;

    public World() {
        map = new MainPanelWorld("WorldApp", "mapFiles/map.txt");
        map.getPanelMap().getMainPanel().setBackground(Color.black);
    }

    @Override
    public synchronized Artifact explore(String seekerName, String sector, String field) throws RemoteException {
        return map.getArtifact(sector, field);
    }
}
