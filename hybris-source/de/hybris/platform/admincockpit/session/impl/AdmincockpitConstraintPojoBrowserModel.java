package de.hybris.platform.admincockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.CompareMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.ListMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractPageableBrowserModel;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import de.hybris.platform.validation.services.ConstraintService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;

public class AdmincockpitConstraintPojoBrowserModel extends AbstractPageableBrowserModel
{
    private ConstraintService constraintService;
    private List<MainAreaComponentFactory> viewModes = null;
    private List<TypedObject> matchedElements = null;


    public AdmincockpitConstraintPojoBrowserModel()
    {
        if(this.viewMode == null)
        {
            this.viewMode = "LIST";
        }
        setLabel(Labels.getLabel("ba.constraint_pojo_browsermodel_label"));
    }


    protected ConstraintService getConstraintServiceInternal()
    {
        if(this.constraintService == null)
        {
            this.constraintService = (ConstraintService)SpringUtil.getApplicationContext().getBean("constraintService", ConstraintService.class);
        }
        return this.constraintService;
    }


    public void updateItems(int page)
    {
        clearSelection();
        setCurrentPage(page);
        List<TypedObject> allElements = getAllItems();
        int totalCount = allElements.size();
        int fromIndex = Math.max(0, getOffset());
        int toIndex = Math.min(getPageSize() + fromIndex, totalCount);
        setMatchedElements(new ArrayList<>(allElements.subList(fromIndex, toIndex)));
        getItems();
        setTotalCount(totalCount);
        fireChanged();
    }


    public Object clone() throws CloneNotSupportedException
    {
        AdmincockpitConstraintPojoBrowserModel constraintPojoBrowserModel = new AdmincockpitConstraintPojoBrowserModel();
        constraintPojoBrowserModel.setOffset(getOffset());
        constraintPojoBrowserModel.setPageSize(getPageSize());
        constraintPojoBrowserModel.setTotalCount(getTotalCount());
        constraintPojoBrowserModel.setBrowserFilterFixed(getBrowserFilterFixed());
        constraintPojoBrowserModel.setBrowserFilter(getBrowserFilter());
        constraintPojoBrowserModel.setLabel(getLabel());
        constraintPojoBrowserModel.setViewMode(getViewMode());
        constraintPojoBrowserModel.setMatchedElements(getMatchedElements());
        constraintPojoBrowserModel.setSimplePaging(isSimplePaging());
        return constraintPojoBrowserModel;
    }


    public TypedObject getItem(int index)
    {
        return getItems().get(index);
    }


    public List<TypedObject> getItems()
    {
        if(getMatchedElements() == null)
        {
            setMatchedElements(getAllItems());
        }
        return getMatchedElements();
    }


    private List<TypedObject> getAllItems()
    {
        List<AbstractConstraintModel> abstractConstraintModelList = getConstraintServiceInternal().getPojoRelatedConstraints();
        List<TypedObject> listTypeObject = new ArrayList<>();
        for(AbstractConstraintModel abstractConstraintModel : abstractConstraintModelList)
        {
            TypedObject wrappedComposedTypeModel = UISessionUtils.getCurrentSession().getTypeService().wrapItem(abstractConstraintModel.getPk());
            listTypeObject.add(wrappedComposedTypeModel);
        }
        return listTypeObject;
    }


    public void blacklistItems(Collection<Integer> indexes)
    {
    }


    public void removeItems(Collection<Integer> indexes)
    {
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new Object(this);
    }


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList<>();
            this.viewModes.add(new ListMainAreaComponentFactory());
            this.viewModes.add(new CompareMainAreaComponentFactory());
        }
        return this.viewModes;
    }


    public void setMatchedElements(List<TypedObject> matchedElements)
    {
        this.matchedElements = matchedElements;
    }


    public List<TypedObject> getMatchedElements()
    {
        return this.matchedElements;
    }
}
