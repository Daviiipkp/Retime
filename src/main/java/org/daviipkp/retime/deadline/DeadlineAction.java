package org.daviipkp.retime.deadline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.javalin.router.Endpoint;

public abstract class DeadlineAction {

    private static final ForkJoinPool POOL = new ForkJoinPool(
        Runtime.getRuntime().availableProcessors()
    );

    private boolean hasExpired = false;
    protected Runnable action;

    public abstract void setupFromMetadata(String metadata);

    public DeadlineAction(Runnable r) {
        action = r;
    }

    public void call() {
        if(hasExpired && action == null) {
            throw new RuntimeException("Can't call action.");
        }

        POOL.submit(action); 

        hasExpired=true;

    }

    public static Class<? extends DeadlineAction> getActionById(short id) {

        String packageN = "org.daviipkp.retime.deadline";

        try (ScanResult scanResult = new ClassGraph()
                .enableAnnotationInfo() 
                .acceptPackages(packageN)  
                .scan()) {

            ClassInfoList cl = scanResult.getClassesWithAnnotation(Action.class);

            for (ClassInfo classInfo : cl) {
                AnnotationInfo info = classInfo.getAnnotationInfo(Action.class);
                short a = (short) info.getParameterValues().getValue("id");
                if(a == id) {
                    @SuppressWarnings("unchecked")
                    Class<? extends DeadlineAction> clazz = (Class<? extends DeadlineAction>) classInfo.loadClass();
                    return clazz;
                }
            }
        }

        return null;
    }

    public static short getIdByName(String name) {
        String packageN = "org.daviipkp.retime.deadline";

        try (ScanResult scanResult = new ClassGraph()
                .enableAnnotationInfo() 
                .acceptPackages(packageN)  
                .scan()) {

            ClassInfoList cl = scanResult.getClassesWithAnnotation(Action.class);

            for (ClassInfo classInfo : cl) {
                AnnotationInfo info = classInfo.getAnnotationInfo(Action.class);
                String a = (String) info.getParameterValues().getValue("name");
                if(a.equals(name)) {
                    return (short) info.getParameterValues().getValue("id");
                }
            }
        }

        return -1;
    }



}
