package net.korikisulda.gotow;

import java.io.File;

/**
 * Created by Travis on 10/29/2014.
 *
 * Utils.class, a better way to separate some basic logic and also we can place any external functions here
 */
public class Utils {
    public static String sanitiseName(String s) {
        return s.replaceAll("[^a-zA-Z0-9.-]", "-").replace(".class", "");
    }

    public static String javaHome(){
        return System.getenv("JAVA_HOME");
    }

    public static String javapPath(){
        return javaHome() + File.separatorChar + "bin" + File.separatorChar + "javap";
    }
}
