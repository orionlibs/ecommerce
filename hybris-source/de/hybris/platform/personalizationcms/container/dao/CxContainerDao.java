package de.hybris.platform.personalizationcms.container.dao;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;

public interface CxContainerDao extends Dao
{
    List<CxCmsComponentContainerModel> getCxContainersByDefaultComponent(SimpleCMSComponentModel paramSimpleCMSComponentModel);
}
