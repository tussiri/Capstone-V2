//package org.launchcode.LiftoffRecipeProject;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.launchcode.LiftoffRecipeProject.models.User;
//import org.launchcode.LiftoffRecipeProject.models.Recipe;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class FavoriteRecipeServiceTest {
//
//    @InjectMocks
//    FavoriteRecipeService favoriteRecipeService;
//
//    @Mock
//    FavoriteRepository favoriteRepository;
//
//    @Test
//    public void testAddFavorite() {
//        Favorite favorite = new Favorite();
//        favorite.setId(1);
//        favorite.setUser(new User());
//        favorite.setRecipe(new Recipe());
//
//        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);
//
//        Favorite result = favoriteRecipeService.addFavorite(favorite);
//
//        assertEquals(favorite.getId(), result.getId());
//    }
//}
