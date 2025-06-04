package controllers.v1;

import model.PersonEntity;
import service.IPersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/controllers/v1/persona")
public class PersonController {

    private final IPersonService personService;

    public PersonController(IPersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/id/{id}")
    public Mono<PersonEntity> finById(@PathVariable Long id){
        return this.personService.getById(id);
    }
}
