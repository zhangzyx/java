package test.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于测试类扫描的注解.<br/>
 * 
 * @author zhangyx 
 * @version Time：2017年4月6日 下午23:12:11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestClassAnnoation {
	String value() default "N/A";
}
