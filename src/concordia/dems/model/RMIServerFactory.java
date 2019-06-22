package concordia.dems.model;

import concordia.dems.communication.IEventManagerCommunication;
import concordia.dems.helpers.Constants;
import concordia.dems.model.enumeration.Servers;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This RMI Server factory create an instance of registry of rmi server of dedicated city
 *
 * @author Mayank Jariwala
 * @version 1.0.0
 */
public class RMIServerFactory {

    private RMIServerFactory() {

    }

    public static IEventManagerCommunication getInstance(Servers servers) {
        Registry registry;
        IEventManagerCommunication communication = null;
        try {
            switch (servers) {
                case MONTREAL:
                    registry = LocateRegistry.getRegistry(Constants.MTL_RMI_PORT);
                    communication = (IEventManagerCommunication) registry.lookup(Constants.MTL_RMI_URL);
                    break;
                case TORONTO:
                    registry = LocateRegistry.getRegistry(Constants.TOR_RMI_PORT);
                    communication = (IEventManagerCommunication) registry.lookup(Constants.TOR_RMI_URL);
                    break;
                case OTTAWA:
                    registry = LocateRegistry.getRegistry(Constants.OTW_RMI_PORT);
                    communication = (IEventManagerCommunication) registry.lookup(Constants.OTW_RMI_URL);
                    break;
                default:
                    break;
            }
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return communication;
    }
}
