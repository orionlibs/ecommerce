package de.hybris.platform.warehousingbackoffice.actions.taskassignment;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class WarehousingTaskAssignmentWorkflowActionController extends AbstractInitAdvancedSearchAdapter
{
    private static final String BACKOFFICE_WFL_INBOX_ID = "warehousing.treenode.taskassignment.inbox";
    private static final String WORKFLOW_TYPE_CODE = "WorkflowAction";
    private static final String SOCKET_CONTEXT = "context";
    private static final String WORKFLOW_ACTION_PRINCIPAL_ASSIGNED_ATTR = "principalAssigned";
    private static final String WORKFLOW_ACTION_STATUS_ATTR = "status";
    @Resource
    private transient CockpitUserService cockpitUserService;
    @Resource
    private transient UserService userService;


    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> navigationNode)
    {
        FieldType fieldType = new FieldType();
        fieldType.setDisabled(Boolean.valueOf(true));
        fieldType.setSelected(Boolean.valueOf(true));
        fieldType.setName("principalAssigned");
        UserModel currentUser = getCurrentUser();
        List<SearchConditionData> conditions = searchData.getConditions("principalAssigned");
        if(CollectionUtils.isNotEmpty(conditions))
        {
            conditions.clear();
        }
        searchData.addCondition(fieldType, ValueComparisonOperator.EQUALS, currentUser);
        conditions = searchData.getConditions("status");
        if(CollectionUtils.isNotEmpty(conditions))
        {
            conditions.clear();
        }
        FieldType statusType = new FieldType();
        statusType.setDisabled(Boolean.valueOf(true));
        statusType.setSelected(Boolean.valueOf(true));
        statusType.setName("status");
        searchData.addCondition(statusType, ValueComparisonOperator.EQUALS, WorkflowActionStatus.IN_PROGRESS);
    }


    private UserModel getCurrentUser()
    {
        UserModel userModel = null;
        String userId = getCockpitUserService().getCurrentUser();
        if(StringUtils.isNotBlank(userId))
        {
            userModel = getUserService().getUserForUID(userId);
        }
        return userModel;
    }


    public String getTypeCode()
    {
        return "WorkflowAction";
    }


    public String getNavigationNodeId()
    {
        return "warehousing.treenode.taskassignment.inbox";
    }


    protected String getOutputSocketName()
    {
        return "context";
    }


    protected CockpitUserService getCockpitUserService()
    {
        return this.cockpitUserService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }
}
