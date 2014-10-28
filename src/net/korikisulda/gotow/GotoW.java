package net.korikisulda.gotow;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GotoW {
	public static void main(String[] args) throws IOException{
		new GotoW().command(args);
	}
	
	public void command(String[] args) throws IOException{
		File inputFile=new File(args[0]);

		if(args.length==0){
			System.out.println("gw.jar <filepath>");
			return;
		}else if(!inputFile.exists()){
			System.out.println("That file does not exist.");
			return;
		}
		
		JarFile jar=new JarFile(inputFile);
		
		Enumeration<JarEntry> entries=jar.entries();
		ArrayList<File> tempFiles=new ArrayList<File>();
		
		while(entries.hasMoreElements()){
			JarEntry entry=entries.nextElement();
			if(!entry.getName().endsWith(".class")) continue;
			
			File temp= File.createTempFile(sanitiseName(entry.getName()), ".class");
			tempFiles.add(temp);
			temp.deleteOnExit();
			
			InputStream is=jar.getInputStream(jar.getJarEntry(entry.getName()));
            int currentByte;
            byte data[] = new byte[8224];
            FileOutputStream fos = new FileOutputStream(temp);
            BufferedOutputStream dest = new BufferedOutputStream(fos,
            8224);
            while ((currentByte = is.read(data, 0, 8224)) != -1) {
                dest.write(data, 0, currentByte);
            }
            dest.flush();
            dest.close();
            is.close();
		}
		
		System.out.println("Extracted " + tempFiles.size() + " classes");
		for(File f:tempFiles){
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(new String[]{"javap","-c",f.getAbsolutePath()});
			BufferedReader br=new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String contents="";
			String s = null;
			while ((s = br.readLine()) != null) {
			    contents += s + "\n";
			}
			if(contents.contains("goto_w")){
				System.out.print("!");
			}else if(contents.isEmpty()){
				System.out.println("Please ensure that you have the JDK installed, and the path variable correctly set.");
			}else{
				System.out.print("_");
			}
		}
		System.out.println();
		jar.close();
	}
	
	public String sanitiseName(String name){
		HashMap<String,String> replaces=new HashMap<String,String>()
				{private static final long serialVersionUID = 1L;{
					put("\\","-");
					put("/","-");
					put("*","-");
					put("\"","-");
					put("?","-");
					put(".","-");
					put("#","-");
					put(":","-");
					put(";","-");
					put("|","-");
					put("=","-");
					put("[","-");
					put("]","-");
					put(".class","");
				}};
				for(Entry<String, String> entry:replaces.entrySet()){
					name=name.replace(entry.getKey(), entry.getValue());
				}
				return name;
	}

}
