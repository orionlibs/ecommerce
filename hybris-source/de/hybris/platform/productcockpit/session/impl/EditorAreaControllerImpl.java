package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelLabelRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultEditorAreaController;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFormat;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.productcockpit.components.sectionpanel.DefaultSectionPanelRenderer;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import de.hybris.platform.variants.jalo.VariantProduct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zkplus.spring.SpringUtil;

public class EditorAreaControllerImpl extends DefaultEditorAreaController
{
    private static final Logger LOG = LoggerFactory.getLogger(EditorAreaControllerImpl.class);
    private static final String MEDIA_FORMAT_SCALED = "thumbnail_pc_scaled";
    private transient CatalogService productCockpitCatalogService;
    private transient DefaultSectionPanelRenderer sectionPanelLabelRenderer;


    @Required
    public void setProductCockpitCatalogService(CatalogService productCockpitCatalogService)
    {
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    protected CatalogService getProductCockpitCatalogService()
    {
        if(this.productCockpitCatalogService == null)
        {
            this.productCockpitCatalogService = (CatalogService)SpringUtil.getBean("productCockpitCatalogService");
        }
        return this.productCockpitCatalogService;
    }


    protected SectionPanelLabelRenderer createSectionPanelLabelRenderer()
    {
        if(this.sectionPanelLabelRenderer == null)
        {
            this.sectionPanelLabelRenderer = new DefaultSectionPanelRenderer(getModel());
        }
        return (SectionPanelLabelRenderer)this.sectionPanelLabelRenderer;
    }


    protected void updateLabelForProduct(Product product, AbstractSectionPanelModel model)
    {
        this.sectionPanelLabelRenderer.setCurrentObject(UISessionUtils.getCurrentSession().getTypeService().wrapItem(product));
        CatalogVersion version = CatalogManager.getInstance().getCatalogVersion(product);
        String code = product.getCode();
        Media thumb = product.getThumbnail();
        Media thumbnail = null;
        if(thumb != null)
        {
            MediaFormat mediaFormat = MediaManager.getInstance().getMediaFormatByQualifier("thumbnail_pc_scaled");
            if(mediaFormat != null)
            {
                thumbnail = thumb.getInFormat(mediaFormat);
            }
        }
        boolean scaledVersion = (thumbnail != null);
        if(!scaledVersion)
        {
            thumbnail = product.getThumbnail();
        }
        String imageUrl = "";
        try
        {
            imageUrl = (thumbnail == null) ? "" : thumbnail.getURL();
        }
        catch(Exception e)
        {
            LOG.warn("Could not retrieve thumbnail URL:", e);
        }
        String name = getLocalizedAttribute((Item)product, "name");
        if(name == null)
        {
            if(product instanceof VariantProduct)
            {
                name = "#base#" + getLocalizedAttribute((Item)((VariantProduct)product).getBaseProduct(), "name");
            }
            else
            {
                name = " ";
            }
        }
        String v_name = "---";
        if(version == null)
        {
            LOG.warn("No catalog version set for product " + product);
        }
        else
        {
            v_name = CockpitManager.getInstance().getMnemonic(version);
        }
        model.setLabel(name + " #-# " + name + " #-# (" + code + ")");
        model.setImageUrl(StringUtils.isBlank(imageUrl) ? null : imageUrl);
    }


    protected String getLocalizedAttribute(Item item, String attribute)
    {
        CatalogManager catalogManager = CatalogManager.getInstance();
        Set<Language> languages = new LinkedHashSet<>(10);
        languages.add(C2LManager.getInstance().getLanguageByIsoCode(UISessionUtils.getCurrentSession().getGlobalDataLanguageIso()));
        if(item instanceof CatalogVersion)
        {
            languages.addAll(((CatalogVersion)item).getLanguages());
        }
        else if(catalogManager.isCatalogItem(item))
        {
            CatalogVersion version = catalogManager.getCatalogVersion(null, item);
            if(version != null)
            {
                languages.addAll(version.getLanguages());
            }
        }
        languages.add(C2LManager.getInstance().getLanguageByIsoCode(UISessionUtils.getCurrentSession().getLanguageIso()));
        List<Language> all = new ArrayList<>(C2LManager.getInstance().getAllLanguages());
        Collections.sort(all, (Comparator<? super Language>)new Object(this));
        languages.addAll(all);
        SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
        for(Language l : languages)
        {
            ctx.setLanguage(l);
            try
            {
                String ret = (String)item.getAttribute(ctx, attribute);
                if(ret != null && ret.length() > 0)
                {
                    return ret;
                }
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return null;
    }


    public void updateLabel(SectionPanelModel sectionPanelModel)
    {
        TypedObject current = getModel().getCurrentObject();
        if(current != null)
        {
            Object object = current.getObject();
            if(object instanceof de.hybris.platform.core.model.product.ProductModel)
            {
                updateLabelForProduct((Product)TypeTools.getModelService().getSource(object), (AbstractSectionPanelModel)sectionPanelModel);
                ((AbstractSectionPanelModel)sectionPanelModel).refreshInfoContainer();
            }
        }
    }


    public void updateEditorRequest(TypedObject typedObject, PropertyDescriptor descriptor)
    {
        super.updateEditorRequest(typedObject, descriptor);
        TypedObject current = getModel().getCurrentObject();
        if(current != null && current.getObject() instanceof de.hybris.platform.core.model.product.ProductModel)
        {
            SectionPanelModel mod = getSectionPanelModel();
            ((AbstractSectionPanelModel)mod).refreshInfoContainer();
        }
    }
}
