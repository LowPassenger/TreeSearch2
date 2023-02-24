package org.productenginetest;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
public class OutputThread implements Runnable {
    private final ArrayList<ConcurrentSkipListSet<String>> fileTree;
    private String searchMask;

    @Override
    public synchronized void run() {
        log.info("Output information process is started. Thread params: name {}",
                Thread.currentThread().getName());
        System.out.println("Search results are: ");
        for (ConcurrentSkipListSet<String> levelElements : fileTree) {
            for (String element : levelElements) {
                String[] filePath = element.split("/");
                String globMask = maskCorrector(searchMask);
                if ((filePath[filePath.length - 1]).matches(globMask)) {
                    System.out.println(element);
                }
                log.info("Output information process is completed.");
            }
        }
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
