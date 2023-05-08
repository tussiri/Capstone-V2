package org.launchcode.LiftoffRecipeProject.data;

import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);
}
