package spring.student.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spring.student.controllers.domain.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static spring.student.controllers.domain.Producer.getProducers;


@RestController
@RequestMapping("v1/producers")
public class ProducerController {


    @GetMapping
    public List<Producer> ListAllProducers(){
        return getProducers();
    }



    @GetMapping("filterList")
    public ArrayList<Producer> findByName(@RequestParam(required = false) List<String> producerName) {
        return getProducers().stream().filter(producer -> producerName.stream().anyMatch(hero -> producer.getName().equalsIgnoreCase(hero))).collect(Collectors.toCollection(ArrayList::new));
    }

    @GetMapping("filterPath/{idproducer}")
    public Producer filterProducer(@PathVariable Long idproducer) {
        return getProducers().stream().
                filter(producer -> producer.getId().equals(idproducer)).
                findFirst().get();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,headers = "x-api-key=addProducer")
    public ResponseEntity<Producer> addProducer(@RequestBody Producer producer) {
        producer.setId(getProducers().stream().count() + 1);
        getProducers().add(producer);
        return ResponseEntity.status(HttpStatus.CREATED).body(producer);

    }
}
