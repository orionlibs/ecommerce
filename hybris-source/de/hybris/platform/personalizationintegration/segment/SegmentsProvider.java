package de.hybris.platform.personalizationintegration.segment;

import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import java.util.List;
import java.util.Optional;

public interface SegmentsProvider extends CxProvider
{
    Optional<List<BaseSegmentData>> getSegments();
}
