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
    private static GUI gui = null;

    public static void main(String[] args) throws IOException {
        System.out.println("Basic Goto_W Java Bytecode Finder");
        System.out.println("-----------------");
        System.out.println("JAVA_HOME Location: " + Utils.javaHome());
        System.out.println("JAVAP Location: " + Utils.javapPath());
        System.out.println("\n");

        if (Utils.javaHome()==null || Utils.javaHome()==null?true:Utils.javaHome().trim().equals("")) {
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
        HashMap<String, File> temp_Files = new HashMap<String, File>();
        File inputFile = new File(args[0]);

        if (!inputFile.exists()) {

               println("That file does not exist.");

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

            println(String.format("Extracted %s classes from %s", temp_Files.size() + "", inputFile.getName()));

        for (Map.Entry<String, File> f : temp_Files.entrySet()) {

                println("-----------------");
                println(String.format("Looking in file: %s", f.getKey()));

            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(new String[]{Utils.javapPath(), "-c", f.getValue().getAbsolutePath()});
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String contents = "";
            String s;
            while ((s = br.readLine()) != null) {
                contents += s + "\n";
            }
            if (contents.contains("goto_w")) {
            	println("Found goto_w");
            } else {
            	println("No goto_w found.");
            }
        }
        System.out.println();
        jar.close();
    }
    
    public void print(String ... text){
    	String concat="";
    	for(String s:text) concat+=s;
        if (gui == null) {
            System.out.print(concat);
        }else{
        	gui.output.append(concat);
        }
    }
    
    public void println(String ... text){
    	String concat="";
    	for(String s:text) concat+=s;
    	concat+="\n";
        if (gui == null) {
            System.out.print(concat);
        }else{
        	gui.output.append(concat);
        }
    }
}
