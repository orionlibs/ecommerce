package de.hybris.platform.platformbackoffice.bulkedit.renderer;

import com.hybris.cockpitng.util.YTestTools;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.platformbackoffice.classification.provider.ClassificationSectionNameProvider;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;

public class DefaultClassificationSectionBulkEditRenderer implements ClassificationSectionBulkEditRenderer
{
    private static final String SCLASS_EXPAND_BUTTON = "yw-expandCollapse";
    private static final String SCLASS_GRPBOX_CAPTION = "yw-bulkedit-groupbox-caption";
    private static final String SCLASS_GRPBOX = "yw-bulkedit-groupbox";
    private ClassificationSectionNameProvider classificationSectionNameProvider;


    public Component render(Component parent, ClassificationClassModel classificationClassModel)
    {
        String sectionName = getClassificationSectionNameProvider().provide(classificationClassModel);
        Groupbox sectionGroupBox = new Groupbox();
        sectionGroupBox.setParent(parent);
        Button expandButton = new Button();
        Caption caption = new Caption(sectionName);
        expandButton.setSclass("yw-expandCollapse");
        expandButton.addEventListener("onClick", e -> sectionGroupBox.setOpen(!sectionGroupBox.isOpen()));
        YTestTools.modifyYTestId((Component)caption, sectionName + "_caption");
        caption.appendChild((Component)expandButton);
        caption.setSclass("yw-bulkedit-groupbox-caption");
        sectionGroupBox.appendChild((Component)caption);
        sectionGroupBox.setSclass("yw-bulkedit-groupbox");
        YTestTools.modifyYTestId((Component)sectionGroupBox, sectionName);
        return (Component)sectionGroupBox;
    }


    public ClassificationSectionNameProvider getClassificationSectionNameProvider()
    {
        return this.classificationSectionNameProvider;
    }


    @Required
    public void setClassificationSectionNameProvider(ClassificationSectionNameProvider classificationSectionNameProvider)
    {
        this.classificationSectionNameProvider = classificationSectionNameProvider;
    }
}
