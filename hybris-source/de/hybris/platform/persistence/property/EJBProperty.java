package de.hybris.platform.persistence.property;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInternalException;
import de.hybris.platform.util.Utilities;

public final class EJBProperty implements Cloneable
{
    private final String name;
    private final PK lang;
    private final String key;
    private Object value;
    private Object oldValue;
    private boolean inDB = false;
    private boolean hasChangedFlag = false;


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.hasChangedFlag)
        {
            throw new EJBInternalException(null, "you cannot clone a modified EJBProperty", 0);
        }
        return new EJBProperty(this.name, this.lang, this.value, this.oldValue, this.inDB, this.hasChangedFlag);
    }


    public static Object constructKey(String name, PK langPK)
    {
        return name.toLowerCase(LocaleHelper.getPersistenceLocale()) + "_" + name.toLowerCase(LocaleHelper.getPersistenceLocale());
    }


    public int hashCode()
    {
        return getKey().hashCode();
    }


    public boolean equals(Object o)
    {
        if(o instanceof EJBProperty)
        {
            EJBProperty other = (EJBProperty)o;
            return getKey().equals(other.getKey());
        }
        return false;
    }


    private void markModified()
    {
        this.hasChangedFlag = (this.inDB || this.value != null);
    }


    public boolean hasChanged()
    {
        return this.hasChangedFlag;
    }


    public boolean isInDatabase()
    {
        return this.inDB;
    }


    public void wroteChanges(boolean success)
    {
        this.hasChangedFlag = false;
        if(!this.inDB && this.value != null)
        {
            this.inDB = success;
        }
        else if(this.inDB && this.value == null)
        {
            this.inDB = !success;
        }
        if(success)
        {
            this.oldValue = this.value;
        }
        else
        {
            this.value = this.oldValue;
        }
    }


    protected boolean exists()
    {
        return (this.inDB || this.hasChangedFlag);
    }


    public Object getValue1Internal()
    {
        return this.value;
    }


    protected static EJBProperty create(String name, PK lang, Object value)
    {
        if(name == null || value == null)
        {
            throw new EJBInternalException(null, "cannot create new dump property with name or value NULL (name=" + name + ",value=" + value + ")", 0);
        }
        return new EJBProperty(name, lang, value, false);
    }


    public static EJBProperty load(String name, PK langPK, Object value)
    {
        if(name == null || value == null)
        {
            throw new EJBInternalException(null, "illegal state - should never load a dump property with name or value NULL (name=" + name + ",value=" + value + ")", 0);
        }
        return new EJBProperty(name, langPK, value, true);
    }


    private EJBProperty(String name, PK lang, Object value, boolean inDB)
    {
        this.name = name.intern();
        this.lang = lang;
        this.key = (String)constructKey(name, lang);
        this.value = value;
        this.oldValue = inDB ? value : null;
        this.inDB = inDB;
        this.hasChangedFlag = (!inDB && value != null);
    }


    private EJBProperty(String name, PK lang, Object value, Object oldValue, boolean inDB, boolean hasChanged)
    {
        this.name = name.intern();
        this.lang = lang;
        this.key = (String)constructKey(name, lang);
        this.value = value;
        this.oldValue = oldValue;
        this.inDB = inDB;
        this.hasChangedFlag = hasChanged;
    }


    public Object getKey()
    {
        return this.key;
    }


    public boolean isCommitable()
    {
        return false;
    }


    public String getName()
    {
        return this.name;
    }


    public boolean isLocalized()
    {
        return (this.lang != null && !PK.NULL_PK.equals(this.lang));
    }


    public PK getLang()
    {
        return this.lang;
    }


    public Object getValue()
    {
        return Utilities.copyViaSerializationIfNecessary(this.value);
    }


    public Object setValue(Object value)
    {
        if(value == null)
        {
            return remove();
        }
        Object old = this.value;
        if(!value.equals(old))
        {
            this.value = Utilities.copyViaSerializationIfNecessary(value);
            markModified();
        }
        return old;
    }


    public Object remove()
    {
        Object old = this.value;
        this.value = null;
        markModified();
        return old;
    }


    public void commit()
    {
    }


    public void rollback()
    {
    }


    public String toString()
    {
        return "EJBProperty(name=" + this.name + ",value=" + this.value + ",lang=" + this.lang + ",inDB=" + this.inDB + "):" +
                        System.identityHashCode(this);
    }
}
