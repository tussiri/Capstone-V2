package org.launchcode.LiftoffRecipeProject.specification;

import jakarta.persistence.criteria.*;
import org.launchcode.LiftoffRecipeProject.models.Ingredient;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification implements Specification<Recipe> {

    private final SearchCriteria criteria;
    private static final Logger logger = LoggerFactory.getLogger(RecipeSpecification.class);

    public RecipeSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        logger.info("Processing SearchCriteria: key={}, operation={}, value={}",
                criteria.getKey(), criteria.getOperation(), criteria.getValues());

        if (criteria.getKey().equalsIgnoreCase("ingredients")) {
            List<Predicate> predicates = new ArrayList<>();
            for (Object value : criteria.getValues()) {
                Join<Recipe, Ingredient> ingredientsJoin = root.join("ingredients");
                predicates.add(builder.like(ingredientsJoin.get("name"), "%" + value + "%"));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        } else if (criteria.getKey().equalsIgnoreCase("quantity")) {
            return builder.equal(root.get("quantity"), criteria.getValues().get(0));
        } else if (criteria.getKey().equalsIgnoreCase("name")) {
            return builder.like(root.get("name"), "%" + criteria.getValues().get(0) + "%");
        } else if (criteria.getKey().equalsIgnoreCase("time")) {
            if (criteria.getOperation().equalsIgnoreCase(">")) {
                return builder.greaterThanOrEqualTo(root.<Integer>get("time"), Integer.parseInt(criteria.getValues().get(0).toString()));
            } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                return builder.lessThanOrEqualTo(root.<Integer>get("time"), Integer.parseInt(criteria.getValues().get(0).toString()));
            }
        } else if (criteria.getKey().equalsIgnoreCase("rating")) {
            if (criteria.getOperation().equalsIgnoreCase(">")) {
                return builder.greaterThanOrEqualTo(root.<Double>get("rating"), Double.parseDouble(criteria.getValues().get(0).toString()));
            } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                return builder.lessThanOrEqualTo(root.<Double>get("rating"), Double.parseDouble(criteria.getValues().get(0).toString()));
            }
        } else if (criteria.getKey().equalsIgnoreCase("category")) {
            return builder.like(root.<String>get("category"), "%" + criteria.getValues().get(0) + "%");
        }

        return null;
    }


}