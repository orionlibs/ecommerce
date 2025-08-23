package de.hybris.platform.cmscockpit.components.liveedit.converter;

import de.hybris.platform.cmscockpit.events.impl.CmsUrlChangeEvent;
import de.hybris.platform.cmscockpit.session.impl.FrontendAttributes;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class DefaultCmsUrlChangeEventConverter implements Converter<String[], CmsUrlChangeEvent>
{
    private Converter<String[], FrontendAttributes> frontEndAttributesConverter;


    protected String extractRequestPath(String longUrl)
    {
        String ret = "";
        if(!longUrl.contains("cx-preview"))
        {
            String[] urlParts = longUrl.split("[\\?&]cmsTicketId");
            ret = urlParts[0];
        }
        return ret;
    }


    public CmsUrlChangeEvent convert(String[] attributes) throws ConversionException
    {
        CmsUrlChangeEvent event = new CmsUrlChangeEvent(UISessionUtils.getCurrentSession().getCurrentPerspective(), extractRequestPath(attributes[1]), attributes[2], attributes[3], attributes[4]);
        return convert(attributes, event);
    }


    public CmsUrlChangeEvent convert(String[] attributes, CmsUrlChangeEvent prototype) throws ConversionException
    {
        if(prototype instanceof CmsUrlChangeEvent && getFrontEndAttributesConverter() != null)
        {
            prototype.setExtendedFrontendAttributes((FrontendAttributes)getFrontEndAttributesConverter().convert(attributes));
        }
        return prototype;
    }


    public Converter<String[], FrontendAttributes> getFrontEndAttributesConverter()
    {
        return this.frontEndAttributesConverter;
    }


    public void setFrontEndAttributesConverter(Converter<String[], FrontendAttributes> frontEndAttributesConverter)
    {
        this.frontEndAttributesConverter = frontEndAttributesConverter;
    }
}
