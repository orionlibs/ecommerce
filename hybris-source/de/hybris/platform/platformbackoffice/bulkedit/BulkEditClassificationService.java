package de.hybris.platform.platformbackoffice.bulkedit;

import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.platformbackoffice.bulkedit.dto.ClassificationChangeDto;
import java.util.Collection;
import java.util.Map;

public interface BulkEditClassificationService
{
    Collection<ClassificationChangeDto> generateChanges(ClassificationBulkEditForm paramClassificationBulkEditForm, Map<String, Feature> paramMap);
}
