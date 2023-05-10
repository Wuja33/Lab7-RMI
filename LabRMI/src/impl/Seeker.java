package impl;

import interfaces.IClub;
import interfaces.IOffice;
import interfaces.ISeeker;
import interfaces.IWorld;
import model.Artifact;
import tools.FrameSeeker;

import java.rmi.RemoteException;

public class Seeker implements ISeeker {
    IWorld stubWorld;
    IOffice stubOffice;
    IClub stubClub;
    ISeeker stubSeeker;
    FrameSeeker frameSeeker;
    String seekerName;

    public Seeker()
    {
        frameSeeker = new FrameSeeker(this);
    }

    @Override
    public boolean exploreTask(String sector, String field) throws RemoteException {
        Artifact artifact;
        try {
            artifact = stubWorld.explore(seekerName,sector,field);
            System.out.println(artifact.getCategory());
            System.out.println(artifact.getExcavationTime());

            frameSeeker.doTask(sector,field,artifact);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }



    @Override
    public String getName() throws RemoteException {
        return seekerName;
    }


    public void setStubOffice(IOffice stubOffice) {
        this.stubOffice = stubOffice;
    }

    public void setStubSeeker(ISeeker stubSeeker) {
        this.stubSeeker = stubSeeker;
    }

    public void setStubWorld(IWorld stubWorld) {
        this.stubWorld = stubWorld;
    }

    public void setSeekerName(String seekerName) {
        this.seekerName = seekerName;
    }

    public String getSeekerName() {
        return seekerName;
    }

    public ISeeker getStubSeeker() {
        return stubSeeker;
    }

    public IOffice getStubOffice() {
        return stubOffice;
    }

    public IClub getStubClub() {
        return stubClub;
    }

    public void setStubClub(IClub stubClub) {
        this.stubClub = stubClub;
    }

    public FrameSeeker getFrameSeeker() {
        return frameSeeker;
    }

}
