package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.UpdateAwareCustomSectionConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class CommentsSectionConfiguration extends DefaultEditorSectionConfiguration implements UpdateAwareCustomSectionConfiguration
{
    private SectionRenderer sectionRenderer;
    private Set<PropertyDescriptor> updateTriggerProperties;
    private Set<ObjectType> updateTriggerTypes;


    public void allInitialized(EditorConfiguration config, ObjectType type, TypedObject object)
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


    public void initialize(EditorConfiguration config, ObjectType type, TypedObject object)
    {
    }


    public void loadValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    public void saveValues(EditorConfiguration config, ObjectType type, TypedObject object, ObjectValueContainer objectValues)
    {
    }


    @Required
    public void setSectionRenderer(SectionRenderer sectionRenderer)
    {
        this.sectionRenderer = sectionRenderer;
    }


    public SectionRenderer getSectionRenderer()
    {
        return this.sectionRenderer;
    }


    public Set<PropertyDescriptor> getUpdateTriggerProperties()
    {
        if(this.updateTriggerProperties == null)
        {
            this.updateTriggerProperties = new HashSet<>();
            this.updateTriggerProperties
                            .add(UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("Comment.replies"));
            this.updateTriggerProperties.add(UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("Item.comments"));
            this.updateTriggerProperties.add(UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor("AbstractComment.attachments"));
        }
        return this.updateTriggerProperties;
    }


    public Set<ObjectType> getUpdateTriggerTypes()
    {
        if(this.updateTriggerTypes == null)
        {
            this.updateTriggerTypes = new HashSet<>();
            this.updateTriggerTypes.add(UISessionUtils.getCurrentSession().getTypeService().getObjectType(GeneratedCommentsConstants.TC.COMMENT));
            this.updateTriggerTypes.add(UISessionUtils.getCurrentSession().getTypeService().getObjectType(GeneratedCommentsConstants.TC.REPLY));
            this.updateTriggerTypes.add(UISessionUtils.getCurrentSession().getTypeService().getObjectType(GeneratedCommentsConstants.TC.COMMENTATTACHMENT));
        }
        return this.updateTriggerTypes;
    }
}
