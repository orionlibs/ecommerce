package de.hybris.platform.workflow.daos.impl;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.FlexibleSearchUtils;
import de.hybris.platform.workflow.daos.WorkflowTemplateDao;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultWorkflowTemplateDao extends DefaultGenericDao<WorkflowTemplateModel> implements WorkflowTemplateDao
{
    private UserService userService;
    private String adHocTemplateName;


    public DefaultWorkflowTemplateDao(String typecode)
    {
        super(typecode);
    }


    public List<WorkflowTemplateModel> findAdhocWorkflowTemplates()
    {
        return find(Collections.singletonMap("code", getAdhocTemplateName()));
    }


    public EmployeeModel findAdhocWorkflowTemplateDummyOwner()
    {
        return this.userService.getAdminUser();
    }


    public List<WorkflowTemplateModel> findWorkflowTemplatesByUser(UserModel user)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("user", user);
        String query = "SELECT {pk} FROM {WorkflowTemplate} WHERE {owner} = ?user";
        SearchResult<WorkflowTemplateModel> res = getFlexibleSearchService().search("SELECT {pk} FROM {WorkflowTemplate} WHERE {owner} = ?user", params);
        return res.getResult();
    }


    public List<WorkflowTemplateModel> findWorkflowTemplatesVisibleForPrincipal(PrincipalModel principal)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {t:").append("pk").append("} ");
        query.append("FROM {").append("WorkflowTemplate").append(" AS t ");
        query.append("JOIN ").append("WorkflowTemplate2PrincipalRelation").append(" AS r ");
        query.append("ON {r:").append("source").append("}={t:").append("pk").append("} } ");
        Collection<PrincipalModel> principals = new ArrayList<>();
        principals.addAll(principal.getAllGroups());
        principals.add(principal);
        Map<Object, Object> params = new HashMap<>();
        params.put("principals", principals);
        String inPart = FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{r:target} IN (?principals)", "principals", "AND", principals, params);
        query.append("WHERE ").append(inPart);
        SearchResult<WorkflowTemplateModel> res = getFlexibleSearchService().search(query.toString(), params);
        return res.getResult();
    }


    public List<WorkflowTemplateModel> findAllWorkflowTemplates()
    {
        return find();
    }


    public List<WorkflowActionTemplateModel> findWorkflowActionTemplatesByCode(String code)
    {
        String query = "SELECT {pk} FROM {WorkflowActionTemplate} WHERE {code} = ?code";
        SearchResult<WorkflowActionTemplateModel> res = getFlexibleSearchService().search("SELECT {pk} FROM {WorkflowActionTemplate} WHERE {code} = ?code",
                        Collections.singletonMap("code", code));
        return res.getResult();
    }


    public List<WorkflowTemplateModel> findWorkflowTemplatesByCode(String code)
    {
        return find(Collections.singletonMap("code", code));
    }


    private String getAdhocTemplateName()
    {
        return (this.adHocTemplateName == null) ? Config.getParameter("workflow.adhoctemplate.name") : this.adHocTemplateName;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setAdHocTemplateName(String adHocTemplateName)
    {
        this.adHocTemplateName = adHocTemplateName;
    }


    public Collection<LinkModel> findWorkflowLinkTemplates(AbstractWorkflowDecisionModel decision, AbstractWorkflowActionModel action)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("act", action);
        params.put("desc", decision);
        SearchResult<LinkModel> res = getFlexibleSearchService().search("SELECT {pk} from {WorkflowActionTemplateLinkTemplateRelation} where {source}=?desc AND {target}=?act", params);
        return res.getResult();
    }


    public Collection<LinkModel> findWorkflowLinkTemplatesByAction(AbstractWorkflowActionModel action)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("act", action);
        SearchResult<LinkModel> res = getFlexibleSearchService().search("SELECT {pk} from {WorkflowActionTemplateLinkTemplateRelation} where {target}=?act", params);
        return res.getResult();
    }


    public Collection<LinkModel> findWorkflowLinkTemplatesByDecision(AbstractWorkflowDecisionModel decision)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("desc", decision);
        SearchResult<LinkModel> res = getFlexibleSearchService().search("SELECT {pk} from {WorkflowActionTemplateLinkTemplateRelation} where {source}=?desc", params);
        return res.getResult();
    }
}
