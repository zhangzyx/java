package test;

import java.io.IOException;
import java.util.List;

import prev.zyx.lib.classscan.ClassScan;
import test.annoation.TestClassAnnoation;

public class TestMain {

	public static void main(String[] args) throws IOException {
		
		boolean recursive = true;//是否遍历子目录
		String pkgName = "test";
		//String pkgName = "org";
		
		List<Class<?>> classList = ClassScan.getClassList(pkgName, recursive, TestClassAnnoation.class);
		System.out.println("---------------------------------------");
		for (Class<?> clazz : classList) {
			System.out.println(clazz.getName());
		}
	}

}
