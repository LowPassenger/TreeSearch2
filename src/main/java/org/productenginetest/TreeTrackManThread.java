package org.productenginetest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
public class TreeTrackManThread implements Runnable {
    private String rootFolder;
    private int searchDepth;

    @Override
    public void run() {
        final LinkedList<File> elements = new LinkedList<>();
        final LinkedList<File> commonElements = new LinkedList<>();
        final ArrayList<ConcurrentSkipListSet<String>> fileTree = new FileTree().getFileTree();
        final ConcurrentSkipListSet<String> levelElements = new ConcurrentSkipListSet<>();
        log.info("Start File Tree Track process. Thread info: name {}",
                Thread.currentThread().getName());
        elements.add(new File(rootFolder));
        for (int i = 0; i < searchDepth + 1; i++) {
            commonElements.clear();
            for (File element : elements) {
                if (element.isFile()) {
                    commonElements.add(element);
                }
                if (element.isDirectory()) {
                    commonElements.add(element);
                    File[] contents = element.listFiles();
                    if (contents != null) {
                        commonElements.addAll(Arrays.asList(contents));
                    }
                }
            }
            elements.addAll(commonElements);
            levelElements.clear();
            for (File common : elements) {
                levelElements.add(common.getAbsolutePath());
            }
            log.info("Depth {} complete, total records {} was added", i, levelElements.size());
            Thread.yield();
        }
           fileTree.add(levelElements);
        log.info("File Tree Tracking process is completed.");
    }
}

