package de.hybris.platform.platformbackoffice.bulkedit;

import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.platformbackoffice.bulkedit.dto.ClassificationChangeDto;
import de.hybris.platform.platformbackoffice.classification.util.BackofficeClassificationUtils;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBulkEditClassificationService implements BulkEditClassificationService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBulkEditClassificationService.class);
    private BulkEditSelectedAttributesHelper selectedAttributesHelper;
    private ObjectFacade objectFacade;


    public Collection<ClassificationChangeDto> generateChanges(ClassificationBulkEditForm bulkEditForm, Map<String, Feature> modifiedProductFeatures)
    {
        return (Collection<ClassificationChangeDto>)getSelectedAttributesHelper().findLeaves(bulkEditForm.getClassificationAttributesForm().getSelectedAttributes())
                        .stream().map(attr -> generateChange(bulkEditForm, modifiedProductFeatures, attr)).collect(Collectors.toSet());
    }


    protected ClassificationChangeDto generateChange(ClassificationBulkEditForm bulkEditForm, Map<String, Feature> modifiedProductFeatures, Attribute selectedAttribute)
    {
        ClassificationChangeDto dto = new ClassificationChangeDto();
        dto.setIsoCode(selectedAttribute.getIsoCode());
        Optional<ClassAttributeAssignmentModel> assignment = getClassAttributeAssignmentModel(selectedAttribute
                        .getQualifier());
        if(assignment.isPresent())
        {
            ClassAttributeAssignmentModel foundAssignment = assignment.get();
            String qualifier = generateEncodedQualifier(foundAssignment);
            dto.setEncodedQualifier(qualifier);
            dto.setClear(bulkEditForm.getQualifiersToBeCleared().contains(selectedAttribute.getQualifier()));
            dto.setMerge(bulkEditForm.getQualifiersToMerge().contains(selectedAttribute.getQualifier()));
            dto.setFeature(modifiedProductFeatures.get(qualifier));
        }
        return dto;
    }


    protected String generateEncodedQualifier(ClassAttributeAssignmentModel foundAssignment)
    {
        return
                        BackofficeClassificationUtils.getFeatureQualifierEncoded(BackofficeClassificationUtils.getFeatureQualifier(foundAssignment));
    }


    protected Optional<ClassAttributeAssignmentModel> getClassAttributeAssignmentModel(String pk)
    {
        try
        {
            ItemModel item = (ItemModel)getObjectFacade().load(pk);
            return (item instanceof ClassAttributeAssignmentModel) ? Optional.<ClassAttributeAssignmentModel>of((ClassAttributeAssignmentModel)item) :
                            Optional.<ClassAttributeAssignmentModel>empty();
        }
        catch(ObjectNotFoundException e)
        {
            LOG.warn(String.format("Cannot load %s for attribute '%s'", new Object[] {ClassAttributeAssignmentModel.class.getName(), pk}), (Throwable)e);
            return Optional.empty();
        }
    }


    public BulkEditSelectedAttributesHelper getSelectedAttributesHelper()
    {
        return this.selectedAttributesHelper;
    }


    @Required
    public void setSelectedAttributesHelper(BulkEditSelectedAttributesHelper selectedAttributesHelper)
    {
        this.selectedAttributesHelper = selectedAttributesHelper;
    }


    public ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }


    @Required
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }
}
