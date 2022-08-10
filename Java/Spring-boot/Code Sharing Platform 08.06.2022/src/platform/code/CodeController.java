package platform.code;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController()
public class CodeController {

    private CodeService service;

    CodeController(CodeService service) {
        this.service = service;
    }

    @GetMapping("code/{id}")
    Object getAsHTML(HttpServletResponse response, @PathVariable String id) {
        return service.get(id).map(value -> {
            response.addHeader("Content-Type", "text/html");
            response.setStatus(200);

            ModelAndView modelAndView = new ModelAndView("code");
            modelAndView.addObject("code", value.getCode());
            modelAndView.addObject("date", value.getDate());
            modelAndView.addObject("time", value.isTimeLimit() ?
                    String.format("<span id=\"time_restriction\">%s</span>\n", value.getTime()) : "");
            modelAndView.addObject("views", value.isViewLimit() ?
                    String.format("<span id=\"views_restriction\">%s</span>\n", value.getViews()) : "");

            return modelAndView;
        }).orElseGet(() -> {
            response.setStatus(404);
            return null;
        });
    }

    @GetMapping("code/new")
    ModelAndView getHTMLFromToAddCode(HttpServletResponse response) {
        response.addHeader("Content-Type", "text/html");
        response.setStatus(200);
        return new ModelAndView("add");
    }

    @GetMapping("code/latest")
    ModelAndView getLatestAsHTML(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/html");
        response.setStatus(200);

        ModelAndView modelAndView = new ModelAndView("latest");
        modelAndView.addObject("codes", service.getLatest());
        return modelAndView;
    }

    @GetMapping("api/code/{id}")
    ResponseEntity<?> getAsJSON(@PathVariable String id) {
        return service.get(id).map(value -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(value, headers, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND));
    }

    @PostMapping("api/code/new")
    ResponseEntity<?> add(@RequestBody Code code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(Map.of("id", service.add(code)), headers, HttpStatus.OK);
    }

    @GetMapping("api/code/latest")
    ResponseEntity<?> getLatestAsJSON() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(service.getLatest(), headers, HttpStatus.OK);
    }

}
