package de.hybris.platform.ruleengine.dao;

import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import java.util.List;

public interface DroolsKIEBaseDao
{
    List<DroolsKIEBaseModel> findAllKIEBases();
}
