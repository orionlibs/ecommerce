package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchHelper;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.impl.GenericSearchParameterDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.media.MediaModel;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMediaReferenceSelectorModel extends DefaultReferenceSelectorModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMediaReferenceSelectorModel.class);
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String MEDIA_MIME = "media.mime";
    private static final String MIME_TYPES = "mimeTypes";
    private static final String SEMICOLON = ";";
    private static final String EXTRA_PARAM_PREVENT_CACHING = "cimgnr";


    public DefaultMediaReferenceSelectorModel(ObjectType rootType)
    {
        super(rootType);
    }


    public String getDownloadUrlCachingFree(Object object)
    {
        String regularDownloadUrl = getDownloadUrl(object);
        if(StringUtils.isNotEmpty(regularDownloadUrl))
        {
            if(regularDownloadUrl.contains("?"))
            {
                regularDownloadUrl = regularDownloadUrl + "&cimgnr=" + regularDownloadUrl;
            }
            else
            {
                regularDownloadUrl = regularDownloadUrl + "?cimgnr=" + regularDownloadUrl;
            }
        }
        return regularDownloadUrl;
    }


    protected String getDownloadUrl(Object object)
    {
        String downloadUrl = "";
        if(object instanceof TypedObject)
        {
            Object item = ((TypedObject)object).getObject();
            if(item instanceof MediaModel)
            {
                if(!TypeTools.getModelService().isRemoved(item))
                {
                    try
                    {
                        TypeTools.getModelService().refresh(item);
                        MediaModel media = (MediaModel)item;
                        downloadUrl = media.getDownloadURL();
                    }
                    catch(Exception e)
                    {
                        LOG.error("Could not retrieve media URL", e);
                    }
                }
            }
        }
        return downloadUrl;
    }


    public String getMnemonic(Object object)
    {
        String label = "";
        if(object instanceof TypedObject)
        {
            CatalogVersionModel catalogVersion = UISessionUtils.getCurrentSession().getSystemService().getCatalogVersion((TypedObject)object);
            if(catalogVersion != null)
            {
                label = catalogVersion.getMnemonic();
            }
        }
        else
        {
            LOG.warn("Can not get mnemonic for item since it is not a TypedObject.");
            if(object != null)
            {
                label = object.toString();
            }
        }
        return "(" + label + ")";
    }


    protected void createSearchParameterValues(AdvancedSearchParameterContainer parameterContainer, List<SearchParameterValue> paramValues, List<List<SearchParameterValue>> orValues)
    {
        AdvancedSearchHelper.createSearchParameterValues(getAdvancedSearchModel(), parameterContainer, paramValues, orValues);
        PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor("media.mime");
        GenericSearchParameterDescriptor searchDescriptor = (GenericSearchParameterDescriptor)UISessionUtils.getCurrentSession().getSearchService().getSearchDescriptor(propertyDescriptor);
        String rawMimeTypes = (String)getParameters().get("mimeTypes");
        if(rawMimeTypes != null)
        {
            List<SearchParameterValue> orParams = new LinkedList<>();
            for(String singleMimeType : rawMimeTypes.split(";"))
            {
                singleMimeType = singleMimeType.trim();
                SearchParameterValue searchParameter = new SearchParameterValue((SearchParameterDescriptor)searchDescriptor, singleMimeType, searchDescriptor.getDefaultOperator());
                orParams.add(searchParameter);
            }
            orValues.add(orParams);
        }
    }
}
