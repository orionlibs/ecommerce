package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class CatalogTools
{
    public static final int ERROR = 1712;
    public static final String SOURCE = "SOURCE";
    private static final Logger LOG = Logger.getLogger(CatalogTools.class.getName());


    public static synchronized Product copyProduct(Product oldProd, CatalogVersion newCV)
    {
        Product newProd = null;
        try
        {
            newProd = copyItems(Collections.singletonList(oldProd), newCV).iterator().next();
            newProd.setAttribute(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION, newCV);
            newProd.setProperty("SOURCE", oldProd.getPK());
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1712);
        }
        return newProd;
    }


    public static synchronized Keyword copyKeyword(Keyword oldKeywrd, CatalogVersion newCV)
    {
        Keyword newKeywrd = null;
        try
        {
            newKeywrd = copyItems(Collections.singletonList(oldKeywrd), newCV).iterator().next();
            newKeywrd.setAttribute(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION, newCV);
            newKeywrd.setProperty("SOURCE", oldKeywrd.getPK());
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1712);
        }
        return newKeywrd;
    }


    public static synchronized Media copyMedia(Media oldMedia, CatalogVersion newCV)
    {
        Media newMedia = null;
        try
        {
            newMedia = copyItems(Collections.singletonList(oldMedia), newCV).iterator().next();
            newMedia.setAttribute(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION, newCV);
            newMedia.setProperty("SOURCE", oldMedia.getPK());
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1712);
        }
        return newMedia;
    }


    public static synchronized Category copyCategory(Category oldCat, CatalogVersion newCV)
    {
        Category newCat = null;
        try
        {
            newCat = copyItems(Collections.singletonList(oldCat), newCV).iterator().next();
            newCat.setAttribute(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION, newCV);
            newCat.setProperty("SOURCE", newCat.getPK());
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1712);
        }
        return newCat;
    }


    public static synchronized Collection copyAllProducts(CatalogVersion oldCV, CatalogVersion newCV)
    {
        Collection<Product> copiedProducts = new ArrayList();
        try
        {
            int start = 0;
            int range = 10;
            List result = null;
            do
            {
                result = getCatalogVersionItems(Product.class, oldCV, start, 10).getResult();
                for(Iterator<Product> it = result.iterator(); it.hasNext(); )
                {
                    copiedProducts.add(copyProduct(it.next(), newCV));
                }
                start += 10;
            }
            while(result.size() >= 10);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1712);
        }
        return copiedProducts;
    }


    public static synchronized Collection copyAllCategories(CatalogVersion oldCV, CatalogVersion newCV)
    {
        Collection<Category> copiedCategories = new ArrayList();
        try
        {
            int start = 0;
            int range = 10;
            List result = null;
            do
            {
                result = getCatalogVersionItems(Category.class, oldCV, start, 10).getResult();
                for(Iterator<Category> it = result.iterator(); it.hasNext(); )
                {
                    copiedCategories.add(copyCategory(it.next(), newCV));
                }
                start += 10;
            }
            while(result.size() >= 10);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1712);
        }
        return copiedCategories;
    }


    public static synchronized Collection copyAllKeywords(CatalogVersion oldCV, CatalogVersion newCV)
    {
        Collection<Keyword> copiedKeywords = new ArrayList();
        try
        {
            int start = 0;
            int range = 10;
            List result = null;
            do
            {
                result = getCatalogVersionItems(Keyword.class, oldCV, start, 10).getResult();
                for(Iterator<Keyword> it = result.iterator(); it.hasNext(); )
                {
                    copiedKeywords.add(copyKeyword(it.next(), newCV));
                }
                start += 10;
            }
            while(result.size() >= 10);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1712);
        }
        return copiedKeywords;
    }


    public static synchronized Collection copyAllMedias(CatalogVersion oldCV, CatalogVersion newCV)
    {
        Collection<Media> copiedMedias = new ArrayList();
        try
        {
            int start = 0;
            int range = 10;
            List result = null;
            do
            {
                result = getCatalogVersionItems(Media.class, oldCV, start, 10).getResult();
                for(Iterator<Media> it = result.iterator(); it.hasNext(); )
                {
                    copiedMedias.add(copyMedia(it.next(), newCV));
                }
                start += 10;
            }
            while(result.size() >= 10);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1712);
        }
        return copiedMedias;
    }


    public static SearchResult getCatalogVersionItems(Class<?> itemclass, CatalogVersion catalogVersion, int start, int count)
    {
        JaloSession jSession = JaloSession.getCurrentSession();
        Map<Object, Object> map = new HashMap<>();
        map.put("catversion", catalogVersion);
        map.put("cat", catalogVersion.getCatalog());
        SearchResult result = jSession.getFlexibleSearch().search("SELECT {PK} FROM {" + jSession
                                        .getTypeManager().getComposedType(itemclass).getCode() + "} WHERE {catalogversion} = ?catversion AND {catalog} = ?cat ORDER BY {PK} ASC", map,
                        Collections.singletonList(itemclass), true, true, start, count);
        return result;
    }


    public static synchronized Collection copyItems(Collection items, CatalogVersion newCV)
    {
        Collection<Item> newItems = new ArrayList();
        Language lang = JaloSession.getCurrentSession().getSessionContext().getLanguage();
        JaloSession.getCurrentSession().getSessionContext().setLanguage(null);
        for(Iterator<Item> it = items.iterator(); it.hasNext(); )
        {
            newItems.add(copyItemInternal(it.next(), newCV));
        }
        JaloSession.getCurrentSession().getSessionContext().setLanguage(lang);
        return newItems;
    }


    private static Item copyItemInternal(Item item, CatalogVersion newCV)
    {
        Item back = null;
        try
        {
            ComposedType itemType = item.getComposedType();
            Set attrdesc = itemType.getAttributeDescriptors();
            Item.ItemAttributeMap<String, Object> itemAttributeMap = new Item.ItemAttributeMap();
            AttributeDescriptor attributeDescriptor = null;
            for(Iterator<AttributeDescriptor> it = attrdesc.iterator(); it.hasNext(); )
            {
                attributeDescriptor = it.next();
                if((attributeDescriptor.isInitial() || attributeDescriptor.isWritable()) && !attributeDescriptor.isPartOf())
                {
                    LOG.debug("Copying: " + attributeDescriptor.getQualifier() + " // " + item
                                    .getAttribute(attributeDescriptor.getQualifier()));
                    itemAttributeMap.put(attributeDescriptor.getQualifier(), item.getAttribute(attributeDescriptor.getQualifier()));
                }
            }
            back = itemType.newInstance((Map)itemAttributeMap);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1712);
        }
        return back;
    }
}
