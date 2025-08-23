package de.hybris.platform.personalizationcms.service;

import de.hybris.platform.acceleratorcms.services.impl.DefaultCMSDynamicAttributeService;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplier;
import de.hybris.platform.personalizationcms.strategy.CmsCxAware;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CxCMSDynamicAttributeService extends DefaultCMSDynamicAttributeService implements CMSItemPropertiesSupplier
{
    protected static final String GROUP_NAME = "smartedit";
    protected static final AttributeNames JSP_NAMES = AttributeNames.getJSP();
    protected static final AttributeNames SPA_NAMES = AttributeNames.getSPA();
    private SessionService sessionService;


    public String groupName()
    {
        return "smartedit";
    }


    public Predicate<CMSItemModel> getConstrainedBy()
    {
        return item -> item instanceof CmsCxAware;
    }


    public Map<String, Object> getProperties(CMSItemModel itemModel)
    {
        Map<String, Object> result = new HashMap<>();
        populatePropertiesMap((Map)result, itemModel, SPA_NAMES);
        return result;
    }


    public Map<String, String> getDynamicComponentAttributes(AbstractCMSComponentModel component, ContentSlotModel contentSlot)
    {
        Map<String, String> result = new HashMap<>();
        if(isEnabled((CMSItemModel)component))
        {
            populatePropertiesMap(result, (CMSItemModel)component, JSP_NAMES);
        }
        return result;
    }


    protected void populatePropertiesMap(Map<String, ? super String> result, CMSItemModel itemModel, AttributeNames names)
    {
        if(itemModel instanceof CmsCxAware)
        {
            CmsCxAware cxAwareComponent = (CmsCxAware)itemModel;
            Objects.requireNonNull(cxAwareComponent);
            putIfNotEmpty(result, names.containerId, cxAwareComponent::getCxContainerUid);
            Objects.requireNonNull(cxAwareComponent);
            putIfNotEmpty(result, names.containerType, cxAwareComponent::getCxContainerType);
            Objects.requireNonNull(cxAwareComponent);
            putIfNotEmpty(result, names.sourceId, cxAwareComponent::getContainerSourceId);
            Objects.requireNonNull(cxAwareComponent);
            putIfNotEmpty(result, names.actionId, cxAwareComponent::getCxActionCode);
            Objects.requireNonNull(cxAwareComponent);
            putIfNotEmpty(result, names.variationId, cxAwareComponent::getCxVariationCode);
            Objects.requireNonNull(cxAwareComponent);
            putIfNotEmpty(result, names.customizationId, cxAwareComponent::getCxCustomizationCode);
        }
    }


    private static void putIfNotEmpty(Map<String, ? super String> result, String key, Supplier<String> supplier)
    {
        String value = supplier.get();
        if(value != null)
        {
            result.put(key, value);
        }
    }


    public boolean isEnabled(CMSItemModel itemModel)
    {
        return (getSessionService().getAttribute("cmsTicketId") != null);
    }


    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }
}
