package de.hybris.platform.mediaconversion.web.tag;

import javax.servlet.jsp.PageContext;

public enum Scope
{
    page, request, session, application;


    abstract void set(PageContext paramPageContext, String paramString, Object paramObject);
}
