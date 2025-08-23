package de.hybris.bootstrap.ddl.model;

import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.platform.core.PK;
import org.apache.commons.beanutils.DynaBean;

public class YRecord
{
    private DynaBean dynaBean = null;
    private boolean isForUpdate = false;
    private PK pk;
    private PK typePK;
    private final YDeployment deployment;
    private final YComposedType type;


    public YRecord(DynaBean dynaBean, YDeployment deployment, boolean isForUpdate)
    {
        this(dynaBean, null, deployment, isForUpdate);
    }


    public YRecord(DynaBean dynaBean, YComposedType type, YDeployment deployment, boolean isForUpdate)
    {
        this.dynaBean = dynaBean;
        this.isForUpdate = isForUpdate;
        this.deployment = deployment;
        this.type = type;
    }


    public void setPK(PK pk)
    {
        this.pk = pk;
        if(pk.getTypeCode() != this.deployment.getItemTypeCode())
        {
            throw new IllegalStateException(String.format("invalid primary key. type code of pk(%d) does not match to type code(%d) of deployment.", new Object[] {Integer.valueOf(pk.getTypeCode()), Integer.valueOf(this.deployment.getItemTypeCode())}));
        }
        set("PK", pk.getLongValue());
    }


    public void setTypePK(PK typePK)
    {
        this.typePK = typePK;
        set("TypePkString", typePK.getLongValue());
    }


    public void setBinary(String name, Object value)
    {
        this.dynaBean.set(name, toDBValue(value));
    }


    public void set(String name, Object value)
    {
        if(value instanceof Boolean)
        {
            set(name, (Boolean)value);
        }
        else
        {
            this.dynaBean.set(name, toDBValue(value));
        }
    }


    public void set(String name, long value)
    {
        set(name, toDBValue(Long.valueOf(value)));
    }


    public void set(String name, int value)
    {
        set(name, toDBValue(Integer.valueOf(value)));
    }


    public void set(String name, Boolean value)
    {
        if(value != null)
        {
            set(name, value.booleanValue());
        }
        else
        {
            set(name, false);
        }
    }


    public void set(String name, boolean value)
    {
        if(value)
        {
            set(name, 1);
        }
        else
        {
            set(name, 0);
        }
    }


    protected Object toDBValue(Object o)
    {
        return o;
    }


    public DynaBean getDynaBean()
    {
        return this.dynaBean;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public boolean isForUpdate()
    {
        return this.isForUpdate;
    }


    public YDeployment getDeployment()
    {
        return this.deployment;
    }


    public YComposedType getType()
    {
        return this.type;
    }


    public void setPk(PK pk)
    {
        this.pk = pk;
    }


    public PK getTypePK()
    {
        return this.typePK;
    }
}
