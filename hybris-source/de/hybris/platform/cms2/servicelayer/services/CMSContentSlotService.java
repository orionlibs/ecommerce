package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface CMSContentSlotService
{
    ContentSlotModel getContentSlotForId(String paramString) throws AmbiguousIdentifierException, UnknownIdentifierException;


    List<SimpleCMSComponentModel> getSimpleCMSComponents(ContentSlotModel paramContentSlotModel, boolean paramBoolean, HttpServletRequest paramHttpServletRequest);


    String getAvailableContentSlotsNames(AbstractPageModel paramAbstractPageModel);


    String getMissingContentSlotsNames(AbstractPageModel paramAbstractPageModel);


    List<String> getDefinedContentSlotPositions(PageTemplateModel paramPageTemplateModel);


    List<String> getDefinedContentSlotPositions(AbstractPageModel paramAbstractPageModel);


    Collection<AbstractPageModel> getPagesForContentSlot(ContentSlotModel paramContentSlotModel);


    default boolean isSharedSlot(ContentSlotModel contentSlot)
    {
        return false;
    }
}
