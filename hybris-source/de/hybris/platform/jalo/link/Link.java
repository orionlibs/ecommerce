package de.hybris.platform.jalo.link;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.persistence.link.LinkRemote;

public class Link extends ExtensibleItem
{
    public LinkImpl getImplementation()
    {
        return (LinkImpl)super.getImplementation();
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Object src = allAttributes.get("source");
        Object tgt = allAttributes.get("target");
        if(src == null)
        {
            throw new JaloInvalidParameterException("missing source to create a new " + type.getCode(), 0);
        }
        if(tgt == null)
        {
            throw new JaloInvalidParameterException("missing target to create a new " + type.getCode(), 0);
        }
        String quali = (String)allAttributes.get("qualifier");
        if(quali == null)
        {
            quali = type.getCode();
            if(quali.equalsIgnoreCase("Link"))
            {
                throw new JaloInvalidParameterException("untyped links always require a qualifier to be specified", 0);
            }
        }
        Integer seq = (Integer)allAttributes.get("sequenceNumber");
        Integer rSeq = (Integer)allAttributes.get("reverseSequenceNumber");
        return (Item)type.getTenant().getJaloConnection().getLinkManager().createLink(quali, (Language)allAttributes
                                        .get("language"),
                        (src instanceof Item) ? ((Item)src).getPK() : (PK)src,
                        (tgt instanceof Item) ? ((Item)tgt).getPK() : (PK)tgt,
                        (seq != null) ? seq.intValue() : 0,
                        (rSeq != null) ? rSeq.intValue() : 0);
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap copyMap = super.getNonInitialAttributes(ctx, allAttributes);
        copyMap.remove("qualifier");
        copyMap.remove("source");
        copyMap.remove("target");
        copyMap.remove("language");
        copyMap.remove("sequenceNumber");
        copyMap.remove("reverseSequenceNumber");
        return copyMap;
    }


    public void setAttribute(SessionContext ctx, String qualifier, Object value) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        if(value instanceof PK)
        {
            if("source".equalsIgnoreCase(qualifier))
            {
                setSourcePK((PK)value);
            }
            else if("target".equalsIgnoreCase(qualifier))
            {
                setTargetPK((PK)value);
            }
            else
            {
                super.setAttribute(ctx, qualifier, value);
            }
        }
        else
        {
            super.setAttribute(ctx, qualifier, value);
        }
    }


    protected void removeLinks()
    {
    }


    public static final Item ANYITEM = (Item)new Object();
    public static final Language ANYLANGUAGE = (Language)new Object();
    public static final String QUALIFIER = "qualifier";
    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    public static final String LANGUAGE = "language";
    public static final String SEQUENCE_NUMBER = "sequenceNumber";
    public static final String REVERSE_SEQUENCE_NUMBER = "reverseSequenceNumber";


    public Item getSource()
    {
        return (Item)(new Object(this, "source"))
                        .get();
    }


    public void setSource(Item source)
    {
        (new Object(this, "source", source))
                        .set();
    }


    public void setSourcePK(PK sourcePK)
    {
        ((LinkRemote)getImplementation().getRemote()).setSourcePK(sourcePK);
    }


    public Item getTarget()
    {
        return (Item)(new Object(this, "target"))
                        .get();
    }


    public void setTarget(Item target)
    {
        (new Object(this, "target", target))
                        .set();
    }


    public void setTargetPK(PK targetPK)
    {
        (new Object(this, "target", targetPK))
                        .set();
    }


    public Language getLanguage()
    {
        return (Language)getProperty(getSession().getSessionContext(), "language");
    }


    public void setLanguage(Language language)
    {
        setProperty(getSession().getSessionContext(), "language", language);
    }


    public String getQualifier()
    {
        return (String)(new Object(this, "qualifier"))
                        .get();
    }


    public void setQualifier(String qualifier)
    {
        (new Object(this, "qualifier", qualifier))
                        .set();
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        String sourceStr = (getSource() == null) ? "null" : getSource().toString();
        String targetStr = (getTarget() == null) ? "null" : getTarget().toString();
        String langStr = (getLanguage() == null) ? "null" : getLanguage().toString();
        return "Link[Qualifier: " + getQualifier() + ", Language: " + langStr + ", Source: " + sourceStr + " -> Target: " + targetStr + "]";
    }


    public int getSequenceNumber()
    {
        return ((Integer)(new Object(this, "sequenceNumber"))
                        .get()).intValue();
    }


    public void setSequenceNumber(int number)
    {
        (new Object(this, "sequenceNumber", number))
                        .set();
    }


    public int getReverseSequenceNumber()
    {
        return ((Integer)(new Object(this, "reverseSequenceNumber"))
                        .get()).intValue();
    }


    public void setReverseSequenceNumber(int number)
    {
        (new Object(this, "reverseSequenceNumber", number))
                        .set();
    }
}
