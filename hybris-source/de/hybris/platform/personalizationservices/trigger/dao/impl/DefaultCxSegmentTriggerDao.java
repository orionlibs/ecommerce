package de.hybris.platform.personalizationservices.trigger.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.enums.CxGroupingOperator;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxSegmentTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.trigger.dao.CxSegmentTriggerDao;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultCxSegmentTriggerDao extends AbstractCxDao<CxSegmentTriggerModel> implements CxSegmentTriggerDao
{
    public DefaultCxSegmentTriggerDao()
    {
        super("CxSegmentTrigger");
    }


    public Collection<CxVariationModel> findApplicableVariations(UserModel user, BigDecimal affinity, CatalogVersionModel catalogVersion)
    {
        String query = " SELECT {v.pk}  FROM {CxUserToSegment as u2s   JOIN CxSegmentToTrigger as s2t on {u2s.segment} = {s2t.target}   JOIN CxSegmentTrigger as t on {t.pk} = {s2t.source}   JOIN CxVariation as v on {t.variation} = {v.pk}  }  WHERE {u2s.affinity} >= ?affinity  AND {v.catalogVersion} = ?catalogVersion  AND {t.catalogVersion} = ?catalogVersion  AND {u2s.user} = ?user  AND (   ( ({t.groupBy} = ?and) AND     ({{ SELECT count(distinct {lu2s.segment})       FROM {CxUserToSegment as lu2s       JOIN CxSegmentToTrigger as ls2t on {ls2t.target} = {lu2s.segment}       }       WHERE {lu2s.user} = ?user       AND {lu2s.affinity} >= ?affinity       AND {ls2t.source} = {t.pk}     }}) = ({{       SELECT count(distinct {rs2t.target})       FROM {CxSegmentToTrigger as rs2t}       WHERE {rs2t.source} = {t.pk}     }})   )  OR   ( {t.groupBy} = ?or ) ) ";
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("catalogVersion", catalogVersion);
        params.put("affinity", affinity);
        params.put("and", CxGroupingOperator.AND);
        params.put("or", CxGroupingOperator.OR);
        return queryList(
                        " SELECT {v.pk}  FROM {CxUserToSegment as u2s   JOIN CxSegmentToTrigger as s2t on {u2s.segment} = {s2t.target}   JOIN CxSegmentTrigger as t on {t.pk} = {s2t.source}   JOIN CxVariation as v on {t.variation} = {v.pk}  }  WHERE {u2s.affinity} >= ?affinity  AND {v.catalogVersion} = ?catalogVersion  AND {t.catalogVersion} = ?catalogVersion  AND {u2s.user} = ?user  AND (   ( ({t.groupBy} = ?and) AND     ({{ SELECT count(distinct {lu2s.segment})       FROM {CxUserToSegment as lu2s       JOIN CxSegmentToTrigger as ls2t on {ls2t.target} = {lu2s.segment}       }       WHERE {lu2s.user} = ?user       AND {lu2s.affinity} >= ?affinity       AND {ls2t.source} = {t.pk}     }}) = ({{       SELECT count(distinct {rs2t.target})       FROM {CxSegmentToTrigger as rs2t}       WHERE {rs2t.source} = {t.pk}     }})   )  OR   ( {t.groupBy} = ?or ) ) ",
                        params);
    }


    public Collection<CxVariationModel> findApplicableVariations(Collection<CxSegmentModel> segments, CatalogVersionModel catalogVersion)
    {
        String query = " SELECT {v.pk}  FROM {CxSegmentToTrigger as s2t   JOIN CxSegmentTrigger as t on {t.pk} = {s2t.source}   JOIN CxVariation as v on {t.variation} = {v.pk}  }  WHERE {s2t.target} IN ( ?segments )  AND {v.catalogVersion} = ?catalogVersion  AND {t.catalogVersion} = ?catalogVersion  AND (   ( ({t.groupBy} = ?and) AND     ({{       SELECT count(distinct {rs2t.target})       FROM {CxSegmentToTrigger as rs2t}       WHERE {rs2t.source} = {t.pk}       AND {rs2t.target} IN ( ?segments )     }}) = ({{       SELECT count(distinct {rs2t.target})       FROM {CxSegmentToTrigger as rs2t}       WHERE {rs2t.source} = {t.pk}     }})   )  OR   ( {t.groupBy} = ?or ) ) ";
        Map<String, Object> params = new HashMap<>();
        params.put("segments", segments);
        params.put("catalogVersion", catalogVersion);
        params.put("and", CxGroupingOperator.AND);
        params.put("or", CxGroupingOperator.OR);
        return queryList(
                        " SELECT {v.pk}  FROM {CxSegmentToTrigger as s2t   JOIN CxSegmentTrigger as t on {t.pk} = {s2t.source}   JOIN CxVariation as v on {t.variation} = {v.pk}  }  WHERE {s2t.target} IN ( ?segments )  AND {v.catalogVersion} = ?catalogVersion  AND {t.catalogVersion} = ?catalogVersion  AND (   ( ({t.groupBy} = ?and) AND     ({{       SELECT count(distinct {rs2t.target})       FROM {CxSegmentToTrigger as rs2t}       WHERE {rs2t.source} = {t.pk}       AND {rs2t.target} IN ( ?segments )     }}) = ({{       SELECT count(distinct {rs2t.target})       FROM {CxSegmentToTrigger as rs2t}       WHERE {rs2t.source} = {t.pk}     }})   )  OR   ( {t.groupBy} = ?or ) ) ",
                        params);
    }
}
