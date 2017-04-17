package prev.zyx.lib.classscan.utils;

import java.io.File;

public class FileUtil {
	public static File[] filterSurffixFiles(String path, String surffix) {
		if (path == null || "".equals(path)) {
			return null;
		}
		
		return new File(path).listFiles(new ClassScanFileFilter(surffix));
	}
}
