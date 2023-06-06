package org.launchcode.LiftoffRecipeProject.data;

import org.launchcode.LiftoffRecipeProject.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Integer>, JpaRepository<Ingredient,Integer> {

    Optional<Ingredient> findByName(String namme);
    List<Ingredient> findByNameIn(List<String>names);
}