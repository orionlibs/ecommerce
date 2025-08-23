package de.hybris.platform.tx;

import de.hybris.platform.core.PK;

public class AfterSaveEvent
{
    public static final int UPDATE = 1;
    public static final int REMOVE = 2;
    public static final int CREATE = 4;
    private final PK pk;
    private final int type;


    public AfterSaveEvent(PK pk, int type)
    {
        if(1 != type && 2 != type && 4 != type)
        {
            throw new IllegalArgumentException("wrong operation type");
        }
        this.pk = pk;
        this.type = type;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public int getType()
    {
        return this.type;
    }


    public int hashCode()
    {
        return this.pk.hashCode() ^ this.type;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj instanceof AfterSaveEvent)
        {
            AfterSaveEvent event = (AfterSaveEvent)obj;
            if(this.type == event.type && this.pk.equals(event.pk))
            {
                return true;
            }
        }
        return false;
    }


    public String toString()
    {
        return "AfterSave[type:" + getTypeAsString() + ",item:" + this.pk + "]";
    }


    private String getTypeAsString()
    {
        switch(this.type)
        {
            case 4:
                return "CREATE";
            case 2:
                return "REMOVE";
            case 1:
                return "UPDATE";
        }
        return "UNKNOWN<" + this.type + ">";
    }
}
