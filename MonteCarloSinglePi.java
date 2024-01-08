package project_4;

import java.util.concurrent.ThreadLocalRandom;
import java.time.Duration;
import java.time.Instant;

public class MonteCarloSinglePi {
    public static void main(String[] args) {
        // total number of random points to generate
        long[] totalSamples = {200_000, 20_000_000, 2_000_000_000};

        // save start time of program
        Instant start = Instant.now();

        // separation line before loop output
        System.out.println();
        // for each number of samples to generate,
        for (long samples : totalSamples) {
            // estimate pi using a single thread
            double piEstimate = monteCarloPiSingleThread((int) samples);
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

    public static double monteCarloPiSingleThread(long totalSamples) {
        // counter for number of points inside circle
        int pointsInsideCircle = 0;

        // for each of the samples,
        for (int i = 0; i < totalSamples; i++) {
            // generate a random point
            double x = ThreadLocalRandom.current().nextDouble(-1, 1);
            double y = ThreadLocalRandom.current().nextDouble(-1, 1);

            // if the point is inside the circle, increment counter
            if (x * x + y * y <= 1) {
                pointsInsideCircle++;
            }
        }

        // return the estimated value of pi
        return 4.0 * pointsInsideCircle / totalSamples;
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