package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class PDFDocumentComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "PDFDocumentComponent";
    public static final String PDFFILE = "pdfFile";
    public static final String TITLE = "title";
    public static final String HEIGHT = "height";


    public PDFDocumentComponentModel()
    {
    }


    public PDFDocumentComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PDFDocumentComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PDFDocumentComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "height", type = Accessor.Type.GETTER)
    public Integer getHeight()
    {
        return (Integer)getPersistenceContext().getPropertyValue("height");
    }


    @Accessor(qualifier = "pdfFile", type = Accessor.Type.GETTER)
    public MediaModel getPdfFile()
    {
        return getPdfFile(null);
    }


    @Accessor(qualifier = "pdfFile", type = Accessor.Type.GETTER)
    public MediaModel getPdfFile(Locale loc)
    {
        return (MediaModel)getPersistenceContext().getLocalizedValue("pdfFile", loc);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle()
    {
        return getTitle(null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("title", loc);
    }


    @Accessor(qualifier = "height", type = Accessor.Type.SETTER)
    public void setHeight(Integer value)
    {
        getPersistenceContext().setPropertyValue("height", value);
    }


    @Accessor(qualifier = "pdfFile", type = Accessor.Type.SETTER)
    public void setPdfFile(MediaModel value)
    {
        setPdfFile(value, null);
    }


    @Accessor(qualifier = "pdfFile", type = Accessor.Type.SETTER)
    public void setPdfFile(MediaModel value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("pdfFile", loc, value);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value)
    {
        setTitle(value, null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("title", loc, value);
    }
}
