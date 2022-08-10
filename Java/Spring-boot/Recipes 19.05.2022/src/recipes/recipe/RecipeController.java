package recipes.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import recipes.user.UserDetailsImpl;

import javax.validation.Valid;

@RequestMapping("api")
@RestController()
public class RecipeController {

    private final RecipeService service;

    @Autowired
    RecipeController(RecipeService service) {
        this.service = service;
    }

    @PostMapping("recipe/new")
    ResponseEntity<?> add(@RequestBody @Valid Recipe recipe) {
        recipe.setAuthor(getCurrentUserName());
        return service.add(recipe);
    }

    @GetMapping("recipe/{id}")
    ResponseEntity<?> get(@PathVariable int id) {
        return service.get(id);
    }

    @GetMapping("recipe/search")
    ResponseEntity<?> search(@RequestParam(required = false) String category,
                             @RequestParam(required = false) String name) {
       return service.search(category, name);
    }

    @DeleteMapping("recipe/{id}")
    ResponseEntity<?> delete(@PathVariable int id) {
        return service.delete(id, getCurrentUserName());
    }

    @PutMapping("recipe/{id}")
    ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Recipe recipe) {
        return service.update(id, recipe, getCurrentUserName());
    }

    private String getCurrentUserName() {
        return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
    }


}
