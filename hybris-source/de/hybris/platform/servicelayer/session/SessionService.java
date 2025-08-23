package de.hybris.platform.servicelayer.session;

import de.hybris.platform.core.model.user.UserModel;
import java.util.Map;

public interface SessionService
{
    Session getCurrentSession();


    @Deprecated(since = "5.5.1", forRemoval = true)
    Session getSession(String paramString);


    Session createNewSession();


    void closeSession(Session paramSession);


    void closeCurrentSession();


    boolean hasCurrentSession();


    void setAttribute(String paramString, Object paramObject);


    <T> T getAttribute(String paramString);


    <T> T getOrLoadAttribute(String paramString, SessionAttributeLoader<T> paramSessionAttributeLoader);


    <T> Map<String, T> getAllAttributes();


    <T> T executeInLocalView(SessionExecutionBody paramSessionExecutionBody);


    <T> T executeInLocalViewWithParams(Map<String, Object> paramMap, SessionExecutionBody paramSessionExecutionBody);


    <T> T executeInLocalView(SessionExecutionBody paramSessionExecutionBody, UserModel paramUserModel);


    void removeAttribute(String paramString);


    Session getBoundSession(Object paramObject);


    Object getRawSession(Session paramSession);
}
