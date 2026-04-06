package spring.student.controllers;


import org.springframework.web.bind.annotation.*;
import spring.student.domain.Anime;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@RestController()
@RequestMapping("v1/animes")
public class AnimeListController {

    public static HashSet<Anime> animes;

    static{
        animes = listAnime();
    }


    @GetMapping
    public static HashSet<Anime> listAnime() {
       Set<String> anime = new HashSet<>(Arrays.asList(
                "Dragon Ball Z",
                "Naruto",
                "One Piece",
                "Attack on Titan",
                "Death Note",
                "Fullmetal Alchemist: Brotherhood",
                "Bleach",
                "Demon Slayer (Kimetsu no Yaiba)",
                "My Hero Academia (Boku no Hero Academia)",
                "Neon Genesis Evangelion"
        ));
        AtomicLong counter = new AtomicLong();

       HashSet<Anime> animes = anime.stream().map(a -> new Anime(counter.incrementAndGet(), a)).collect(Collectors.toCollection(HashSet::new));
        return animes;

    }

    @GetMapping("filterList")
    public HashSet<Anime> findByName(@RequestParam(required = false) List<String> animeName) {
        return listAnime().stream().filter(anime -> animeName.stream().anyMatch(hero -> anime.getName().equalsIgnoreCase(hero))).collect(Collectors.toCollection(HashSet::new));
    }

    @GetMapping("filterPath/{idAnime}")
    public Anime filterAnime(@PathVariable Long idAnime) {
        return listAnime().stream().filter(anime -> anime.getId().equals(idAnime)).findFirst().get();
    }

    @PostMapping()
    public Anime addAnime(@RequestBody Anime anime) {
        anime.setId(animes.stream().count() + 1);
        animes.add(anime);
        return anime;

    }


}
