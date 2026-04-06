package spring.student.controllers;


import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/heroes")
public class HeroController {
    public static final List<String> heroes = List.of("Jesus","MyFather","MyMother");


    @GetMapping
    public List<String> ListAllHeroes() {
        return heroes;
    }

    @GetMapping("filter")
    // O RequestParam serve como uma forma de trazer parametros para requisição web permitindo interação com
    // os users para utilizalo na barra de busca ?(nomeDaVariavel)=(valor)
    // podemos usar required como uma forma de previnir se não for usado o filtro na requisição
    public String HeroesParam(@RequestParam(required = false) String heroName) {
        return heroes.stream().filter(hero -> hero.equalsIgnoreCase(heroName)).findFirst().get();
    }


    @GetMapping("filterList")
    public List<String> HeroesFilter(@RequestParam(required = false) List<String> heroName) {
        return heroes.stream().filter(hero -> heroName.stream().anyMatch(h -> h.equalsIgnoreCase(hero)) ).collect(Collectors.toList());
    }


    // Perceba que o nome está entre {} e dentro do url logo o PathVariable serve como alternativa para
    // buscar os objetos de uma classe pela barra da url , se tiver multiplos parametros devem ser separados por /
    // no url e por virgula nos parametros do metodo
    @GetMapping("{name}")
    public List<String> findByName(@PathVariable String name) {
        return heroes.stream().filter(h -> h.equalsIgnoreCase(name)).collect(Collectors.toList());
    }

}


