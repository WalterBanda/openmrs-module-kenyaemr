package org.openmrs.module.kenyaemr.reporting.data.converter.definition.evaluator.anc;

import org.openmrs.annotation.Handler;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.anc.ANCAZTDispensedDataDefinition;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * ANC AZT Dispensed column
 */
@Handler(supports= ANCAZTDispensedDataDefinition.class, order=50)
public class ANCAZTDispensedDataEvaluator implements EncounterDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedEncounterData evaluate(EncounterDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedEncounterData c = new EvaluatedEncounterData(definition, context);

        String qry = "select\n" +
                "    v.encounter_id,\n" +
                "  (case v.baby_azt_dispensed when 160123 then \"Yes\" when 1066 then \"No\" when 1175 then \"N/A\" else \"\" end) as baby_azt_dispensed\n" +
                "    from kenyaemr_etl.etl_mch_antenatal_visit v inner join kenyaemr_etl.etl_mch_enrollment e on v.patient_id = e.patient_id and e.date_of_discontinuation IS NULL\n" +
                "    GROUP BY v.encounter_id";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
