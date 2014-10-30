package net.korikisulda.gotow;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GotoW {
    public static void main(String[] args) throws IOException {
        System.out.println("JAVA_HOME Location: " + Utils.javaHome());
        System.out.println("JAVAP Location: " + Utils.javapPath());

        if(Utils.javaHome().equals(null)){
            System.out.println("Set JAVA_HOME variable in your PATH settings");
            return;
        }

        if (args.length == 0) {
            System.out.println("Correct Usage: gw.jar <filepath> || GUI");
            return;
        }

        if(args[0].equals("gui")){
            System.out.println("Coming Soon...");
            return;
        }

        new GotoW().command(args);
    }

    public void command(String[] args) throws IOException {
        File inputFile = new File(args[0]);

        if (!inputFile.exists()) {
            System.out.println("That file does not exist.");
            return;
        }

        JarFile jar = new JarFile(inputFile);

        Enumeration<JarEntry> entries = jar.entries();
        ArrayList<File> tempFiles = new ArrayList<File>();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (!entry.getName().endsWith(".class")) continue;

            File temp = File.createTempFile(Utils.sanitiseName(entry.getName()), ".class");
            tempFiles.add(temp);
            temp.deleteOnExit();

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

        System.out.println("Extracted " + tempFiles.size() + " classes");
        for (File f : tempFiles) {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(new String[]{Utils.javapPath(), "-c", f.getAbsolutePath()});
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String contents = "";
            String s;
            while ((s = br.readLine()) != null) {
                contents += s + "\n";
            }
            if (contents.contains("goto_w")) {
                System.out.print("!");
            } else if (contents.isEmpty()) {
                System.out.println("Please ensure that you have the JDK installed, and the path variable correctly set.");
            } else {
                System.out.print("_");
            }
        }
        System.out.println();
        jar.close();
    }
}
