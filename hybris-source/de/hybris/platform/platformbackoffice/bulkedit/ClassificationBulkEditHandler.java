package de.hybris.platform.platformbackoffice.bulkedit;

import com.hybris.backoffice.bulkedit.BulkEditForm;
import com.hybris.backoffice.bulkedit.BulkEditHandler;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.bulkedit.dto.ClassificationChangeDto;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationBulkEditHandler extends BulkEditHandler
{
    private ClassificationPersistenceHandler classificationPersistenceHandler;
    private BulkEditClassificationService bulkEditClassificationService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        performForStandardAttributes(customType, adapter, parameters);
        if(!parameters.containsKey("hasValidationErrors"))
        {
            performForClassificationAttributes(adapter, parameters);
        }
    }


    protected void performForStandardAttributes(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        super.perform(customType, adapter, parameters);
    }


    protected void performForClassificationAttributes(FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        BulkEditForm bulkEditForm = getBulkEditForm(adapter.getWidgetInstanceManager().getModel(), parameters);
        if(bulkEditForm instanceof ClassificationBulkEditForm)
        {
            Map<String, Feature> modifiedProductFeatures = (Map<String, Feature>)adapter.getWidgetInstanceManager().getModel().getValue("modifiedProductFeatures.pknull", Map.class);
            if(modifiedProductFeatures != null)
            {
                Collection<ClassificationChangeDto> dtos = getBulkEditClassificationService().generateChanges((ClassificationBulkEditForm)bulkEditForm, modifiedProductFeatures);
                Objects.requireNonNull(ProductModel.class);
                Objects.requireNonNull(ProductModel.class);
                Set<ProductModel> products = (Set<ProductModel>)bulkEditForm.getItemsToEdit().stream().filter(ProductModel.class::isInstance).map(ProductModel.class::cast).collect(Collectors.toSet());
                products.forEach(product -> getClassificationPersistenceHandler().saveChanges(product, dtos));
            }
        }
    }


    public ClassificationPersistenceHandler getClassificationPersistenceHandler()
    {
        return this.classificationPersistenceHandler;
    }


    @Required
    public void setClassificationPersistenceHandler(ClassificationPersistenceHandler classificationPersistenceHandler)
    {
        this.classificationPersistenceHandler = classificationPersistenceHandler;
    }


    public BulkEditClassificationService getBulkEditClassificationService()
    {
        return this.bulkEditClassificationService;
    }


    @Required
    public void setBulkEditClassificationService(BulkEditClassificationService bulkEditClassificationService)
    {
        this.bulkEditClassificationService = bulkEditClassificationService;
    }
}
