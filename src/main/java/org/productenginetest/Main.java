package org.productenginetest;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Please enter the root folder for search: ");
        String rootPath = readLineFromKeyboard();
        while (!new File(rootPath).exists()) {
            System.out.println("Bed root path or directory doesn't exist. ");
            rootPath = tryAgain();
        }
        int searchDepth = 0;
        while (true) {
            readDepth:
            {
                System.out.println("Please enter the positive integer depth (0...50) for search: ");
                try {
                    searchDepth = Integer.parseInt(readLineFromKeyboard());
                    if (searchDepth < 0 || searchDepth > 50) {
                        break readDepth;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(("Wrong number format, you should input a number"));
                }
            }
        }
        System.out.println("Please enter the mask for search: ");
        String searchMask = readLineFromKeyboard();
        while (searchMask == null || searchMask.isEmpty()) {
            searchMask = tryAgain();
        }
        scanner.close();

        Semaphore semaphore = new Semaphore(searchDepth + 1, true);
        ArrayList<ConcurrentSkipListSet<String>> fileTree = new FileTree().getFileTree();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < searchDepth + 1; i++) {
            Future<ArrayList<ConcurrentSkipListSet<String>>> treeTrackMan =
                    executorService.submit(new TreeTrackManThread(semaphore, fileTree,
                            rootPath));
            log.info("Start new Thread {} for File Tree TrackMan. "
                    + "Params: rootPath {}, search depth {}", treeTrackMan, rootPath, searchDepth);

            Future<String> output = executorService.submit(new OutputThread(semaphore, fileTree,
                    searchMask));
            log.info("Start new Thread {} for File Tree TrackMan. "
                    + "Params: search mask {}", output, searchMask);
        }
        executorService.shutdown();
    }

    private static String readLineFromKeyboard() {
        return scanner.nextLine();
    }

    private static String tryAgain() {
        System.out.print("Please try again! " + System.lineSeparator());
        return scanner.nextLine();
    }
}
