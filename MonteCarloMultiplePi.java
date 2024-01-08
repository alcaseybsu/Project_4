package project_4;

import java.util.concurrent.ThreadLocalRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MonteCarloMultiplePi {
    public static void main(String[] args) {
        // total number of random points to generate
        long[] totalSamples = {200_000, 20_000_000, 2_000_000_000};
        int numThreads = 4;

        // save start time of program
        Instant start = Instant.now();

        // separation line before loop output
        System.out.println();
        // for each number of samples to generate,
        for (long samples : totalSamples) {
            // estimate pi using multiple threads
            double piEstimate = monteCarloPiMultiThreaded(samples, numThreads);
            // save end time of the program
            Instant end = Instant.now();
            // calculate execution time
            Duration duration = Duration.between(start, end);

            System.out.println("Estimated Pi for " + formatNumber(samples) + " samples: " + piEstimate);
            System.out.println("Execution time: " + duration.toMillis() + " milliseconds");
            System.out.println();

            // add duration of this iteration to start time of next iteration
            start = end;
        }
    }

    public static double monteCarloPiMultiThreaded(long totalSamples, int numThreads) {
        // calculate the number of points to generate per thread
        long pointsPerThread = totalSamples / numThreads;

        // create thread pool with fixed no. of threads
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        // counter for no. of points inside circle
        int pointsInsideCircle = 0;

        try {
            // for each thread,
            for (int i = 0; i < numThreads; i++) {
                // submit a task to generate points and return the result
                Future<Integer> result = executor.submit(() -> monteCarloPiWorker(pointsPerThread));
                try {
                    // add the result to the counter
                    pointsInsideCircle += result.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            // shut down the thread pool
            executor.shutdown();
        }

        // return estimated value of pi
        return 4.0 * pointsInsideCircle / totalSamples;
    }

    public static int monteCarloPiWorker(long numSamples) {
        // counter for no. of points inside circle
        int pointsInsideCircle = 0;
        // for each of the samples,
        for (int i = 0; i < numSamples; i++) {
            // generate a random point
            double x = ThreadLocalRandom.current().nextDouble(-1, 1);
            double y = ThreadLocalRandom.current().nextDouble(-1, 1);

            // if point is inside circle, increment counter
            if (x * x + y * y <= 1) {
                pointsInsideCircle++;
            }
        }

        return pointsInsideCircle;
    }

    // print formatting for easier reading
    public static String formatNumber(long number) {
        if (number < 1_000) {
            return Long.toString(number);
        } else if (number < 1_000_000) {
            return (number / 1_000) + "k";
        } else if (number < 1_000_000_000) {
            return (number / 1_000_000) + "M";
        } else {
            return (number / 1_000_000_000) + "B";
        }
    }
}