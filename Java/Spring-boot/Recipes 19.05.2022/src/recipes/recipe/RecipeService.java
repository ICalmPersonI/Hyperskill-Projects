package recipes.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import recipes.user.User;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository repository;

    RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    ResponseEntity<?> add(Recipe recipe) {
        recipe.setDate(LocalDateTime.now());
        return new ResponseEntity<>(Map.of("id", repository.save(recipe).getId()), new HttpHeaders(), HttpStatus.OK);
    }

    ResponseEntity<?> get(int id) {
        return repository.findById(id).map(value -> new ResponseEntity<>(value, new HttpHeaders(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND));
    }

    ResponseEntity<?> delete(int id, String userName) {
        return repository.findById(id).map(value -> {
            if (value.getAuthor().equals(userName)) {
                repository.deleteById(id);
                return new ResponseEntity<>(value, new HttpHeaders(), HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }).orElseGet(() -> new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND));
    }

    ResponseEntity<?> update(int id, Recipe recipe, String userName) {
        return repository.findById(id).map(value -> {
            if (value.getAuthor().equals(userName)) {
                value.setName(recipe.getName());
                value.setCategory(recipe.getCategory());
                value.setDescription(recipe.getDescription());
                value.setIngredients(recipe.getIngredients());
                value.setDirections(recipe.getDirections());
                return new ResponseEntity<>(repository.save(value), new HttpHeaders(), HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }).orElseGet(() -> new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND));
    }

    ResponseEntity<?> search(String category, String name) {
        if (category != null) {
            return new ResponseEntity<>(repository.findAllByCategoryIgnoreCaseOrderByDateDesc(category), new HttpHeaders(), HttpStatus.OK);
        } else if (name != null) {
            return new ResponseEntity<>(repository.findAllByNameContainingIgnoreCaseOrderByDateDesc(name), new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

}
