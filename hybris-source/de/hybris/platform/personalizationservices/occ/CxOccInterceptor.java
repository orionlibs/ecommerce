package de.hybris.platform.personalizationservices.occ;

import de.hybris.platform.personalizationservices.voters.CxOccInterceptorVote;
import de.hybris.platform.personalizationservices.voters.Vote;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.integration.context.Orderable;

public interface CxOccInterceptor extends Orderable
{
    @Deprecated(since = "1905", forRemoval = true)
    default boolean shouldPersonalizeRequest(HttpServletRequest request)
    {
        return true;
    }


    default CxOccInterceptorVote shouldPersonalizeRequestVote(HttpServletRequest request)
    {
        CxOccInterceptorVote result = new CxOccInterceptorVote();
        result.setVote(true);
        result.setConclusive(false);
        return result;
    }


    default void beforeGetVote(HttpServletRequest request)
    {
    }


    default void beforeVoteExecution(HttpServletRequest request, Vote vote)
    {
    }


    default void afterVoteExecution(HttpServletRequest request, HttpServletResponse response, Vote vote)
    {
    }
}
