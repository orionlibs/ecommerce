package de.hybris.platform.comments.services.impl;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.CommentUserSettingModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.comments.services.CommentDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCommentDao implements CommentDao
{
    private FlexibleSearchService flexibleSearchService;


    @Deprecated(since = "ages", forRemoval = true)
    public SearchResult<CommentModel> findAll(UserModel user, Collection<ComponentModel> components, int offset, int count)
    {
        return findAllInternal(user, components, null, null, offset, count);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public SearchResult<CommentModel> findAllByItem(UserModel user, Collection<ComponentModel> components, ItemModel item, int offset, int count)
    {
        return findAllInternal(user, components, null, item, offset, count);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public SearchResult<CommentModel> findAllByItemAndType(UserModel user, Collection<ComponentModel> components, ItemModel item, Collection<CommentTypeModel> types, int offset, int count)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("Item must be specified.");
        }
        if(types == null || types.isEmpty())
        {
            throw new IllegalArgumentException("At least one comment type must be specified");
        }
        return findAllInternal(user, components, types, item, offset, count);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public SearchResult<CommentModel> findAllByType(UserModel user, Collection<ComponentModel> components, Collection<CommentTypeModel> types, int offset, int count)
    {
        if(types == null || types.isEmpty())
        {
            throw new IllegalArgumentException("At least one comment type must be specified.");
        }
        return findAllInternal(user, components, types, null, offset, count);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public SearchResult<ReplyModel> findAllDirectReplies(CommentModel comment, int offset, int count)
    {
        if(comment == null)
        {
            throw new IllegalArgumentException("Comment must be specified.");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("comment", comment);
        StringBuilder query = new StringBuilder();
        query.append("SELECT {r:").append("pk").append("} ");
        query.append("FROM {").append("Reply").append(" AS r } ");
        query.append("WHERE ");
        query.append("{r:").append("comment").append("} = ?comment ");
        query.append("AND ");
        query.append("{r:").append("parent").append("} IS NULL");
        return searchInternal(query.toString(), params, offset, count);
    }


    protected SearchResult<CommentModel> findAllInternal(UserModel user, Collection<ComponentModel> components, Collection<CommentTypeModel> types, ItemModel item, int offset, int count)
    {
        if(components == null || components.isEmpty())
        {
            throw new IllegalArgumentException("At least one domain component must be specified.");
        }
        boolean typeSpecified = (types != null && !types.isEmpty());
        Map<String, Object> params = new HashMap<>();
        if(user != null)
        {
            params.put("user", user);
        }
        params.put("components", components);
        StringBuilder query = new StringBuilder();
        query.append("SELECT {c:").append("pk").append("} ");
        query.append("FROM {").append("Comment").append(" AS c ");
        if(item != null)
        {
            params.put("item", item);
            query.append("JOIN ").append(GeneratedCommentsConstants.Relations.COMMENTITEMRELATION).append(" AS cItemRel ");
            query.append("ON {cItemRel:").append("source").append("} = {c:").append("pk").append("} ");
        }
        query.append(" } ");
        query.append("WHERE ");
        query.append("{c:").append("component").append("} IN (?components) ");
        if(user != null)
        {
            query.append("AND (");
            query.append("{c:").append("author").append("} = ?user ");
            query.append(") ");
        }
        if(typeSpecified)
        {
            query.append("AND {c:").append("commentType").append("} IN (?types) ");
            params.put("types", types);
        }
        if(item != null)
        {
            query.append("AND {cItemRel:").append("target").append("} = ?item ");
        }
        query.append(" ORDER BY {c:").append("modifiedtime").append("} DESC");
        return searchInternal(query.toString(), params, offset, count);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public SearchResult<CommentUserSettingModel> findUserSettingByComment(UserModel user, AbstractCommentModel commentItem)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("CommentUserSetting").append("} ");
        query.append("WHERE {").append("user").append("} = " + user.getPk() + " ");
        query.append("AND {").append("comment").append("} = " + commentItem.getPk());
        return searchInternal(query.toString(), Collections.EMPTY_MAP, 0, -1);
    }


    protected <T extends de.hybris.platform.servicelayer.model.AbstractItemModel> SearchResult<T> searchInternal(String query, Map<String, Object> params, int offset, int count)
    {
        FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(query, params);
        flexQuery.setStart(offset);
        flexQuery.setCount(count);
        flexQuery.setNeedTotal((count > 0));
        return this.flexibleSearchService.search(flexQuery);
    }


    public List<CommentModel> findCommentsByItem(UserModel user, Collection<ComponentModel> components, ItemModel item, int offset, int count)
    {
        return findAllInternal(user, components, null, item, offset, count).getResult();
    }


    public List<CommentModel> findCommentsByItemAndType(UserModel user, Collection<ComponentModel> components, ItemModel item, Collection<CommentTypeModel> types, int offset, int count)
    {
        return findAllInternal(user, components, types, item, offset, count).getResult();
    }


    public List<CommentModel> findCommentsByType(UserModel user, Collection<ComponentModel> components, Collection<CommentTypeModel> types, int offset, int count)
    {
        return findAllInternal(user, components, types, null, offset, count).getResult();
    }


    public List<CommentModel> findCommentsByUser(UserModel user, Collection<ComponentModel> components, int offset, int count)
    {
        return findAllInternal(user, components, null, null, offset, count).getResult();
    }


    public List<ReplyModel> findDirectRepliesByComment(CommentModel comment, int offset, int count)
    {
        if(comment == null)
        {
            throw new IllegalArgumentException("Comment must be specified.");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("comment", comment);
        StringBuilder query = new StringBuilder();
        query.append("SELECT {r:").append("pk").append("} ");
        query.append("FROM {").append("Reply").append(" AS r } ");
        query.append("WHERE ");
        query.append("{r:").append("comment").append("} = ?comment ");
        query.append("AND ");
        query.append("{r:").append("parent").append("} IS NULL");
        SearchResult<ReplyModel> searchInternal = searchInternal(query.toString(), params, offset, count);
        return searchInternal.getResult();
    }


    public List<CommentUserSettingModel> findUserSettingsByComment(UserModel user, AbstractCommentModel comment)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("CommentUserSetting").append("} ");
        query.append("WHERE {").append("user").append("} = " + user.getPk() + " ");
        query.append("AND {").append("comment").append("} = " + comment.getPk());
        SearchResult<CommentUserSettingModel> searchInternal = searchInternal(query.toString(), Collections.EMPTY_MAP, 0, -1);
        return searchInternal.getResult();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public List<ComponentModel> findComponentsByDomainAndCode(DomainModel domain, String componentCode)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("domain", domain);
        params.put("componentCode", componentCode);
        StringBuilder query = new StringBuilder();
        query.append("SELECT {c:").append("pk").append("} ");
        query.append("FROM {").append("Component").append(" AS c }");
        query.append("WHERE ");
        query.append("{c:").append("domain").append("} = ?domain ");
        query.append("AND {c:").append("code").append("} = ?componentCode");
        SearchResult<ComponentModel> searchResult = this.flexibleSearchService.search(query.toString(), params);
        List<ComponentModel> result = searchResult.getResult();
        return CollectionUtils.isNotEmpty(result) ? result : Collections.EMPTY_LIST;
    }


    public List<DomainModel> findDomainsByCode(String domainCode)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("domainCode", domainCode);
        StringBuilder query = new StringBuilder();
        query.append("SELECT {d:").append("pk").append("} ");
        query.append("FROM {").append("Domain").append(" AS d }");
        query.append("WHERE {d:").append("code").append("} = ?domainCode");
        SearchResult<DomainModel> searchResult = this.flexibleSearchService.search(query.toString(), params);
        List<DomainModel> result = searchResult.getResult();
        return CollectionUtils.isNotEmpty(result) ? result : Collections.EMPTY_LIST;
    }
}
