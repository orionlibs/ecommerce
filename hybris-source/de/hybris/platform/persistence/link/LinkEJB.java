package de.hybris.platform.persistence.link;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ExtensibleItemEJB;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.util.EJBTools;

public abstract class LinkEJB extends ExtensibleItemEJB implements LinkRemote, LinkHome
{
    public abstract PK getSourcePKInternal();


    public abstract void setSourcePKInternal(PK paramPK);


    public abstract PK getTargetPKInternal();


    public abstract void setTargetPKInternal(PK paramPK);


    public abstract String getQualifierInternal();


    public abstract void setQualifierInternal(String paramString);


    public abstract int getSequenceNumberInternal();


    public abstract void setSequenceNumberInternal(int paramInt);


    public abstract int getReverseSequenceNumberInternal();


    public abstract void setReverseSequenceNumberInternal(int paramInt);


    protected int typeCode()
    {
        return 7;
    }


    protected ComposedTypeRemote getDefaultType() throws EJBItemNotFoundException
    {
        try
        {
            return getEntityContext().getPersistencePool().getTenant().getSystemEJB().getTypeManager()
                            .getComposedType(getQualifierInternal());
        }
        catch(EJBItemNotFoundException e)
        {
            return super.getDefaultType();
        }
    }


    public PK getSourcePK()
    {
        return getSourcePKInternal();
    }


    public void setSourcePK(PK pk)
    {
        PK oldPK = getSourcePKInternal();
        if(oldPK != pk && (oldPK == null || !oldPK.equals(pk)))
        {
            setSourcePKInternal(pk);
        }
    }


    public PK getTargetPK()
    {
        return getTargetPKInternal();
    }


    public void setTargetPK(PK pk)
    {
        PK oldPK = getTargetPKInternal();
        if(oldPK != pk && (oldPK == null || !oldPK.equals(pk)))
        {
            setTargetPKInternal(pk);
        }
    }


    public void setSource(ItemRemote source)
    {
        setSourcePK(EJBTools.getPK(source));
    }


    public ItemRemote getSource()
    {
        return EJBTools.instantiatePK(getSourcePKInternal());
    }


    public void setTarget(ItemRemote target)
    {
        setTargetPK(EJBTools.getPK(target));
    }


    public ItemRemote getTarget()
    {
        return EJBTools.instantiatePK(getTargetPKInternal());
    }


    public void setQualifier(String str)
    {
        String old = getQualifierInternal();
        if(old != str && (old == null || !old.equals(str)))
        {
            setQualifierInternal(str);
        }
    }


    public String getQualifier()
    {
        return getQualifierInternal();
    }


    public void setSequenceNumber(int number)
    {
        setSequenceNumberInternal(number);
    }


    public int getSequenceNumber()
    {
        return getSequenceNumberInternal();
    }


    public int getReverseSequenceNumber()
    {
        return getReverseSequenceNumberInternal();
    }


    public void setReverseSequenceNumber(int position)
    {
        setReverseSequenceNumberInternal(position);
    }


    public PK ejbCreate(String quali, PK sourcePK, PK targetPK, int sequenceNumber, int reverseSequenceNumber) throws EJBInvalidParameterException
    {
        setQualifierInternal(quali);
        doCreateInternal(null, null, null, null);
        setSourcePK(sourcePK);
        setTargetPK(targetPK);
        setSequenceNumberInternal(sequenceNumber);
        setReverseSequenceNumberInternal(reverseSequenceNumber);
        return null;
    }


    public void ejbPostCreate(String quali, PK sourcePK, PK targetPK, int sequenceNumber, int reverseSequenceNumber)
    {
        doPostCreateInternal(null, null, null);
    }
}
