/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 *
 * @author summers
 */
public class ObjectUtils {

    public static String printTree(Object object, int maxDepth) {
        StringBuilder builder = new StringBuilder();
        printTree(object, maxDepth, 0, new HashSet(), builder);
        return builder.toString();
    }

    private static void printTree(Object object, int maxDepth, int depth, Set<Object> stash, StringBuilder tree) {

        tree.append("{").append('\n');
        IntStream.range(0, depth + 1).forEach(i -> tree.append('\t'));
        if (object == null) {
            tree.append("null").append('\n');
            IntStream.range(0, depth).forEach(i -> tree.append('\t'));
            tree.append("}\n");
            IntStream.range(0, depth).forEach(i -> tree.append('\t'));
            return;
        }
//        if (stash.contains(object)) {
//            tree.append(" circular reference detected }").append('\n');
//            return;
//        } 
        if (depth > maxDepth) {
            tree.append(" depth exceeded").append('\n');
            IntStream.range(0, depth).forEach(i -> tree.append('\t'));
            tree.append("}\n");
            IntStream.range(0, depth).forEach(i -> tree.append('\t'));
            return;
        }

        if (object instanceof String || object.getClass().isPrimitive() || object instanceof Class || object instanceof File) {
            tree.append(object).append('\n');
            IntStream.range(0, depth).forEach(i -> tree.append('\t'));
            tree.append("}\n");
            IntStream.range(0, depth).forEach(i -> tree.append('\t'));
            return;
        }

        stash.add(object);

        try {

            if (object instanceof List || object instanceof Set) {
                int index = 0;
                Collection objectCollection = (Collection) object;
                for (Object collectionItem : objectCollection) {
                    printTree(collectionItem, maxDepth, depth + 1, stash, tree.append("[").append(index).append("]"));
                }
            } else if (object instanceof Map) {

                Map objectMap = (Map) object;
                objectMap.forEach((key, value) -> {
                    printTree(value, maxDepth, depth + 1, stash, tree.append("[").append(key.toString()).append("]"));
                });

            } else if (object.getClass().isArray()) {
                for (int i = 0; i < ((Object[]) object).length; i++) {

                    printTree(((Object[]) object)[i], maxDepth, depth + 1, stash, tree.append("[").append(i).append("]"));
                }
            } else {
                Method[] classMethods = object.getClass().getMethods();
                for (Method method : classMethods) {
                    if (method.getName().startsWith("get") && method.getParameterCount() == 0 && !method.getName().equals("getClass")) {
                        printTree(method.invoke(object, (Object[]) null), maxDepth, depth + 1, stash, tree.append(method.getName()));
                    }
                }
            }

        } catch (Throwable t) {
            Logger.getAnonymousLogger().log(Level.SEVERE, t.getMessage(), t);
        }
        //IntStream.range(0, depth).forEach(i -> tree.append('\t'));
        tree.append("}\n");
        IntStream.range(0, depth).forEach(i -> tree.append('\t'));

    }
}
