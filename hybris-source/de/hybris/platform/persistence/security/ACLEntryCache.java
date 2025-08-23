package de.hybris.platform.persistence.security;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInternalException;
import java.io.Serializable;

public final class ACLEntryCache implements Serializable, Cloneable
{
    private final PK user;
    private final PK permission;
    private Boolean negative;
    private boolean inDB = false;
    private boolean hasChangedFlag = false;


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.hasChangedFlag)
        {
            throw new EJBInternalException(null, "you cannot clone a modified EJBProperty", 0);
        }
        return new ACLEntryCache(this.user, this.permission, toBooleanValue(this.negative), this.inDB, this.hasChangedFlag);
    }


    public static Object constructKey(PK userPK, PK permissionPK)
    {
        return userPK.getLongValueAsString() + "_" + userPK.getLongValueAsString();
    }


    public int hashCode()
    {
        return getKey().hashCode();
    }


    public boolean equals(Object o)
    {
        if(o instanceof ACLEntryCache)
        {
            ACLEntryCache other = (ACLEntryCache)o;
            return getKey().equals(other.getKey());
        }
        return false;
    }


    private void markModified()
    {
        this.hasChangedFlag = (this.inDB || this.negative != null);
    }


    public boolean hasChanged()
    {
        return this.hasChangedFlag;
    }


    public boolean isInDatabase()
    {
        return this.inDB;
    }


    public void wroteChanges()
    {
        this.hasChangedFlag = false;
        if(this.negative == null)
        {
            this.inDB = false;
        }
        else
        {
            this.inDB = true;
        }
    }


    protected boolean exists()
    {
        return (this.inDB || this.hasChangedFlag);
    }


    protected static ACLEntryCache create(PK user, PK permission, boolean negative)
    {
        return new ACLEntryCache(user, permission, negative, false);
    }


    public static ACLEntryCache load(PK user, PK permission, boolean negative)
    {
        return new ACLEntryCache(user, permission, negative, true);
    }


    private ACLEntryCache(PK user, PK permission, boolean negative, boolean inDB)
    {
        this.user = user;
        this.permission = permission;
        this.negative = negative ? Boolean.TRUE : Boolean.FALSE;
        this.inDB = inDB;
        this.hasChangedFlag = !inDB;
    }


    private ACLEntryCache(PK user, PK permission, boolean negative, boolean inDB, boolean hasChanged)
    {
        this.user = user;
        this.permission = permission;
        this.negative = negative ? Boolean.TRUE : Boolean.FALSE;
        this.inDB = inDB;
        this.hasChangedFlag = hasChanged;
    }


    public Object getKey()
    {
        return constructKey(this.user, this.permission);
    }


    public boolean isCommitable()
    {
        return false;
    }


    public PK getPrincipal()
    {
        return this.user;
    }


    public PK getPermission()
    {
        return this.permission;
    }


    public boolean isNegative()
    {
        return toBooleanValue(this.negative);
    }


    public Boolean getNegative()
    {
        return this.negative;
    }


    public boolean setNegative(boolean negative)
    {
        Boolean old = this.negative;
        if(old == null || negative != toBooleanValue(old))
        {
            this.negative = negative ? Boolean.TRUE : Boolean.FALSE;
            markModified();
        }
        return toBooleanValue(old);
    }


    public String toString()
    {
        return "ACLEntry[" + getPrincipal() + "+" + getPermission() + "=" + getNegative() + "]";
    }


    private boolean toBooleanValue(Boolean value)
    {
        return (value != null && value.booleanValue());
    }


    public boolean remove()
    {
        Boolean old = this.negative;
        this.negative = null;
        markModified();
        return toBooleanValue(old);
    }
}
