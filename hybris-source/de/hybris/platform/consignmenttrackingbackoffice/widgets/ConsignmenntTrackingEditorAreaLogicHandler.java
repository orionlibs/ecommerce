package de.hybris.platform.consignmenttrackingbackoffice.widgets;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.impl.DefaultValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaLogicHandler;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;

public class ConsignmenntTrackingEditorAreaLogicHandler extends DefaultEditorAreaLogicHandler
{
    public static final String MSGKEY = "type.validation.consignment.tracking.text";


    public List<ValidationInfo> performValidation(WidgetInstanceManager widgetInstanceManager, Object currentObject, ValidationContext validationContext)
    {
        List<ValidationInfo> validationInfos = new ArrayList<>(super.performValidation(widgetInstanceManager, currentObject, validationContext));
        if(currentObject instanceof ConsignmentModel)
        {
            ConsignmentModel consignment = (ConsignmentModel)currentObject;
            if(!isValidConsignment(consignment))
            {
                validationInfos.add(createValidationInfo());
            }
        }
        return validationInfos;
    }


    protected boolean isValidConsignment(ConsignmentModel consignment)
    {
        return ((Objects.isNull(consignment.getCarrierDetails()) && StringUtils.isEmpty(consignment.getTrackingID())) || (
                        !Objects.isNull(consignment.getCarrierDetails()) && StringUtils.isNotEmpty(consignment.getTrackingID())));
    }


    protected ValidationInfo createValidationInfo()
    {
        DefaultValidationInfo validationInfo = new DefaultValidationInfo();
        String msg = Localization.getLocalizedString("type.validation.consignment.tracking.text");
        validationInfo.setValidationMessage(msg);
        validationInfo.setConfirmed(false);
        validationInfo.setValidationSeverity(ValidationSeverity.ERROR);
        return (ValidationInfo)validationInfo;
    }
}
