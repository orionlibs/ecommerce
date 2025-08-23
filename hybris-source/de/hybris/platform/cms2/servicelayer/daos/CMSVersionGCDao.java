package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.cms2.model.CMSVersionModel;
import java.util.Date;
import java.util.List;

public interface CMSVersionGCDao
{
    List<CMSVersionModel> findRetainableVersions(Date paramDate);
}
