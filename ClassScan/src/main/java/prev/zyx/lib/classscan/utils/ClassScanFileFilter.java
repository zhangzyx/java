package prev.zyx.lib.classscan.utils;

import java.io.File;
import java.io.FileFilter;

public  class ClassScanFileFilter implements FileFilter {
	private String surffix = "";

	public ClassScanFileFilter(String surffix) {
		super();
		this.surffix = surffix;
	}
	
	@Override
	public boolean accept(File file) {
		return (file.isFile() && file.getName().endsWith(surffix)) || file.isDirectory();
	}
}
