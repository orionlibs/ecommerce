package de.hybris.platform.cms2.version.service;

import de.hybris.platform.cms2.model.CMSVersionModel;
import java.util.List;

public interface CMSVersionGCService
{
    List<CMSVersionModel> getRetainableVersions(int paramInt1, int paramInt2);
}
