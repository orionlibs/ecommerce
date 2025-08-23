package de.hybris.platform.personalizationcms.rendering.populators;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.registry.CMSComponentContainerRegistry;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.cmsfacades.rendering.populators.ContentSlotModelToDataRenderingPopulator;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;

public class CxContentSlotModelToDataRenderingPopulator extends ContentSlotModelToDataRenderingPopulator
{
    protected static final String CONTAINER_PROPERTY = "containerMetadata";
    private CMSComponentContainerRegistry cmsComponentContainerRegistry;


    protected void populateComponents(ContentSlotModel slotModel, PageContentSlotData targetData)
    {
        Objects.requireNonNull(getRenderingVisibilityService());
        List<AbstractCMSComponentData> componentsData = (List<AbstractCMSComponentData>)slotModel.getCmsComponents().stream().filter(getRenderingVisibilityService()::isVisible)
                        .flatMap(component -> (component instanceof CxCmsComponentContainerModel) ? flattenContainers((CxCmsComponentContainerModel)component) : mapComponent(component)).collect(Collectors.toList());
        targetData.setComponents(componentsData);
    }


    protected Stream<AbstractCMSComponentData> flattenContainers(CxCmsComponentContainerModel container)
    {
        List<AbstractCMSComponentModel> components = this.cmsComponentContainerRegistry.getStrategy((AbstractCMSComponentContainerModel)container).getDisplayComponentsForContainer((AbstractCMSComponentContainerModel)container);
        if(CollectionUtils.isNotEmpty(components))
        {
            AbstractCMSComponentData containerData = convertComponentModelToData((AbstractCMSComponentModel)container);
            containerData.getOtherProperties().remove("defaultCmsComponent");
            containerData.getOtherProperties().remove("components");
            return components.stream().map(c -> convertComponentInContainerToData(c, containerData));
        }
        return Stream.empty();
    }


    protected Stream<AbstractCMSComponentData> mapComponent(AbstractCMSComponentModel component)
    {
        return Stream.<AbstractCMSComponentModel>of(component).map(x$0 -> rec$.convertComponentModelToData(x$0));
    }


    protected AbstractCMSComponentData convertComponentInContainerToData(AbstractCMSComponentModel component, AbstractCMSComponentData containerData)
    {
        AbstractCMSComponentData componentData = convertComponentModelToData(component);
        componentData.getOtherProperties().put("containerMetadata", containerData);
        return componentData;
    }


    public void setCmsComponentContainerRegistry(CMSComponentContainerRegistry cmsComponentContainerRegistry)
    {
        this.cmsComponentContainerRegistry = cmsComponentContainerRegistry;
    }


    public CMSComponentContainerRegistry getCmsComponentContainerRegistry()
    {
        return this.cmsComponentContainerRegistry;
    }
}
