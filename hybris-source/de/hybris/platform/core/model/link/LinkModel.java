package de.hybris.platform.core.model.link;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class LinkModel extends ItemModel
{
    public static final String _TYPECODE = "Link";
    public static final String LANGUAGE = "language";
    public static final String QUALIFIER = "qualifier";
    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    public static final String SEQUENCENUMBER = "sequenceNumber";
    public static final String REVERSESEQUENCENUMBER = "reverseSequenceNumber";


    public LinkModel()
    {
    }


    public LinkModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public LinkModel(ItemModel _source, ItemModel _target)
    {
        setSource(_source);
        setTarget(_target);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public LinkModel(ItemModel _owner, ItemModel _source, ItemModel _target)
    {
        setOwner(_owner);
        setSource(_source);
        setTarget(_target);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "reverseSequenceNumber", type = Accessor.Type.GETTER)
    public Integer getReverseSequenceNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("reverseSequenceNumber");
    }


    @Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
    public Integer getSequenceNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("sequenceNumber");
    }


    @Accessor(qualifier = "source", type = Accessor.Type.GETTER)
    public ItemModel getSource()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("source");
    }


    @Accessor(qualifier = "target", type = Accessor.Type.GETTER)
    public ItemModel getTarget()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("target");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }


    @Accessor(qualifier = "reverseSequenceNumber", type = Accessor.Type.SETTER)
    public void setReverseSequenceNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("reverseSequenceNumber", value);
    }


    @Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
    public void setSequenceNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("sequenceNumber", value);
    }


    @Accessor(qualifier = "source", type = Accessor.Type.SETTER)
    public void setSource(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("source", value);
    }


    @Accessor(qualifier = "target", type = Accessor.Type.SETTER)
    public void setTarget(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("target", value);
    }
}
