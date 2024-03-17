package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        List<Future<?>> tasks = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(texts.length);
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }
        long startTsThread = System.currentTimeMillis();
        for (String text : texts) {
            Future<?> task = threadPool.submit(() -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
                return maxSize;
            });
            tasks.add(task);
        }
        long endTsThread = System.currentTimeMillis(); // end time
        System.out.println("Time Threads: " + (endTsThread - startTsThread) + "ms");
        int maxInterval = 0;
        for (Future<?> task : tasks) {
            int interval = (int) task.get();
            maxInterval = Math.max(maxInterval, interval);
        }
        System.out.println("Максимальный интервал значений: " + maxInterval);
        threadPool.shutdown();

//        long startTs = System.currentTimeMillis(); // start time
//        for (String text : texts) {
//            int maxSize = 0;
//            for (int i = 0; i < text.length(); i++) {
//                for (int j = 0; j < text.length(); j++) {
//                    if (i >= j) {
//                        continue;
//                    }
//                    boolean bFound = false;
//                    for (int k = i; k < j; k++) {
//                        if (text.charAt(k) == 'b') {
//                            bFound = true;
//                            break;
//                        }
//                    }
//                    if (!bFound && maxSize < j - i) {
//                        maxSize = j - i;
//                    }
//                }
//            }
//            System.out.println(text.substring(0, 100) + " -> " + maxSize);
//        }
//        long endTs = System.currentTimeMillis(); // end time
//
//        System.out.println("Time: " + (endTs - startTs) + "ms");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}