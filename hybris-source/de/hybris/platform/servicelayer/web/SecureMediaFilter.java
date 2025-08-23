package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.servicelayer.media.MediaPermissionService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.MediaUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.GenericFilterBean;

@Deprecated(since = "6.3.0", forRemoval = true)
public class SecureMediaFilter extends GenericFilterBean
{
    private static final Logger LOG = Logger.getLogger(SecureMediaFilter.class);
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private MediaPermissionService mediaPermissionService;
    private ModelService modelService;
    private UserService userService;
    private MediaService mediaService;
    private String secureMediaToken = "securemedias";


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if(!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
        {
            throw new ServletException("SecureMediaFilter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String resourcePath = getResourcePath(httpRequest);
        try
        {
            setSecureURLRendererForThread(httpResponse);
            if(StringUtils.contains(resourcePath, this.secureMediaToken))
            {
                MediaModel mediaModel;
                String mediaPKStr = httpRequest.getParameter("mediaPK");
                try
                {
                    mediaModel = (MediaModel)this.modelService.get(PK.parse(mediaPKStr));
                }
                catch(Exception e)
                {
                    logMediaLookupError(mediaPKStr, e);
                    httpResponse.sendError(404);
                    return;
                }
                if(!isAccessGranted(mediaModel))
                {
                    setForbiddenResponseStatus(httpResponse);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("No access for the resource: " + resourcePath);
                    }
                    httpResponse.sendError(403);
                    return;
                }
                sendData(httpResponse, httpRequest, mediaModel);
            }
            else
            {
                chain.doFilter(request, response);
            }
        }
        finally
        {
            clearSecureURLRendererForThread();
        }
    }


    protected void logMediaLookupError(String mediaPKStr, Exception e)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Invalid or outdated secure media pk " + mediaPKStr, e);
        }
        else
        {
            LOG.info("Invalid or outdated secure media pk");
        }
    }


    private void clearSecureURLRendererForThread()
    {
        MediaUtil.unsetCurrentSecureMediaURLRenderer();
    }


    private void setSecureURLRendererForThread(HttpServletResponse httpResponse)
    {
        String urlEncoded = httpResponse.encodeURL(this.secureMediaToken);
        MediaUtil.setCurrentSecureMediaURLRenderer(media -> urlEncoded + "?mediaPK=" + urlEncoded);
    }


    private void sendData(HttpServletResponse httpResponse, HttpServletRequest httpRequest, MediaModel media) throws IOException
    {
        int length = (media.getSize() == null) ? 0 : media.getSize().intValue();
        String mime = (media.getMime() == null) ? "application/octet-stream" : media.getMime();
        httpResponse.setContentLength(length);
        httpResponse.setContentType(mime);
        if(isAddContentDisposition(httpRequest))
        {
            httpResponse.addHeader("Content-Disposition", " attachment; filename=" + media.getRealFileName());
        }
        InputStream inputStream = null;
        try
        {
            inputStream = this.mediaService.getStreamFromMedia(media);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Loading secure resource from media storage [PK: " + media.getPk() + ", User: " + this.userService
                                .getCurrentUser() + ", Mime: " + mime + "]");
            }
            IOUtils.copy(inputStream, (OutputStream)httpResponse.getOutputStream());
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }


    protected boolean isAddContentDisposition(HttpServletRequest httpRequest)
    {
        return Boolean.parseBoolean(httpRequest.getParameter("attachment"));
    }


    protected String getResourcePath(HttpServletRequest httpRequest)
    {
        String resourcePath = httpRequest.getServletPath();
        if(resourcePath == null || resourcePath.trim().isEmpty())
        {
            String reqURI = httpRequest.getRequestURI();
            String ctxPath = httpRequest.getContextPath();
            resourcePath = reqURI.replace(ctxPath, "");
        }
        return resourcePath;
    }


    protected InputStream getResourceAsStream(String resourceName)
    {
        return Media.class.getResourceAsStream(resourceName);
    }


    private void setForbiddenResponseStatus(HttpServletResponse httpResponse)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Access forbidden for given media...");
        }
        httpResponse.setStatus(403);
    }


    private boolean isAccessGranted(MediaModel mediaModel)
    {
        boolean result = this.mediaPermissionService.isReadAccessGranted(mediaModel, (PrincipalModel)this.userService.getCurrentUser());
        if(LOG.isDebugEnabled())
        {
            String readAccessText = result ? " is granted to read the media" : " has NO read access for the media";
            LOG.debug("The Principal " + this.userService.getCurrentUser().getName() + readAccessText + mediaModel.getCode());
        }
        return result;
    }


    @Required
    public void setMediaPermissionService(MediaPermissionService mediaPermissionService)
    {
        this.mediaPermissionService = mediaPermissionService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public void setSecureMediaToken(String secureMediaToken)
    {
        if(StringUtils.isNotBlank(secureMediaToken))
        {
            this.secureMediaToken = secureMediaToken;
        }
    }
}
