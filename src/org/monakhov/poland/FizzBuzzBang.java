package org.monakhov.poland;

class FizzBuzzBang {

    /**
     * Return Splat, Fizz, Buzz, or Bang depending on number
     *
     * Based on old English game of FizzBuzz.
     * Aim - read this code; I want to see all test cases,
     * ideas for refactoring
     * and convert to a function or functor for a functional langugage.
     *
     * @param number
     * @return
     */
    public final static String getFizzBuzzBang( Integer number) {

        boolean divisibleByThree = 0==(number % 3);
        boolean divisibleByFive = 0==(number % 5);
        boolean divisibleBySeven = 0==(number % 7);
        String s="Splat";

        if (divisibleByThree && !divisibleByFive  && !divisibleBySeven) {
            s= "Fizz";
        }

        if (!divisibleByThree && divisibleByFive  && !divisibleBySeven) {
            s= "Buzz";
        }

        if (!divisibleByThree && !divisibleByFive  && divisibleBySeven) {
            s= "Bang";
        }

        if (divisibleByThree && divisibleByFive  && !divisibleBySeven) {
            s= "FizzBuzz";
        }

        if (divisibleByThree && !divisibleByFive  && divisibleBySeven) {
            s= "FizzBang";
        }

        if (!divisibleByThree && divisibleByFive  && divisibleBySeven) {
            s= "BuzzBang";
        }

        if (divisibleByThree && divisibleByFive  && divisibleBySeven) {
            s= "FizzBuzzBang";
        }
        return s;
    }

    public static void main(String[] argc) {
        for (int i =0; i < 100; i++) {
            System.out.println( getFizzBuzzBang(i));
        }
    }
}