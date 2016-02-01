package tanya.arthur.selectionhelper.data.model.contract;

public interface Contract {

    String ID = "_id";

    interface Criteria {
        String GROUP_ID = "group_id";
        String NAME = "name";
        String SORT_ORDER = "sort_order";
        String WEIGHT = "weight";
    }

    interface CriteriaGroup {
        String NAME = "name";
    }

    interface Variant {
        String GROUP_ID = "group_id";
        String NAME = "name";
        String SORT_ORDER = "sort_order";
    }

    interface VariantGroup {
        String NAME = "name";
    }

    interface ComparisonScore {
        String CRITERIA_IDS = "criteria_ids";
        String VARIANT_ID = "variant_id";
        String COMPARISON_ID = "comparison_id";
        String SCORES = "scores";
    }

    interface ComparisonInfo {
        String NAME = "name";
        String DATE_START = "date_start";
        String DATE_END = "date_end";
        String CRITERIA_GROUP_ID = "criteria_group_id";
        String VARIANT_GROUP_ID = "variant_group_id";
    }

    interface ComparisonCombination {
        String COMPARISON_INFO_ID = "comparison_info_id";
        String COMBINATIONS_NUMBER = "combinations_number";
        String COMBINATION = "combination";
        String CURRENT_COMBINATION_INDEX = "current_combination_index";
    }
}
