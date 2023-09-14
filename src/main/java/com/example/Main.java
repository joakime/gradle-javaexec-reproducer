package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Main
{
    private static void dumpJarManifest(Class<Main> clazz)
    {
        URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
        if ("jar".equals(location.getProtocol()))
        {
            // show jar file contents
            try (JarFile jarfile = new JarFile(new File(location.toURI())))
            {
                System.out.printf("Main JAR: %s%n", location);
                Manifest manifest = jarfile.getManifest();
                Attributes mainAttributes = manifest.getMainAttributes();

                System.out.printf("Main-Class is %s%n", mainAttributes.getValue(Attributes.Name.MAIN_CLASS));
                System.out.printf("Class-Path is %s%n", mainAttributes.getValue(Attributes.Name.CLASS_PATH));
            }
            catch (IOException | URISyntaxException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            // show location on disk
            System.out.printf("Main Class Loaded from: %s%n", location);
        }
    }

    private static void findClass(String clazzname)
    {
        try
        {
            Class<?> clazz = Class.forName(clazzname);
            System.out.printf("Class %s found in %s%n", clazz, clazz.getProtectionDomain().getCodeSource().getLocation());
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        // To turn on DEBUG of URLClassPath ignore behaviors -Djdk.net.URLClassPath.showIgnoredClassPathEntries=true
        System.out.println("Running in " + Main.class.getName());

        System.out.printf("java.class.path = %s%n", System.getProperty("java.class.path"));
        dumpJarManifest(Main.class);

        findClass("org.eclipse.jetty.http.HttpStatus");
        findClass("org.eclipse.jetty.io.ByteBufferPool");
        findClass("org.eclipse.jetty.util.NanoTime");
        findClass("org.eclipse.jetty.xml.XmlParser");
        findClass("org.slf4j.Logger");
    }
}
