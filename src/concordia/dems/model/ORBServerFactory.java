package concordia.dems.model;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import concordia.dems.communication.IEventManagerCommunication;
import concordia.dems.communication.IEventManagerCommunicationHelper;
import concordia.dems.communication.IEventManagerCommunicationHolder;
import concordia.dems.communication.impl.EventManagerCommunicationMontreal;
import concordia.dems.communication.impl.EventManagerCommunicationToronto;
import concordia.dems.helpers.Constants;
import concordia.dems.model.enumeration.Servers;

/**
 * This RMI Server factory create an instance of registry of rmi server of
 * dedicated city
 *
 * @author Mayank Jariwala
 * @version 1.0.0
 */
public class ORBServerFactory {

	private ORBServerFactory() {

	}

	public static IEventManagerCommunication getInstance(Servers servers) {
		IEventManagerCommunication communication = null;
		try {
			String[] args = null;
			ORB orb = ORB.init(args, null);
			// -ORBInitialPort 1050 -ORBInitialHost localhost
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = null;
			switch (servers) {
			case MONTREAL:
				ncRef = NamingContextExtHelper.narrow(objRef);
				communication = IEventManagerCommunicationHelper.narrow(ncRef.resolve_str(Constants.MTL_ORB_URL));
				break;
			case TORONTO:
				ncRef = NamingContextExtHelper.narrow(objRef);
				communication = IEventManagerCommunicationHelper.narrow(ncRef.resolve_str(Constants.TOR_ORB_URL));
				break;
			case OTTAWA:
				ncRef = NamingContextExtHelper.narrow(objRef);
				communication = IEventManagerCommunicationHelper.narrow(ncRef.resolve_str(Constants.OTW_ORB_URL));
				break;
			default:
				ncRef = NamingContextExtHelper.narrow(objRef);
				communication = IEventManagerCommunicationHelper.narrow(ncRef.resolve_str(Constants.MTL_ORB_URL));
				break;
			}

		} catch (Exception e) {
			System.out.println("Customer Exception: " + e);
			e.printStackTrace();
		}
		return communication;
	}
}
