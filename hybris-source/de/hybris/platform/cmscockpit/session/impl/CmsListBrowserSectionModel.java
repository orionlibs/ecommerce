package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.Lockable;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultListBrowserSectionModel;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.spring.SpringUtil;

public class CmsListBrowserSectionModel extends DefaultListBrowserSectionModel implements Lockable
{
    private boolean lockable = false;
    private boolean locked = false;
    protected String position;
    protected List<ObjectType> creatableTypes = new ArrayList<>();
    public static final String RELATED_SECTION_BROWSER = "relatedSectionBrowserModel";


    public String getPosition()
    {
        return this.position;
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public CmsListBrowserSectionModel(SectionBrowserModel browserModel)
    {
        super(browserModel);
    }


    public CmsListBrowserSectionModel(SectionBrowserModel browserModel, String label, Object rootItem, String position)
    {
        super(browserModel, label, rootItem);
        this.position = position;
    }


    public CmsListBrowserSectionModel(SectionBrowserModel browserModel, String label)
    {
        super(browserModel, label);
    }


    public List<ObjectType> getCreatableTypes()
    {
        return this.creatableTypes;
    }


    public void setCreatableTypes(List<ObjectType> creatableTypes)
    {
        this.creatableTypes = creatableTypes;
    }


    public boolean isLockable()
    {
        return this.lockable;
    }


    public void setLockable(boolean lockable)
    {
        this.lockable = lockable;
    }


    public boolean isLocked()
    {
        boolean result = this.locked;
        if(!result)
        {
            if(getRootItem() instanceof TypedObject &&
                            UISessionUtils.getCurrentSession().getTypeService().getBaseType(GeneratedCms2Constants.TC.CONTENTSLOT)
                                            .isAssignableFrom((ObjectType)((TypedObject)getRootItem()).getType()))
            {
                result = getCmsPageLockingService().isContentSlotLockedForUser((ContentSlotModel)((TypedObject)
                                                getRootItem()).getObject(),
                                UISessionUtils.getCurrentSession().getSystemService().getCurrentUser());
            }
        }
        return result;
    }


    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }


    protected CMSPageLockingService getCmsPageLockingService()
    {
        return (CMSPageLockingService)SpringUtil.getBean("cmsPageLockingService");
    }


    protected CMSPageService getCmsPageService()
    {
        return (CMSPageService)SpringUtil.getBean("cmsPageService");
    }
}
