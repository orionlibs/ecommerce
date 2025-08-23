package de.hybris.platform.core.systemsetup.datacreator.impl;

import de.hybris.platform.core.systemsetup.datacreator.internal.CoreDataCreator;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import java.util.Collection;

public class UnitsDataCreator implements CoreDataCreator
{
    public static final String PIECES_CODE = "pieces";


    public void populateDatabase()
    {
        createOrGetUnit("pieces", "pieces");
    }


    public Unit createOrGetUnit(String unitType, String unitCode)
    {
        Unit unit;
        Collection<Unit> coll = getProductManager().getUnitsByCode(unitCode);
        if(coll.size() == 1)
        {
            unit = coll.iterator().next();
        }
        else
        {
            unit = getProductManager().createUnit(unitType, unitCode);
        }
        unit.setConversion(1.0D);
        return unit;
    }


    private ProductManager getProductManager()
    {
        return ProductManager.getInstance();
    }
}
