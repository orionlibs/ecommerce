package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ImpexDocumentIdModel extends ItemModel
{
    public static final String _TYPECODE = "ImpexDocumentId";
    public static final String PROCESSCODE = "processCode";
    public static final String DOCID = "docId";
    public static final String ITEMQUALIFIER = "itemQualifier";
    public static final String ITEMPK = "itemPK";
    public static final String RESOLVED = "resolved";


    public ImpexDocumentIdModel()
    {
    }


    public ImpexDocumentIdModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpexDocumentIdModel(String _docId, PK _itemPK, String _itemQualifier, Boolean _resolved)
    {
        setDocId(_docId);
        setItemPK(_itemPK);
        setItemQualifier(_itemQualifier);
        setResolved(_resolved);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpexDocumentIdModel(String _docId, PK _itemPK, String _itemQualifier, ItemModel _owner, Boolean _resolved)
    {
        setDocId(_docId);
        setItemPK(_itemPK);
        setItemQualifier(_itemQualifier);
        setOwner(_owner);
        setResolved(_resolved);
    }


    @Accessor(qualifier = "docId", type = Accessor.Type.GETTER)
    public String getDocId()
    {
        return (String)getPersistenceContext().getPropertyValue("docId");
    }


    @Accessor(qualifier = "itemPK", type = Accessor.Type.GETTER)
    public PK getItemPK()
    {
        return (PK)getPersistenceContext().getPropertyValue("itemPK");
    }


    @Accessor(qualifier = "itemQualifier", type = Accessor.Type.GETTER)
    public String getItemQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("itemQualifier");
    }


    @Accessor(qualifier = "processCode", type = Accessor.Type.GETTER)
    public String getProcessCode()
    {
        return (String)getPersistenceContext().getPropertyValue("processCode");
    }


    @Accessor(qualifier = "resolved", type = Accessor.Type.GETTER)
    public Boolean getResolved()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("resolved");
    }


    @Accessor(qualifier = "docId", type = Accessor.Type.SETTER)
    public void setDocId(String value)
    {
        getPersistenceContext().setPropertyValue("docId", value);
    }


    @Accessor(qualifier = "itemPK", type = Accessor.Type.SETTER)
    public void setItemPK(PK value)
    {
        getPersistenceContext().setPropertyValue("itemPK", value);
    }


    @Accessor(qualifier = "itemQualifier", type = Accessor.Type.SETTER)
    public void setItemQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("itemQualifier", value);
    }


    @Accessor(qualifier = "processCode", type = Accessor.Type.SETTER)
    public void setProcessCode(String value)
    {
        getPersistenceContext().setPropertyValue("processCode", value);
    }


    @Accessor(qualifier = "resolved", type = Accessor.Type.SETTER)
    public void setResolved(Boolean value)
    {
        getPersistenceContext().setPropertyValue("resolved", value);
    }
}
