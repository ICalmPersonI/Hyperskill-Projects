package recipes.recipe;

import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

    Recipe[] findAllByNameContainingIgnoreCaseOrderByDateDesc(String name);

    Recipe[] findAllByCategoryIgnoreCaseOrderByDateDesc(String category);


}
