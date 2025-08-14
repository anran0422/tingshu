package com.anran.tingshu.common.minio.annotation;


import com.anran.tingshu.common.minio.config.MinioAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(MinioAutoConfiguration.class)
public @interface EnableMinioManagement {
}
