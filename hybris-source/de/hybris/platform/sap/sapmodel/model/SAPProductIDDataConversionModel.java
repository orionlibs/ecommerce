package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPProductIDDataConversionModel extends ItemModel
{
    public static final String _TYPECODE = "SAPProductIDDataConversion";
    public static final String CONVERSIONID = "conversionID";
    public static final String MATNRLENGTH = "matnrLength";
    public static final String DISPLAYLEADINGZEROS = "displayLeadingZeros";
    public static final String DISPLAYLEXICOGRAPHIC = "displayLexicographic";
    public static final String MASK = "mask";


    public SAPProductIDDataConversionModel()
    {
    }


    public SAPProductIDDataConversionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPProductIDDataConversionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "conversionID", type = Accessor.Type.GETTER)
    public String getConversionID()
    {
        return (String)getPersistenceContext().getPropertyValue("conversionID");
    }


    @Accessor(qualifier = "displayLeadingZeros", type = Accessor.Type.GETTER)
    public Boolean getDisplayLeadingZeros()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("displayLeadingZeros");
    }


    @Accessor(qualifier = "displayLexicographic", type = Accessor.Type.GETTER)
    public Boolean getDisplayLexicographic()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("displayLexicographic");
    }


    @Accessor(qualifier = "mask", type = Accessor.Type.GETTER)
    public String getMask()
    {
        return (String)getPersistenceContext().getPropertyValue("mask");
    }


    @Accessor(qualifier = "matnrLength", type = Accessor.Type.GETTER)
    public Integer getMatnrLength()
    {
        return (Integer)getPersistenceContext().getPropertyValue("matnrLength");
    }


    @Accessor(qualifier = "conversionID", type = Accessor.Type.SETTER)
    public void setConversionID(String value)
    {
        getPersistenceContext().setPropertyValue("conversionID", value);
    }


    @Accessor(qualifier = "displayLeadingZeros", type = Accessor.Type.SETTER)
    public void setDisplayLeadingZeros(Boolean value)
    {
        getPersistenceContext().setPropertyValue("displayLeadingZeros", value);
    }


    @Accessor(qualifier = "displayLexicographic", type = Accessor.Type.SETTER)
    public void setDisplayLexicographic(Boolean value)
    {
        getPersistenceContext().setPropertyValue("displayLexicographic", value);
    }


    @Accessor(qualifier = "mask", type = Accessor.Type.SETTER)
    public void setMask(String value)
    {
        getPersistenceContext().setPropertyValue("mask", value);
    }


    @Accessor(qualifier = "matnrLength", type = Accessor.Type.SETTER)
    public void setMatnrLength(Integer value)
    {
        getPersistenceContext().setPropertyValue("matnrLength", value);
    }
}
