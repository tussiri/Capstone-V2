package org.launchcode.LiftoffRecipeProject.data;

import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer>, CrudRepository<User, Integer> {

    User findByEmail(String email);

}
