package de.hybris.platform.platformbackoffice.labelproviders;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import java.text.DateFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class SavedValuesLabelProvider implements LabelProvider<SavedValuesModel>
{
    private LabelService labelService;
    private CockpitLocaleService cockpitLocaleService;


    public String getLabel(SavedValuesModel savedValues)
    {
        Validate.notNull("Saved values may not be null", new Object[] {savedValues});
        String labelBy = Labels.getLabel("labelprovider.savedvalues.by");
        DateFormat format = DateFormat.getDateInstance(2, getCockpitLocaleService().getCurrentLocale());
        String timestampValue = format.format(savedValues.getTimestamp());
        UserModel user = savedValues.getUser();
        String changedByValue = (user != null) ? this.labelService.getObjectLabel(user) : null;
        String changedAttributesValue = savedValues.getChangedAttributes();
        StringBuilder label = new StringBuilder((String)StringUtils.defaultIfBlank(changedAttributesValue, ""));
        label.append(" - ");
        label.append(timestampValue);
        label.append(" ");
        label.append(labelBy);
        label.append(" ");
        label.append(changedByValue);
        return label.toString();
    }


    public String getDescription(SavedValuesModel savedValues)
    {
        return null;
    }


    public String getIconPath(SavedValuesModel savedValues)
    {
        return null;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return this.cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
