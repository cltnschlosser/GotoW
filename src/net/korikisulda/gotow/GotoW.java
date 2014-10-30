package net.korikisulda.gotow;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Edits by Travis (Toyz) on 10/29/2014.
 *
 * Original by, korikisulda
 */

public class GotoW {
    private HashMap<String, File> temp_Files = new HashMap<String, File>();

    private static GUI gui = null;

    public static void main(String[] args) throws IOException {
        System.out.println("Basic Goto_W Java Bytecode Finder");
        System.out.println("-----------------");
        System.out.println("JAVA_HOME Location: " + Utils.javaHome());
        System.out.println("JAVAP Location: " + Utils.javapPath());
        System.out.println("\n");

        if (Utils.javaHome().equals(null) || Utils.javaHome().trim().equals("")) {
            System.out.println("Set JAVA_HOME variable in your PATH settings");
            return;
        }

        if (args.length == 0) {
            System.out.println("Correct Usage: gw.jar <filepath> || GUI");
            return;
        }

        if (args[0].equals("gui")) {
            gui = new GUI(new GotoW());
            gui.setVisible(true);
        }else {
            new GotoW().command(args);
        }
    }

    public void command(String[] args) throws IOException {
        File inputFile = new File(args[0]);

        if (!inputFile.exists()) {
            if (gui == null) {
                System.out.println("That file does not exist.");
            } else {
                gui.output.append("That file does not exist.\n");
            }
            return;
        }

        JarFile jar = new JarFile(inputFile);

        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (!entry.getName().endsWith(".class")) continue;

            File temp = File.createTempFile(Utils.sanitiseName(entry.getName()), ".class");
            temp.deleteOnExit();
            temp_Files.put(entry.getName(), temp);

            InputStream is = jar.getInputStream(jar.getJarEntry(entry.getName()));

            FileOutputStream fos = new FileOutputStream(temp);
            BufferedOutputStream dest = new BufferedOutputStream(fos, 1048576);

            int currentByte;
            byte[] bytes = new byte[1024];

            while ((currentByte = is.read(bytes)) != -1) {
                dest.write(bytes, 0, currentByte);
            }

            dest.flush();
            dest.close();
            is.close();
        }

        if (gui == null) {
            System.out.println(String.format("Extracted %s classes from %s", temp_Files.size() + "", inputFile.getName()));
        } else {
            gui.output.append(String.format("Extracted %s classes from %s\n", temp_Files.size() + "", inputFile.getName()));
        }

        for (Map.Entry<String, File> f : temp_Files.entrySet()) {
            if (gui == null) {
                System.out.println("-----------------");
                System.out.println(String.format("Looking in file: %s", f.getKey()));
            } else {
                gui.output.append("-----------------\n");
                gui.output.append(String.format("Looking in file: %s\n", f.getKey()));
            }
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(new String[]{Utils.javapPath(), "-c", f.getValue().getAbsolutePath()});
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String contents = "";
            String s;
            while ((s = br.readLine()) != null) {
                contents += s + "\n";
            }
            if (contents.contains("goto_w")) {
                if (gui == null) {
                    System.out.println("Found goto_w");
                } else {
                    gui.output.append("Found goto_w\n");
                }
            } else {
                if (gui == null) {
                    System.out.print("No goto_w found.");
                } else {
                    gui.output.append("No goto_w found.\n");
                }
            }
        }
        System.out.println();
        jar.close();
    }
}
