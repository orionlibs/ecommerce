package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.RelationBetweenComponentsService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultRelationBetweenComponentsService extends AbstractCMSService implements RelationBetweenComponentsService
{
    private TypeService typeService;
    private CMSAdminContentSlotService cmsAdminContentSlotService;


    public void maintainRelationBetweenComponentsOnComponent(AbstractCMSComponentModel parent)
    {
        removeRelationBetweenComponentsByParent(parent);
        ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(parent.getItemtype());
        composedType.getDeclaredattributedescriptors().stream().filter(AttributeDescriptorModel::getWritable)
                        .filter(attribute -> !attribute.getQualifier().equals("containers"))
                        .map(attribute -> getModelService().getAttributeValue(parent, attribute.getQualifier()))
                        .filter(attributeValue -> Objects.nonNull(attributeValue))
                        .forEach(attributeValue -> insertRelationBetweenParentAndChild(parent, attributeValue));
    }


    public void maintainRelationBetweenComponentsOnPage(AbstractPageModel pageModel)
    {
        getCmsAdminContentSlotService().getContentSlotsForPage(pageModel, false)
                        .forEach(contentSlotData -> contentSlotData.getContentSlot().getCmsComponents().forEach(()));
    }


    public void maintainRelationBetweenComponentsOnSlot(ContentSlotModel slotModel)
    {
        slotModel.getCmsComponents()
                        .forEach(cmsComponentModel -> maintainRelationBetweenComponentsOnComponent(cmsComponentModel));
    }


    public void removeRelationBetweenComponentsOnModel(AbstractCMSComponentModel model)
    {
        removeRelationBetweenComponentsByParent(model);
        removeRelationBetweenComponentsByChild(model);
    }


    private void removeRelationBetweenComponentsByParent(AbstractCMSComponentModel parent)
    {
        if(Objects.nonNull(parent.getChildren()))
        {
            List<AbstractCMSComponentModel> children = parent.getChildren();
            children.forEach(child -> {
                if(Objects.nonNull(child.getParents()))
                {
                    List<AbstractCMSComponentModel> childParents = new ArrayList<>(child.getParents());
                    List<AbstractCMSComponentModel> newParents = (List<AbstractCMSComponentModel>)childParents.stream().filter(()).collect(Collectors.toList());
                    child.setParents(newParents);
                }
            });
        }
        parent.setChildren(new ArrayList());
    }


    private void removeRelationBetweenComponentsByChild(AbstractCMSComponentModel child)
    {
        if(Objects.nonNull(child.getParents()))
        {
            List<AbstractCMSComponentModel> parents = child.getParents();
            parents.forEach(parent -> {
                if(Objects.nonNull(parent.getChildren()))
                {
                    List<AbstractCMSComponentModel> parentChildren = new ArrayList<>(parent.getChildren());
                    List<AbstractCMSComponentModel> newChildren = (List<AbstractCMSComponentModel>)parentChildren.stream().filter(()).collect(Collectors.toList());
                    parent.setChildren(newChildren);
                }
            });
        }
        child.setParents(new ArrayList());
    }


    private void insertRelationBetweenParentAndChild(AbstractCMSComponentModel parent, Object childValue)
    {
        if(childValue instanceof AbstractCMSComponentModel)
        {
            addParentToChild(parent, (AbstractCMSComponentModel)childValue);
            addChildToParent(parent, (AbstractCMSComponentModel)childValue);
        }
        else if(childValue instanceof Collection)
        {
            List<?> cmsComponentModels = (List)((Collection)childValue).stream().filter(value -> value instanceof AbstractCMSComponentModel).collect(Collectors.toList());
            for(AbstractCMSComponentModel child : cmsComponentModels)
            {
                addParentToChild(parent, child);
                addChildToParent(parent, child);
            }
        }
    }


    private void addParentToChild(AbstractCMSComponentModel parent, AbstractCMSComponentModel child)
    {
        List<AbstractCMSComponentModel> parents = new ArrayList<>();
        if(Objects.nonNull(child.getParents()))
        {
            parents = new ArrayList<>(child.getParents());
        }
        parents.add(parent);
        child.setParents(parents);
    }


    private void addChildToParent(AbstractCMSComponentModel parent, AbstractCMSComponentModel child)
    {
        List<AbstractCMSComponentModel> children = new ArrayList<>();
        if(Objects.nonNull(parent.getChildren()))
        {
            children = new ArrayList<>(parent.getChildren());
        }
        children.add(child);
        parent.setChildren(children);
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public CMSAdminContentSlotService getCmsAdminContentSlotService()
    {
        return this.cmsAdminContentSlotService;
    }


    public void setCmsAdminContentSlotService(CMSAdminContentSlotService cmsAdminContentSlotService)
    {
        this.cmsAdminContentSlotService = cmsAdminContentSlotService;
    }
}
