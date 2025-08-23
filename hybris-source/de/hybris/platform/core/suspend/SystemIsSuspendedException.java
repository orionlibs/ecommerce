package de.hybris.platform.core.suspend;

public class SystemIsSuspendedException extends RuntimeException
{
    private final String status;


    public SystemIsSuspendedException(String message)
    {
        super(message);
        this.status = SystemStatus.SUSPENDED.toString();
    }


    public SystemIsSuspendedException(String message, String status)
    {
        super(message);
        this.status = status;
    }


    public String getSystemStatus()
    {
        return this.status;
    }
}
