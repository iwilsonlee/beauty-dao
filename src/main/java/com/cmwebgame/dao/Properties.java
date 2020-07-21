package com.cmwebgame.dao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) //说明这个注解可以注解在基本所有的位置
@Retention(RetentionPolicy.RUNTIME)   //定义成runtime将保存到java虚拟机运行时
@Documented
@Inherited
public @interface Properties {

	boolean ignore() default false;
}
