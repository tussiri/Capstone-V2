package org.launchcode.LiftoffRecipeProject.specification;

import jakarta.persistence.criteria.*;
import org.launchcode.LiftoffRecipeProject.models.Ingredient;
import org.launchcode.LiftoffRecipeProject.models.Recipe;
import org.launchcode.LiftoffRecipeProject.models.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecification implements Specification<Recipe> {

    private final SearchCriteria criteria;
    private static final Logger logger= LoggerFactory.getLogger(RecipeSpecification.class);

    public RecipeSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        logger.info("Processing SearchCriteria: key={}, operation={}, value={}",
        criteria.getKey(), criteria.getOperation(), criteria.getValue());

        Predicate predicate = null;
        
        if (criteria.getKey().equalsIgnoreCase("ingredients")) {
            System.out.println("Adding ingredients criteria: " + criteria.getValue());
            Join<Recipe, Ingredient> ingredientsJoin = root.join("ingredients");
            return builder.like(ingredientsJoin.get("name"), "%"+criteria.getValue()+"%");
//            return builder.like(ingredientsJoin, "%" + criteria.getValue() + "%");
//            return builder.like(root.<String>get("ingredients"), "%" + criteria.getValue() + "%");
        } else if (criteria.getKey().equalsIgnoreCase("time")) {
            if (criteria.getOperation().equalsIgnoreCase(">")) {
                System.out.println("Adding time criteria (greater than): " + criteria.getValue());
                return builder.greaterThanOrEqualTo(root.<Integer>get("time"), Integer.parseInt(criteria.getValue().toString()));
            } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                System.out.println("Adding time criteria (less than): " + criteria.getValue());
                return builder.lessThanOrEqualTo(root.<Integer>get("time"), Integer.parseInt(criteria.getValue().toString()));
            }
        } else if (criteria.getKey().equalsIgnoreCase("rating")) {
            if (criteria.getOperation().equalsIgnoreCase(">")) {
                System.out.println("Adding rating criteria (greater than): " + criteria.getValue());
                return builder.greaterThanOrEqualTo(root.<Double>get("rating"), Double.parseDouble(criteria.getValue().toString()));
            } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                System.out.println("Adding rating criteria (less than): " + criteria.getValue());
                return builder.lessThanOrEqualTo(root.<Double>get("rating"), Double.parseDouble(criteria.getValue().toString()));
            }
        } else if (criteria.getKey().equalsIgnoreCase("category")) {
            System.out.println("Adding category criteria: " + criteria.getValue());
            return builder.like(root.<String>get("category"), "%" + criteria.getValue() + "%");
        }

        return null;
    }
}