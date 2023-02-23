package org.productenginetest;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TreeTrackManThread implements Callable<String> {
    private static final String PLUG = "empty";
    private ConcurrentHashMap<String, String> fileTree;
    private final LinkedList<File> elements = new LinkedList<>();
    private final LinkedList<File> levelElements = new LinkedList<>();
    private String rootFolder;
    private int searchDepth;

    @Override
    public String call() throws InterruptedException {
        levelElements.add(new File(rootFolder));
        LinkedList<File> commonElements = new LinkedList<>();
        for (int i = -1; i < searchDepth; i++) {
            commonElements.clear();
            for (File element : levelElements) {
                if (element.isFile()) {
                    elements.add(element);
                }
                if (element.isDirectory()) {
                    elements.add(element);
                    File[] contents = element.listFiles();
                    if (contents != null) {
                        commonElements.addAll(Arrays.asList(contents));
                    }
                }
            }
            elements.addAll(commonElements);
            levelElements.clear();
            levelElements.addAll(commonElements);
        }
        for (File element : elements) {
            if (element.isDirectory()) {
                fileTree.put(element.getAbsolutePath(), PLUG);
            }
            if (element.isFile()) {
                fileTree.put(element.getParent(), element.getAbsolutePath());
            }
        }
        return "File search complete!";
    }
}

