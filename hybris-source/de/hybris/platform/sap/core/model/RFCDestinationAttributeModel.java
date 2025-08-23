package de.hybris.platform.sap.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.sap.core.configuration.model.SAPRFCDestinationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RFCDestinationAttributeModel extends ItemModel
{
    public static final String _TYPECODE = "RFCDestinationAttribute";
    public static final String _JCOATTRFORJCODESTINATION = "JCoAttrForJCODestination";
    public static final String JCOATTR_NAME = "jcoattr_name";
    public static final String JCOATTR_VALUE = "jcoattr_value";
    public static final String SAPRFCDESTINATION = "SAPRFCDestination";


    public RFCDestinationAttributeModel()
    {
    }


    public RFCDestinationAttributeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RFCDestinationAttributeModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "jcoattr_name", type = Accessor.Type.GETTER)
    public String getJcoattr_name()
    {
        return (String)getPersistenceContext().getPropertyValue("jcoattr_name");
    }


    @Accessor(qualifier = "jcoattr_value", type = Accessor.Type.GETTER)
    public String getJcoattr_value()
    {
        return (String)getPersistenceContext().getPropertyValue("jcoattr_value");
    }


    @Accessor(qualifier = "SAPRFCDestination", type = Accessor.Type.GETTER)
    public SAPRFCDestinationModel getSAPRFCDestination()
    {
        return (SAPRFCDestinationModel)getPersistenceContext().getPropertyValue("SAPRFCDestination");
    }


    @Accessor(qualifier = "jcoattr_name", type = Accessor.Type.SETTER)
    public void setJcoattr_name(String value)
    {
        getPersistenceContext().setPropertyValue("jcoattr_name", value);
    }


    @Accessor(qualifier = "jcoattr_value", type = Accessor.Type.SETTER)
    public void setJcoattr_value(String value)
    {
        getPersistenceContext().setPropertyValue("jcoattr_value", value);
    }


    @Accessor(qualifier = "SAPRFCDestination", type = Accessor.Type.SETTER)
    public void setSAPRFCDestination(SAPRFCDestinationModel value)
    {
        getPersistenceContext().setPropertyValue("SAPRFCDestination", value);
    }
}
