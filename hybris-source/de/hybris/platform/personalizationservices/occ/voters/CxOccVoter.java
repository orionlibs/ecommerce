package de.hybris.platform.personalizationservices.occ.voters;

import de.hybris.platform.personalizationservices.voters.Vote;
import javax.servlet.http.HttpServletRequest;
import org.springframework.integration.context.Orderable;

public interface CxOccVoter extends Orderable
{
    Vote getVote(HttpServletRequest paramHttpServletRequest);
}
