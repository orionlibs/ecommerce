package de.hybris.platform.cmscockpit.components.editorarea;

import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.CustomEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class PageUrlCustomSection extends DefaultEditorSectionConfiguration implements CustomEditorSectionConfiguration
{
    private static final Logger LOG = Logger.getLogger(PageUrlCustomSection.class);
    private SectionRenderer sectionRenderer;
    private TypeService typeService;
    private AbstractPageModel currentPage;


    public void initialize(EditorConfiguration config, ObjectType type, TypedObject object)
    {
        Object typedObjectTarget = object.getObject();
        if(typedObjectTarget instanceof AbstractPageModel)
        {
            this.currentPage = (AbstractPageModel)typedObjectTarget;
            if(LOG.isDebugEnabled())
            {
                LOG.debug("URL Section initialized for page [" + this.currentPage + "]");
            }
        }
    }


    public void allInitialized(EditorConfiguration config, ObjectType type, TypedObject object)
    {
    }


    public void loadValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    public void saveValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    public List<EditorSectionConfiguration> getAdditionalSections()
    {
        return null;
    }


    public SectionRenderer getCustomRenderer()
    {
        return this.sectionRenderer;
    }


    @Required
    public void setSectionRenderer(SectionRenderer sectionRenderer)
    {
        this.sectionRenderer = sectionRenderer;
    }


    public boolean isVisible()
    {
        boolean ret = false;
        if(this.currentPage != null)
        {
            ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(this.currentPage.getClass());
            ret = !((CMSPageTypeModel)composedType).isPreviewDisabled();
        }
        return ret;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
