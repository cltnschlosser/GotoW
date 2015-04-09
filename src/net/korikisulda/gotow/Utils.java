package net.korikisulda.gotow;

import java.io.File;

/**
 * Created by Travis (Toyz) on 10/29/2014.
 * <p/>
 * Utils.class, a better way to separate some basic logic and also we can place any external functions here
 */
public class Utils {
    private static GUI gui;

    public static String sanitiseName(String s) {
        return s.replaceAll("[^a-zA-Z0-9.-]", "-").replace(".class", "");
    }

    public static String javaHome() {
        return System.getenv("JAVA_HOME");
    }

    public static String javapPath() {
        return javaHome() + File.separatorChar + "bin" + File.separatorChar + "javap";
    }

    public static void print(String... text) {
        String concat = "";
        for (String s : text) concat += s;
        if (gui == null) {
            System.out.print(concat);
        } else {
            gui.output.append(concat);
        }
    }

    public static void println(String... text) {
        text[text.length - 1] += "\n";
        print(text);
    }

    public static GUI getGui() {
        return gui;
    }

    public static void createGui() {
        gui = new GUI();
        gui.setVisible(true);
    }

    public static boolean hasGotoW(String content){
       return content.contains("goto_w");
    }
}
