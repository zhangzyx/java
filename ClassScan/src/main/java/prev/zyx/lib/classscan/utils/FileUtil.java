package prev.zyx.lib.classscan.utils;

import java.io.File;
import java.io.FileFilter;

public class FileUtil {
	public static File[] filterSurffixFiles(String path, final String surffix) {
		if (path == null || "".equals(path)) {
			return null;
		}
		
		//return new File(path).listFiles(new ClassScanFileFilter(surffix));
		return new File(path).listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(surffix)) || file.isDirectory();
			}
			
		});
	}
}
