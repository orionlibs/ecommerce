package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.deliveryzone.model.ZoneModel;

public class AtpFormulaDaoImpl extends AbstractWarehousingDao<ZoneModel>
{
    protected String getQuery()
    {
        return "SELECT {pk} FROM {AtpFormula} WHERE {code}=?code";
    }
}
