package com.lulab.ch;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;


/**
 * Created by luwang on 6/28/15.
 */
public class ClassHierarchy {
  private static Set<Class> seenClasses = new HashSet<Class>();
  private String[] _interestingPrefixes;

  public ClassHierarchy(String interestingPrefixes)
  {
    _interestingPrefixes = interestingPrefixes.split(":");
  }

  private boolean isInterestingType(String typeName) {
    for (String prefix : _interestingPrefixes) {
      if (typeName.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }

  private Set<Class> getFieldClasses(Class clazz) {
    Field[] fields =  clazz.getDeclaredFields();
    Set<Class> fieldClasses = new HashSet<Class>();
    for (Field f : fields) {
      Class fieldType = f.getType();
      if (isInterestingType(fieldType.getName())) {
        fieldClasses.add(f.getType());
      }
    }
    return fieldClasses;
  }

  private void outputClassHierarchy(Class clazz) {
    if (seenClasses.contains(clazz)) {
      return;
    }
    seenClasses.add(clazz);

    Set<Class> fieldClasses = getFieldClasses(clazz);
    for (Class fieldClass : fieldClasses) {
      System.out.println("\"" + clazz.getName() + "\" -> \"" + fieldClass.getName()+ "\";");
    }

    for (Class fieldClass : fieldClasses) {
      outputClassHierarchy(fieldClass);
    }

    Class superClass = clazz.getSuperclass();
    if (superClass != null && isInterestingType(superClass.getName())) {
      System.out.println("\"" + clazz.getName() + "\" -> \"" + superClass.getName() + "\" [arrowtail=\"empty\"];");
      outputClassHierarchy(superClass);
    }
  }

  public void outputHierarchyForClasses(String[] classes) {
    try {
      for (String className : classes) {
        Class clazz = Class.forName(className);
        outputClassHierarchy(clazz);
      }
    } catch (ClassNotFoundException e ) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: ClassHierarchy prefix className...");
      System.exit(1);
    }

    String interestingPrefixes = args[0];
    String[] classes = new String[args.length - 1];
    System.arraycopy(args, 1, classes, 0, args.length - 1);

    new ClassHierarchy(interestingPrefixes).outputHierarchyForClasses(classes);

  }
}
