package de.hybris.platform.util.backoff;

import java.time.Duration;

public class ExponentialBackoffStrategy implements BackoffStrategy
{
    private static final double DEFAULT_MULTIPLIER = 1.1D;
    private static final int NUMBER_OF_ATTEMPTS = 10;
    private static final Duration DEFAULT_WAIT_TIME = Duration.ofSeconds(1L);
    private final int numberOfRetries;
    private final Duration defaultTimeToWait;
    private final double multiplier;
    private int numberOfTriesLeft;
    private Duration timeToWait;


    public ExponentialBackoffStrategy()
    {
        this(10, DEFAULT_WAIT_TIME, 1.1D);
    }


    public ExponentialBackoffStrategy(int numberOfRetries, Duration defaultTimeToWait, double multiplier)
    {
        validateConstructorArgs(numberOfRetries, defaultTimeToWait, multiplier);
        this.numberOfRetries = numberOfRetries;
        this.numberOfTriesLeft = numberOfRetries;
        this.defaultTimeToWait = defaultTimeToWait;
        this.timeToWait = defaultTimeToWait;
        this.multiplier = multiplier;
    }


    private void validateConstructorArgs(int numberOfAttempts, Duration defaultTimeToWait, double multiplier)
    {
        if(numberOfAttempts < 1)
        {
            throw new IllegalArgumentException("Number of attempts cannot be less than 1");
        }
        if(multiplier < 1.0D)
        {
            throw new IllegalArgumentException("Multiplier cannot be less than 1");
        }
        if(defaultTimeToWait.isNegative())
        {
            throw new IllegalArgumentException("Time to wait cannot be less than 0");
        }
    }


    public boolean shouldRetry()
    {
        return (this.numberOfTriesLeft > 0);
    }


    public Duration errorOccurred()
    {
        this.numberOfTriesLeft--;
        this.timeToWait = multiplyDuration(this.timeToWait, this.multiplier);
        return this.timeToWait;
    }


    private Duration multiplyDuration(Duration duration, double multiplier)
    {
        long durationInMillis = duration.toMillis();
        double adjustedDuration = durationInMillis * multiplier;
        return Duration.ofMillis((long)adjustedDuration);
    }


    int getNumberOfTriesLeft()
    {
        return this.numberOfTriesLeft;
    }


    Duration getTimeToWait()
    {
        return this.timeToWait;
    }


    public void resetBackOffState()
    {
        this.numberOfTriesLeft = this.numberOfRetries;
        this.timeToWait = this.defaultTimeToWait;
    }
}
