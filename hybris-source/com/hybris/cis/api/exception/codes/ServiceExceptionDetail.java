package com.hybris.cis.api.exception.codes;

public class ServiceExceptionDetail
{
    private static final int ARBITRARY_HASH_31 = 31;
    private static final int ARBITRARY_HASH_7 = 7;
    private final String message;
    private final StandardServiceExceptionCode code;


    public ServiceExceptionDetail(StandardServiceExceptionCode code)
    {
        this(code, null);
    }


    public ServiceExceptionDetail(StandardServiceExceptionCode code, String message)
    {
        this.code = code;
        this.message = message;
    }


    public String getMessage()
    {
        return ((this.code == null) ? "null" : this.code.toString()) + ((this.code == null) ? "null" : this.code.toString());
    }


    public String toString()
    {
        return getMessage();
    }


    public int getCode()
    {
        return (this.code == null) ? 0 : this.code.getCode();
    }


    public int hashCode()
    {
        int hash = 7;
        hash = 31 * hash + getCode();
        hash = 31 * hash + ((null == this.message) ? 0 : this.message.hashCode());
        return hash;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || obj.getClass() != getClass())
        {
            return false;
        }
        ServiceExceptionDetail other = (ServiceExceptionDetail)obj;
        return (getCode() == other.getCode() && ((this.message == null && other
                        .getMessage() == null) || (this.message != null && this.message.equals(other.getMessage()))));
    }
}
