package org.monakhov.poland;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeapTest {

    class AddFunction {
        private final int x, y;

        public AddFunction(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }

        public int calc() {
            return x + y;
        }
    }

    private final static int SIZE = 10000000, RANDOM_SIZE = 20;
    private final static int MB = 1024 * 1024;
    private final static Runtime runtime = Runtime.getRuntime();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<String> results = getResults();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        long res = (runtime.totalMemory() - runtime.freeMemory());
        System.out.println("Used Memory:" + res / MB);
    }

    private static List<String> getResults() {
        List<String> results = new ArrayList<String>();

// do random additions 10 million times and store the result in a list 

        for (int i = 0; i < SIZE; i++) {
            Random rn = new Random();
            int x = rn.nextInt(RANDOM_SIZE);
            int y = rn.nextInt(RANDOM_SIZE);
            int z = new HeapTest().new AddFunction(x, y).calc();
            String result = x + "+" + y + "=" + z;
            results.add(result);
        }

        return results;
    }
}