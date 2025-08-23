package de.hybris.platform.warehousing.process;

public class BusinessProcessException extends RuntimeException
{
    private static final long serialVersionUID = 4397866426091264312L;


    public BusinessProcessException(String message)
    {
        super(message);
    }


    public BusinessProcessException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
