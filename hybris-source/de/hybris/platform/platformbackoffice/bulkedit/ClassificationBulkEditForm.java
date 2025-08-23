package de.hybris.platform.platformbackoffice.bulkedit;

import com.hybris.backoffice.attributechooser.AttributeChooserForm;
import com.hybris.backoffice.bulkedit.BulkEditForm;

public class ClassificationBulkEditForm extends BulkEditForm
{
    private AttributeChooserForm classificationAttributesForm = new AttributeChooserForm();


    public AttributeChooserForm getClassificationAttributesForm()
    {
        return this.classificationAttributesForm;
    }


    public void setClassificationAttributesForm(AttributeChooserForm classificationAttributesForm)
    {
        this.classificationAttributesForm = classificationAttributesForm;
    }


    public boolean hasSelectedClassificationAttributes()
    {
        return (this.classificationAttributesForm == null || this.classificationAttributesForm.hasSelectedAttributes());
    }
}
