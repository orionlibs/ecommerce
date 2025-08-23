package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ExternalImportKeyModel extends ItemModel
{
    public static final String _TYPECODE = "ExternalImportKey";
    public static final String SOURCESYSTEMID = "sourceSystemID";
    public static final String SOURCEKEY = "sourceKey";
    public static final String TARGETPK = "targetPK";


    public ExternalImportKeyModel()
    {
    }


    public ExternalImportKeyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExternalImportKeyModel(String _sourceKey, String _sourceSystemID, PK _targetPK)
    {
        setSourceKey(_sourceKey);
        setSourceSystemID(_sourceSystemID);
        setTargetPK(_targetPK);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExternalImportKeyModel(ItemModel _owner, String _sourceKey, String _sourceSystemID, PK _targetPK)
    {
        setOwner(_owner);
        setSourceKey(_sourceKey);
        setSourceSystemID(_sourceSystemID);
        setTargetPK(_targetPK);
    }


    @Accessor(qualifier = "sourceKey", type = Accessor.Type.GETTER)
    public String getSourceKey()
    {
        return (String)getPersistenceContext().getPropertyValue("sourceKey");
    }


    @Accessor(qualifier = "sourceSystemID", type = Accessor.Type.GETTER)
    public String getSourceSystemID()
    {
        return (String)getPersistenceContext().getPropertyValue("sourceSystemID");
    }


    @Accessor(qualifier = "targetPK", type = Accessor.Type.GETTER)
    public PK getTargetPK()
    {
        return (PK)getPersistenceContext().getPropertyValue("targetPK");
    }


    @Accessor(qualifier = "sourceKey", type = Accessor.Type.SETTER)
    public void setSourceKey(String value)
    {
        getPersistenceContext().setPropertyValue("sourceKey", value);
    }


    @Accessor(qualifier = "sourceSystemID", type = Accessor.Type.SETTER)
    public void setSourceSystemID(String value)
    {
        getPersistenceContext().setPropertyValue("sourceSystemID", value);
    }


    @Accessor(qualifier = "targetPK", type = Accessor.Type.SETTER)
    public void setTargetPK(PK value)
    {
        getPersistenceContext().setPropertyValue("targetPK", value);
    }
}
