package de.hybris.platform.personalizationservices.voters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.integration.context.Orderable;

public interface Voter extends Orderable
{
    Vote getVote(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}
