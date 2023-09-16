package com.example;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Main
{
    private static void dumpJavaClassPath()
    {
        String javaClassPath = System.getProperty("java.class.path");
        if (!javaClassPath.contains(File.pathSeparator) && javaClassPath.endsWith(".jar"))
        {
            // show jar file contents
            try (JarFile jarfile = new JarFile(javaClassPath))
            {
                System.out.printf("Main JAR: %s%n", javaClassPath);
                Manifest manifest = jarfile.getManifest();
                Attributes mainAttributes = manifest.getMainAttributes();
                System.out.printf("Main-Class is %s%n", mainAttributes.getValue(Attributes.Name.MAIN_CLASS));
                String rawClassPath = mainAttributes.getValue(Attributes.Name.CLASS_PATH);
                System.out.println("Class-Path entry:");
                if (rawClassPath == null)
                    System.out.println("   No Entries (blank or unset)");
                else
                    for (String cpEntry : rawClassPath.split(" "))
                    {
                        System.out.printf("   %s%n", cpEntry);
                    }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("java.class.path=" + javaClassPath);
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

    private static void dumpJavaProperty(String propName)
    {
        String value = System.getProperty(propName);
        System.out.printf("System.getProperty(\"%s\") = ", propName);
        if (value == null)
            System.out.println("<null> (does not exist)");
        else
            System.out.printf("\"%s\"%n", value);
    }

    public static void main(String[] args)
    {
        // To turn on DEBUG of URLClassPath ignore behaviors -Djdk.net.URLClassPath.showIgnoredClassPathEntries=true
        dumpJavaProperty("java.version");
        dumpJavaProperty("sun.misc.URLClassPath.debug");
        dumpJavaProperty("jdk.net.URLClassPath.disableRestrictedPermissions");
        dumpJavaProperty("jdk.net.URLClassPath.disableClassPathURLCheck");
        dumpJavaProperty("jdk.net.URLClassPath.showIgnoredClassPathEntries");

        System.out.println("Running in " + Main.class.getName());

        dumpJavaClassPath();

        findClass("org.eclipse.jetty.http.HttpStatus");
        findClass("org.eclipse.jetty.io.ByteBufferPool");
        findClass("org.eclipse.jetty.util.NanoTime");
        findClass("org.eclipse.jetty.xml.XmlParser");
        findClass("org.slf4j.Logger");
    }
}
