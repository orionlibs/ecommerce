package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.celum.CockpitCelumDelegate;
import de.hybris.platform.cockpit.services.celum.impl.CelumNotAvailableException;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CelumEventHandler extends AbstractRequestEventHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(CelumEventHandler.class);
    private static final String RAW_MEDIA_QUALIFIER = "raw";
    public static final String CELUM_CONTAINER_KEY = "cont";
    public static final String CELUM_ITEM_KEY = "item";
    public static final String CELUM_PROP_QUALIFIER = "prop";
    public static final String CELUM_MEDIA_FORMAT = "mf";
    public static final String LANG_ISO_CODE = "isoCode";
    private MediaService mediaService = null;
    private TypeService typeService = null;
    private ModelService modelService = null;
    private CockpitCelumDelegate celumDelegate;


    public void handleEvent(UICockpitPerspective perspective, Map<String, String[]> params)
    {
        if(perspective == null)
        {
            LOG.warn("Can not handle celum event. Reason: No perspective has been specified.");
        }
        else
        {
            try
            {
                TypedObject item = getItem(params);
                PropertyDescriptor propDescr = getPropertyDescriptor(params);
                TypedObject media = getMedia(params);
                ObjectValueContainer valueContainer = TypeTools.createValueContainer(item, Collections.singleton(propDescr),
                                UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos());
                String isoCode = getParameter(params, "isoCode");
                ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue(propDescr, propDescr.isLocalized() ? isoCode : null);
                if(propDescr.getMultiplicity() == PropertyDescriptor.Multiplicity.SINGLE)
                {
                    valueHolder.setLocalValue(media);
                }
                else if(propDescr.getMultiplicity() == PropertyDescriptor.Multiplicity.LIST)
                {
                    Collection currentValue = (Collection)valueHolder.getCurrentValue();
                    ArrayList newValue = new ArrayList();
                    CollectionUtils.addIgnoreNull(newValue, media);
                    newValue.addAll(currentValue);
                    valueHolder.setLocalValue(newValue);
                }
                else if(propDescr.getMultiplicity() == PropertyDescriptor.Multiplicity.SET)
                {
                    Collection currentValue = (Collection)valueHolder.getCurrentValue();
                    HashSet newValue = new HashSet();
                    CollectionUtils.addIgnoreNull(newValue, media);
                    newValue.addAll(currentValue);
                    valueHolder.setLocalValue(newValue);
                }
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Persisting values... Item='" + item + "', property='" + propDescr + "', media='" + media + "'.");
                }
                EditorHelper.persistValues(item, valueContainer);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Values persisted.");
                }
            }
            catch(IllegalStateException ise)
            {
                LOG.error("Processing of request event aborted. Reason: " + ise.getMessage() + ".");
            }
            catch(Exception e)
            {
                LOG.error("Processing of request event aborted. Reason: Unknown error occurred.", e);
            }
        }
    }


    public void setPrefix(String prefix)
    {
        if("cel".equals(prefix))
        {
            super.setPrefix(prefix);
        }
        else
        {
            LOG.warn("Not setting prefix. Reason: Specified prefix '" + prefix + "' does not match required one 'cel'. Please check your Spring configuration.");
        }
    }


    protected TypedObject getItem(Map<String, String[]> params) throws IllegalStateException
    {
        TypedObject item = null;
        String itemPk = getParameter(params, "item");
        if(StringUtils.isBlank(itemPk))
        {
            throw new IllegalStateException("Request event ignored. Reason: No item PK specified.");
        }
        item = UISessionUtils.getCurrentSession().getTypeService().wrapItem(PK.parse(itemPk));
        if(item == null)
        {
            throw new IllegalStateException("Could not process request event. Reason: Invalid item.");
        }
        return item;
    }


    protected TypedObject getMedia(Map<String, String[]> params) throws IllegalStateException
    {
        TypedObject media = null;
        String containerPk = getParameter(params, "cont");
        if(StringUtils.isBlank(containerPk))
        {
            throw new IllegalStateException("Request event ignored. Reason: No celum container PK specified.");
        }
        MediaContainerModel containerModel = (MediaContainerModel)this.modelService.get(PK.parse(containerPk));
        if(containerModel == null)
        {
            throw new IllegalStateException("No valid media container specified.");
        }
        String mfString = getParameter(params, "mf");
        if(StringUtils.isBlank(mfString))
        {
            try
            {
                mfString = getCockpitCelumDelegate().getOriginalMediaFormat();
                LOG.warn("No explicit media format found! Original media format will be used instead");
            }
            catch(CelumNotAvailableException cnae)
            {
                throw new IllegalStateException("No valid media format could be retrieved.", cnae);
            }
        }
        MediaFormatModel mediaFormatModel = this.mediaService.getFormat(mfString);
        if(mediaFormatModel == null)
        {
            mediaFormatModel = this.mediaService.getFormat("raw");
        }
        media = this.typeService.wrapItem(this.mediaService.getMediaByFormat(containerModel, mediaFormatModel));
        if(media == null)
        {
            throw new IllegalStateException("Could not process request event. Reason: Invalid media.");
        }
        return media;
    }


    protected CockpitCelumDelegate getCockpitCelumDelegate()
    {
        if(this.celumDelegate == null)
        {
            this.celumDelegate = (CockpitCelumDelegate)Registry.getApplicationContext().getBean("cockpitCelumDelegate");
        }
        return this.celumDelegate;
    }


    protected PropertyDescriptor getPropertyDescriptor(Map<String, String[]> params) throws IllegalStateException
    {
        PropertyDescriptor propDescr = null;
        String propQuali = getParameter(params, "prop");
        if(StringUtils.isBlank(propQuali))
        {
            throw new IllegalStateException("Request event ignored. Reason: No property qualifier specified.");
        }
        propDescr = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propQuali);
        if(propDescr == null)
        {
            throw new IllegalStateException("Could not process request event. Reason: Invalid property descriptor.");
        }
        return propDescr;
    }


    public String getPrefix()
    {
        if(StringUtils.isBlank(super.getPrefix()))
        {
            super.setPrefix("cel");
        }
        return super.getPrefix();
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
