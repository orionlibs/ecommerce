package de.hybris.bootstrap.ddl.sql;

import java.util.Objects;
import org.apache.ddlutils.model.Index;

public class DbAwareUniqueIndexExtended extends DbAwareUniqueIndex implements ExtendedAwareIndex
{
    private ExtendedParamsForIndex extended;


    public DbAwareUniqueIndexExtended()
    {
    }


    public DbAwareUniqueIndexExtended(IndexCreationMode creationMode, boolean online)
    {
        super(creationMode, false);
    }


    public void setExtendedParams(ExtendedParamsForIndex extendedParams)
    {
        this.extended = extendedParams;
    }


    public ExtendedParamsForIndex getExtendedParams()
    {
        return this.extended;
    }


    public boolean equalsIgnoreCase(Index o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof DbAwareUniqueIndexExtended))
        {
            return false;
        }
        if(!super.equalsIgnoreCase(o))
        {
            return false;
        }
        DbAwareUniqueIndexExtended that = (DbAwareUniqueIndexExtended)o;
        return (this.extended == that.extended || (this.extended != null && this.extended.equalsIgnoreCase(that.extended)));
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof DbAwareUniqueIndexExtended))
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        DbAwareUniqueIndexExtended that = (DbAwareUniqueIndexExtended)o;
        return Objects.equals(this.extended, that.extended);
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {Integer.valueOf(super.hashCode()), this.extended});
    }
}
