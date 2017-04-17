package prev.zyx.lib.classscan;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import prev.zyx.lib.classscan.utils.FileUtil;

/**
 * 用于获取指定包名下的所有类名.<br/>
 * 并可设置是否遍历该包名下的子包的类名.<br/>
 * 并可通过Annotation(内注)来过滤，避免一些内部类的干扰.<br/>
 * 
 * @author zhangyx 
 * @version Time：2017年4月6日 下午23:11:34
 */
public class ClassScan {
	
	public static List<Class<?>> getClassList(String pkgName,boolean recursive, 
																	Class<? extends Annotation> annotation) throws IOException {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();//获取当前类加载器
		
		//按文件的形式查找
		String pkgFile = pkgName.replace(".", "/");
		Enumeration<URL> urls = classloader.getResources(pkgFile);
		while(urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if (url != null) {
				String protocol =  url.getProtocol();
				String pkgPath = url.getPath();
				System.out.println("protocol:" + protocol +" path:" + pkgPath);
				
				if ("file".equals(protocol)) {
					findClassName(classList, pkgName, pkgPath, recursive, annotation);
				} else if ("jar".equals(protocol)) {
					findClassName(classList,pkgName, url, recursive, annotation);
				}
			}
		}
		return classList;
	}

	private static void findClassName(List<Class<?>> classList, String pkgName, String pkgPath,
														boolean recursive, Class<? extends Annotation> annotation) {
		if (classList == null) {
			return;
		}
		
		File[] files = FileUtil.filterSurffixFiles(pkgPath, ".class");
		System.out.println(files == null?"null" : "files.length = " + files.length);
		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				if (file.isFile()) {					
					String className = getClassName(pkgName, fileName);
					addClassName(classList, className, annotation);					
				} else {
					if (recursive) {
						String subPkgName = pkgName + "." + fileName;
						String subPkgPath = pkgPath + "/" + fileName;
						findClassName(classList, subPkgName, subPkgPath, recursive, annotation);
					}
				}
			} 
		}
	}

	private static void findClassName(List<Class<?>> classList, String pkgName,
			URL url, boolean recursive, Class<? extends Annotation> annotation) throws IOException {
		JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
		JarFile jarFile = jarUrlConnection.getJarFile();
		System.out.println("jarFile :" + jarFile.getName());
		
		Enumeration<JarEntry> jarEntries =  jarFile.entries();
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarEntryName = jarEntry.getName(); // 类似：sun/security/internal/interfaces/TlsMasterSecret.class
			String className = jarEntryName.replace("/", "."); 
			
			int endindex = className.lastIndexOf(".");
			String prefix = "";
			if (endindex > 0) {
				String prefix_name = className.substring(0, endindex);
				endindex = prefix_name.lastIndexOf(".");
				if (endindex > 0) {
					prefix = prefix_name.substring(0, endindex);
				}
			}
			
			if (prefix != null && jarEntryName.endsWith(".class")) {
				if (prefix.equals(pkgName)) {
					System.out.println("jar entryName:" + jarEntryName);
					addClassName(classList, className, annotation);
				} else {
					if (recursive && prefix.startsWith(pkgName)) {
						// 遍历子包名：子类
						System.out.println("jar entryName:" + jarEntryName +" isRecursive:" + recursive);
						addClassName(classList, className, annotation);
					}
				}
			}
		}
	}
	
	private static void addClassName(List<Class<?>> classList,
			String className, Class<? extends Annotation> annotation) {
		if (classList != null && className != null && !"".equals(className)) {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if (clazz != null) {
				if (annotation == null) {
					classList.add(clazz);
					System.out.println("add: " + clazz);
				} else {
					if (clazz.isAnnotationPresent(annotation)) {
						classList.add(clazz);
						System.out.println("add(annotation): " + clazz);
					}
				}
				
			}
		}
	}

	private static String getClassName(String pkgName, String fileName) {
		String className = "";
		
		int endindex = fileName.lastIndexOf(".");
		if (endindex >= 0) {
			className = fileName.substring(0, endindex);
		}
		
		return pkgName + "." + className;
	}
}
