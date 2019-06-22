package concordia.dems.helpers;

import java.util.Date;

/**
 * @author Mayank Jariwala
 * @version 1.0.0
 */
public class Constants {

    public static int MTL_TOR_PORT = 2020;
    public static int MTL_OTW_PORT = 2021;
    public static int TOR_MTL_PORT = 2022;
    public static int TOR_OTW_PORT = 2023;
    public static int OTW_TOR_PORT = 2024;
    public static int OTW_MTL_PORT = 2025;

    public static int MTL_RMI_PORT = 8083;
    public static int OTW_RMI_PORT = 8084;
    public static int TOR_RMI_PORT = 8085;

    // Server url name
    public static String MTL_RMI_URL = "rmi://127.0.0.1:" + MTL_RMI_PORT + "/";
    public static String TOR_RMI_URL = "rmi://127.0.0.1:" + TOR_RMI_PORT + "/";
    public static String OTW_RMI_URL = "rmi://127.0.0.1:" + OTW_RMI_PORT + "/";

    // DateTimeStamp
    public static String TIME_STAMP = new Date().toInstant().toString();


    // Unwrapping Request Index Values
    public static int FROM_INDEX = 0;
    public static int TO_INDEX = 1;
    public static int ACTION_INDEX = 2;
    public static int INFORMATION_INDEX = 3;

    public static final int TORONTOSERVERPORT = 8889;
    public static final int OTTAWASERVERPORT = 8890;
    public static final int MONTREALSERVERPORT = 8888;
}
