package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.jalo.template.CockpitItemTemplate;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.Utilities;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CockpitManager extends GeneratedCockpitManager
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitManager.class.getName());


    public static CockpitManager getInstance()
    {
        return (CockpitManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("cockpit");
    }


    @Deprecated
    public List<CockpitObjectAbstractCollection> getGlobalCollections()
    {
        return JaloSession.getCurrentSession().getFlexibleSearch()
                        .search("SELECT {PK} FROM {CockpitObjectCollection} WHERE {user} IS NULL", CockpitObjectAbstractCollection.class)
                        .getResult();
    }


    @Deprecated
    public List<CockpitObjectCollection> getAllCollections()
    {
        return JaloSession.getCurrentSession().getFlexibleSearch()
                        .search("SELECT {PK} FROM {CockpitObjectCollection}", CockpitObjectCollection.class).getResult();
    }


    public List<CockpitSavedQuery> getGlobalSavedQueries()
    {
        return JaloSession.getCurrentSession().getFlexibleSearch()
                        .search("SELECT {PK} FROM {CockpitSavedQuery} WHERE {user} IS NULL", CockpitObjectCollection.class).getResult();
    }


    public String getMnemonic(SessionContext ctx, CatalogVersion item)
    {
        String ret = (String)item.getProperty(ctx, GeneratedCockpitConstants.Attributes.CatalogVersion.MNEMONIC);
        if(ret == null)
        {
            ret = item.getCatalog().getId();
            if(ret.length() >= 3)
            {
                ret = ret.substring(0, 3);
            }
            String version = item.getVersion();
            if(version != null && version.length() > 0)
            {
                ret = ret + "-" + ret;
            }
            ret = ret.toUpperCase();
        }
        return ret;
    }


    @Deprecated
    public List<CockpitObjectAbstractCollection> getCollections(SessionContext ctx, User user)
    {
        return JaloSession.getCurrentSession().getFlexibleSearch()
                        .search("SELECT {PK} FROM {CockpitObjectCollection} WHERE {user}='" + user.getPK() + "'", CockpitObjectCollection.class)
                        .getResult();
    }


    @Deprecated
    public List<CockpitObjectAbstractCollection> getSpecialCollections(User user)
    {
        return JaloSession.getCurrentSession().getFlexibleSearch()
                        .search("SELECT {PK} FROM {CockpitObjectSpecialCollection} WHERE {user}='" + user.getPK() + "'", CockpitObjectSpecialCollection.class)
                        .getResult();
    }


    @Deprecated
    public List<CockpitObjectAbstractCollection> getSpecialCollections(User user, String collectionType)
    {
        if(collectionType == null)
        {
            return Collections.EMPTY_LIST;
        }
        EnumerationValue typeEnum = null;
        try
        {
            typeEnum = JaloSession.getCurrentSession().getEnumerationManager().getEnumerationValue(GeneratedCockpitConstants.TC.COCKPITSPECIALCOLLECTIONTYPE, collectionType);
        }
        catch(Exception e)
        {
            return Collections.EMPTY_LIST;
        }
        if(typeEnum == null)
        {
            return Collections.EMPTY_LIST;
        }
        return JaloSession.getCurrentSession().getFlexibleSearch()
                        .search("SELECT {PK} FROM {CockpitObjectSpecialCollection} WHERE {user} = " + user.getPK().toString() + " AND {collectiontype} = " + typeEnum
                                        .getPK().toString(), CockpitObjectSpecialCollection.class)
                        .getResult();
    }


    public String generateOpenInHMCURL(String pk, HttpServletRequest request) throws EJBPasswordEncoderNotFoundException
    {
        StringBuilder hmc = new StringBuilder();
        String value = JaloSession.getCurrentSession().generateLoginTokenCookieValue();
        String baseurl = Config.getParameter("hmc.base.url");
        if((baseurl == null || baseurl.trim().length() < 1) && request != null)
        {
            int serverport = request.getServerPort();
            String scheme = request.getScheme();
            String servername = request.getServerName();
            StringBuilder base = new StringBuilder();
            base.append(scheme);
            base.append("://");
            base.append(servername);
            base.append(":");
            base.append(serverport);
            base.append((String)Utilities.getInstalledWebModules().get("hmc"));
            base.append("/hybris");
            baseurl = base.toString();
        }
        else if(request == null)
        {
            LOG.warn("No project.properties entry 'hmc.base.url' found!");
        }
        else
        {
            LOG.warn("Submitted request was null!");
        }
        hmc.append(baseurl).append("?open=").append(pk).append("&").append(Config.getParameter("login.token.url.parameter"))
                        .append("=").append(value);
        return hmc.toString();
    }


    public CockpitItemTemplate getCockpitItemTemplate(ComposedType type, String qualifier)
    {
        Map<String, Object> values = new HashMap<>(2);
        values.put("type", type);
        values.put("quali", qualifier);
        List<CockpitItemTemplate> result = JaloSession.getCurrentSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + "} WHERE {relatedType} = ?type AND {code} = ?quali", values, CockpitItemTemplate.class).getResult();
        return result.isEmpty() ? null : result.get(0);
    }


    public Collection<CockpitItemTemplate> getMatchingItemTemplates(Item item)
    {
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            ComposedType type = item.getComposedType();
            Collection<ClassificationClass> classes = (item instanceof Product) ? CatalogManager.getInstance().getClassificationClasses((Product)item) : Collections.EMPTY_LIST;
            return getMatchingItemTemplates(type, classes);
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


    public CockpitUIComponentConfiguration createCockpitUIComponentConfiguration(String factoryBean, String code, String objectTemplateCode, Principal principal, Media media)
    {
        Map<String, Object> attributeValues = new HashMap<>();
        attributeValues.put("factoryBean", factoryBean);
        attributeValues.put("code", code);
        attributeValues.put("objectTemplateCode", objectTemplateCode);
        attributeValues.put("principal", principal);
        attributeValues.put("media", media);
        return createCockpitUIComponentConfiguration(attributeValues);
    }


    public CockpitItemTemplate createCockpitItemTemplate(String code, Class<? extends Item> itemClass)
    {
        Map<String, Object> values = new HashMap<>();
        values.put("relatedType", TypeManager.getInstance().getComposedType(itemClass));
        values.put("code", code);
        return createCockpitItemTemplate(values);
    }


    private Collection<CockpitItemTemplate> getMatchingItemTemplates(ComposedType type, Collection<ClassificationClass> classes)
    {
        List<ComposedType> supertypes = type.getAllSuperTypes();
        List<ComposedType> types = new ArrayList<>(supertypes.size() + 1);
        types.add(type);
        types.addAll(supertypes);
        Map<String, Object> values = new HashMap<>(1);
        values.put("types", types);
        List<CockpitItemTemplate> typeMatches = JaloSession.getCurrentSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + GeneratedCockpitConstants.TC.COCKPITITEMTEMPLATE + "} WHERE {relatedType} IN (?types)", values, CockpitItemTemplate.class).getResult();
        List<CockpitItemTemplate> matches = new ArrayList<>();
        if(classes.isEmpty())
        {
            for(CockpitItemTemplate template : typeMatches)
            {
                if(template.getClassificationClassesCount() == 0L)
                {
                    matches.add(template);
                }
            }
        }
        else
        {
            Set<ClassificationClass> allclasses = new HashSet<>(classes);
            allclasses.addAll(getAllSuperclasses(classes));
            for(CockpitItemTemplate template : typeMatches)
            {
                if(matchByClasses(template, allclasses))
                {
                    matches.add(template);
                }
            }
        }
        Collections.sort(matches, (Comparator<? super CockpitItemTemplate>)new Object(this, type));
        return matches;
    }


    private Set<ClassificationClass> getAllSuperclasses(Collection<ClassificationClass> classes)
    {
        Set<ClassificationClass> superclasses = new HashSet<>();
        for(ClassificationClass cl : classes)
        {
            superclasses.addAll(cl.getAllSuperClasses());
        }
        return superclasses;
    }


    private boolean matchByClasses(CockpitItemTemplate template, Set<ClassificationClass> allclasses)
    {
        Collection<ClassificationClass> templateClasses = template.getClassificationClasses();
        for(ClassificationClass cc : templateClasses)
        {
            if(!allclasses.contains(cc))
            {
                return false;
            }
        }
        return true;
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc) throws Exception
    {
        List<String> extensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
        super.createEssentialData(params, jspc);
        importCSVFromResources("/cockpit/import/essential_data.csv");
        if(extensionNames.contains("comments"))
        {
            importCSVFromResources("/cockpit/import/comments.csv");
        }
        importCSVFromResources("/cockpit/import/report_ui_components.csv");
    }


    public void createProjectData(Map<String, String> params, JspContext jspc) throws Exception
    {
        super.createProjectData(params, jspc);
        importCSVFromResources("/cockpit/import/core_ui_components.csv");
    }


    private void importCSVFromResources(String csv) throws Exception
    {
        importCSVFromResources(csv, "UTF-8", ';', '"', true);
    }


    private void importCSVFromResources(String csv, String encoding, char fieldseparator, char quotecharacter, boolean codeExecution) throws Exception
    {
        LOG.info("importing resource " + csv);
        InputStream inputStream = CockpitManager.class.getResourceAsStream(csv);
        try
        {
            if(inputStream == null)
            {
                LOG.warn("Import resource '" + csv + "' not found!");
                return;
            }
            ImpExManager.getInstance().importData(inputStream, encoding, fieldseparator, quotecharacter, codeExecution);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }


    public MediaFolder getJasperReportsMediaFolder()
    {
        Collection<MediaFolder> folders = MediaManager.getInstance().getMediaFolderByQualifier("jasperreports");
        if(folders.isEmpty())
        {
            return null;
        }
        return folders.iterator().next();
    }


    @Deprecated
    public Set<CockpitObjectAbstractCollection> getAllCollections(User user)
    {
        Collection userOwnedCollections = getCollections(user);
        Set<User> userGroupsAndUser = user.getAllGroups();
        userGroupsAndUser.add(user);
        String collCode = TypeManager.getInstance().getComposedType(CockpitObjectCollection.class).getCode();
        Map<Object, Object> params = new HashMap<>();
        params.put("users", userGroupsAndUser);
        String readQuery = "SELECT {" + Item.PK + "} FROM {" + collCode + " AS coll JOIN " + GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION + " AS readRelation ON {coll." + Item.PK + "} = {readRelation.target}} WHERE {readRelation.source} IN (?users)";
        SearchResult readRes = getSession().getFlexibleSearch().search(readQuery, params,
                        Collections.singletonList(CockpitObjectCollection.class), true, true, 0, -1);
        Collection userReadableCollections = readRes.getResult();
        String writeQuery = "SELECT {" + Item.PK + "} FROM {" + collCode + " AS coll JOIN " + GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION + " AS writeRelation ON {coll." + Item.PK + "} = {writeRelation.target}} WHERE {writeRelation.source} IN (?users)";
        SearchResult writeRes = getSession().getFlexibleSearch().search(writeQuery, params,
                        Collections.singletonList(CockpitObjectCollection.class), true, true, 0, -1);
        Collection userWritableCollections = writeRes.getResult();
        Set<CockpitObjectAbstractCollection> resultList = new HashSet();
        resultList.addAll(userOwnedCollections);
        resultList.addAll(userReadableCollections);
        resultList.addAll(userWritableCollections);
        return resultList;
    }
}
