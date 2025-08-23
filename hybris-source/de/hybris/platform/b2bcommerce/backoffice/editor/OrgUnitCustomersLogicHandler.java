package de.hybris.platform.b2bcommerce.backoffice.editor;

import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.backoffice.editor.OrgUnitLogicHandler;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.services.OrgUnitMemberParameter;
import de.hybris.platform.commerceservices.organization.utils.OrgUtils;
import de.hybris.platform.commerceservices.util.CommerceSearchUtils;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class OrgUnitCustomersLogicHandler extends OrgUnitLogicHandler
{
    private static final Logger LOG = Logger.getLogger(OrgUnitCustomersLogicHandler.class);


    public void beforeEditorAreaRender(WidgetInstanceManager widgetInstanceManager, Object currentObject)
    {
        super.beforeEditorAreaRender(widgetInstanceManager, currentObject);
        OrgUnitMemberParameter<B2BUnitModel> param = OrgUtils.createOrgUnitMemberParameter(((OrgUnitModel)currentObject)
                        .getUid(), null, B2BUnitModel.class, CommerceSearchUtils.getAllOnOnePagePageableData());
        widgetInstanceManager.getModel().setValue("customersChanged", getOrgUnitService().getMembers(param).getResults());
    }


    protected void handleSaveMembers(WidgetInstanceManager widgetInstanceManager, OrgUnitModel unit) throws ObjectSavingException
    {
        try
        {
            super.handleSaveMembers(widgetInstanceManager, unit);
            OrgUnitMemberParameter<B2BUnitModel> param = OrgUtils.createOrgUnitMemberParameter(unit.getUid(), null, B2BUnitModel.class,
                            CommerceSearchUtils.getAllOnOnePagePageableData());
            List<B2BUnitModel> customersToRemove = getOrgUnitService().getMembers(param).getResults();
            if(CollectionUtils.isNotEmpty(customersToRemove))
            {
                param.setMembers(new HashSet<>(customersToRemove));
                getOrgUnitService().removeMembers(param);
            }
            List<B2BUnitModel> customersChanged = (List<B2BUnitModel>)widgetInstanceManager.getModel().getValue("customersChanged", List.class);
            if(CollectionUtils.isNotEmpty(customersChanged))
            {
                param.setMembers(new HashSet<>(customersChanged));
                getOrgUnitService().addMembers(param);
            }
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
            }
            if(e instanceof ObjectSavingException)
            {
                throw e;
            }
            throw new ObjectSavingException(unit.getUid(), e.getMessage(), e);
        }
    }
}
