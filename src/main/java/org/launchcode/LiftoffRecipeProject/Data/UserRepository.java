package org.launchcode.LiftoffRecipeProject.Data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;

public interface UserRepository extends CrudRepository<User,Integer> {
}
