package de.hybris.platform.cmscockpit.services;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;
import java.util.List;

public interface CmsCockpitService
{
    public static final String CMSITEM_UID = "CMSItem.uid";
    public static final String ABSTRACTPAGE_UID_PREFIX = "page";


    List<String> getAllApprovalStatusCodes();


    String getApprovalStatusName(String paramString);


    void setApprovalStatus(TypedObject paramTypedObject, String paramString);


    String getApprovalStatusCode(TypedObject paramTypedObject);


    boolean isPartOfTemplate(TypedObject paramTypedObject);


    boolean isRestricted(TypedObject paramTypedObject);


    TypedObject clonePageFirstLevel(TypedObject paramTypedObject, String paramString);


    String createRestrictionTooltip(TypedObject paramTypedObject);


    Collection<CMSSiteModel> getWebsites();


    List<AbstractPageModel> getRecentlyEditedPages(int paramInt);


    List<TypedObject> getPersonalizedPages(TypedObject paramTypedObject);


    List<TypedObject> getPersonalizedPages(TypedObject paramTypedObject1, TypedObject paramTypedObject2);


    TypedObject getDefaultPage(TypedObject paramTypedObject);


    Collection<CMSSiteModel> getSites();
}
