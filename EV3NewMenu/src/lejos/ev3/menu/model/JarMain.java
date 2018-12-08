package lejos.ev3.menu.model;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class JarMain {
	
	private JarMain() {}
	
	public static void executeJar(File jarFile) throws Exception {
		URLClassLoader loader = new URLClassLoader(new URL[] {(jarFile).toURI().toURL()});
	    JarFile jar = new JarFile(jarFile);
	    invokeMain(loader, jar.getManifest().getMainAttributes().getValue("Main-class"), new String[0]);
	    jar.close();
	}
	
	private static void invokeMain(URLClassLoader loader, String className, String[] args)
		    throws ClassNotFoundException,
		           NoSuchMethodException,
		           InvocationTargetException {
	    Class<?> c = loader.loadClass(className);
	    Method m = c.getMethod("main", new Class[] { args.getClass() });
	    m.setAccessible(true);
	    int mods = m.getModifiers();
	    
	    if (m.getReturnType() != void.class || !Modifier.isStatic(mods) ||
	        !Modifier.isPublic(mods)) {
	        throw new NoSuchMethodException("main");
	    }
	    
	    try {
	        m.invoke(null, new Object[] { args });
	    } catch (IllegalAccessException e) {
	        // This should not happen, as we have disabled access checks
	    }
	}
}
