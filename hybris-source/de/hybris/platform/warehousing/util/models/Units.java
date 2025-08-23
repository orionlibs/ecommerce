package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.daos.UnitDao;
import de.hybris.platform.warehousing.util.builder.UnitModelBuilder;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class Units extends AbstractItems<UnitModel>
{
    public static final String UNIT_TYPE_QUANTITY = "quantity";
    public static final String CODE_PIECE = "piece";
    public static final String CODE_UNIT = "unit";
    private UnitDao unitDao;


    public UnitModel Piece()
    {
        return (UnitModel)getFromCollectionOrSaveAndReturn(() -> getUnitDao().findUnitsByCode("piece"), () -> UnitModelBuilder.aModel().withUnitType("quantity").withCode("piece").withName("piece", Locale.ENGLISH).build());
    }


    public UnitModel Unit()
    {
        return (UnitModel)getFromCollectionOrSaveAndReturn(() -> getUnitDao().findUnitsByCode("unit"), () -> UnitModelBuilder.aModel().withUnitType("quantity").withCode("unit").withName("unit", Locale.ENGLISH).build());
    }


    public UnitDao getUnitDao()
    {
        return this.unitDao;
    }


    @Required
    public void setUnitDao(UnitDao unitDao)
    {
        this.unitDao = unitDao;
    }
}
