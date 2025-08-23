package de.hybris.platform.personalizationintegration.segment;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import java.util.List;

public interface UserSegmentsProvider extends CxProvider
{
    List<SegmentMappingData> getUserSegments(UserModel paramUserModel);
}
