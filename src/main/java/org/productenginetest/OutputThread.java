package org.productenginetest;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Exchanger;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
public class OutputThread implements Callable<String> {
    private final Exchanger<ArrayList<ConcurrentSkipListSet<String>>> exchanger;
    private ArrayList<ConcurrentSkipListSet<String>> fileTree;
    private String searchMask;

    @Override
    public String call() throws InterruptedException {
        log.info("Output information process is started. Thread params: name {}",
                Thread.currentThread().getName());
        fileTree = exchanger.exchange(fileTree);
        if (fileTree.size() == 1) {
            System.out.println("Search results are: ");
        }
        ConcurrentSkipListSet<String> levelElements = fileTree.get(fileTree.size() - 1);
        for (String element : levelElements) {
            String[] filePath = element.split("/");
            String globMask = maskCorrector(searchMask);
            if ((filePath[filePath.length - 1]).matches(globMask)) {
                System.out.println(element);
            }
        }
        log.info("Output information process is completed.");
        return "OutputThread";
    }

    private String maskCorrector(String searchMask) {
        StringBuilder correctMask = new StringBuilder();
        correctMask.append("^");
        for (int i = 0; i < searchMask.length(); ++i) {
            char symbol = searchMask.charAt(i);
            switch (symbol) {
                case '*': correctMask.append(".*");
                    break;
                case '?': correctMask.append('.');
                    break;
                case '.': correctMask.append("\\.");
                    break;
                case '\\': correctMask.append("\\\\");
                    break;
                default : correctMask.append(symbol);
            }
        }
        correctMask.append('$');
        return correctMask.toString();
    }
}
