package de.hybris.platform.sap.core.configuration.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class SAPAdministrationModel extends ItemModel
{
    public static final String _TYPECODE = "SAPAdministration";
    public static final String CORE_LASTDATAHUBINITIALLOAD = "core_lastDataHubInitialLoad";


    public SAPAdministrationModel()
    {
    }


    public SAPAdministrationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPAdministrationModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "core_lastDataHubInitialLoad", type = Accessor.Type.GETTER)
    public Date getCore_lastDataHubInitialLoad()
    {
        return (Date)getPersistenceContext().getPropertyValue("core_lastDataHubInitialLoad");
    }


    @Accessor(qualifier = "core_lastDataHubInitialLoad", type = Accessor.Type.SETTER)
    public void setCore_lastDataHubInitialLoad(Date value)
    {
        getPersistenceContext().setPropertyValue("core_lastDataHubInitialLoad", value);
    }
}
