package com.example.administrator.xposeddemo.hook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;

public class HookUtils {

    public static void dumpClass(Class<?> actions) {

        XposedBridge.log("Dump class " + actions.getName());
        XposedBridge.log("Methods");

        // 获取到指定名称类声明的所有方法的信息
        Method[] m = actions.getDeclaredMethods();
        // 打印获取到的所有的类方法的信息
        for (int i = 0; i < m.length; i++) {

            XposedBridge.log(m[i].toString());
        }

        XposedBridge.log("Fields");
        // 获取到指定名称类声明的所有变量的信息
        Field[] f = actions.getDeclaredFields();
        // 打印获取到的所有变量的信息
        for (int j = 0; j < f.length; j++) {

            XposedBridge.log(f[j].toString());
        }

        XposedBridge.log("Classes");
        // 获取到指定名称类中声明的所有内部类的信息
        Class<?>[] c = actions.getDeclaredClasses();
        // 打印获取到的所有内部类的信息
        for (int k = 0; k < c.length; k++) {

            XposedBridge.log(c[k].toString());
        }
    }
}
