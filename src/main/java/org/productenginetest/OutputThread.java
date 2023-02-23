package org.productenginetest;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OutputThread implements Callable<String> {
    private static final String PLUG = "empty";
    private ConcurrentHashMap<String, String> fileTree;
    private String searchMask;

    @Override
    public String call() throws Exception {
        for (Map.Entry<String, String> entry : fileTree.entrySet()) {
            String raw;
            if (entry.getValue().equals(PLUG)) {
                raw = entry.getKey();
            } else {
                raw = entry.getValue();
            }
            String[] filePath = raw.split("/");
            String globMask = maskCorrector(searchMask);
            if ((filePath[filePath.length - 1]).matches(globMask)) {
                System.out.println(raw);
            }
        }
        return "Done!";
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
