package org.productenginetest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
public class TreeTrackManThread implements Callable<String> {
    private final ArrayList<ConcurrentSkipListSet<String>> fileTree;
    private String rootFolder;
    private int searchDepth;

    @Override
    public String call() {
        TreeSet<File> elements = new TreeSet<>();
        final TreeSet<File> commonElements = new TreeSet<>();
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
            commonElements.removeAll(elements);
            elements = new TreeSet<>(commonElements);
            ConcurrentSkipListSet<String> levelElements = new ConcurrentSkipListSet<>();
            for (File common : elements) {
                levelElements.add(common.getAbsolutePath());
            }
            log.info("Depth {} complete, total records {} was added", i, levelElements.size());
            fileTree.add(levelElements);
        }
        log.info("File Tree Tracking process is completed.");
        return "TreeTrackManThread";
    }
}

