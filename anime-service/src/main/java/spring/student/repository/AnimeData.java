package spring.student.repository;

import org.springframework.stereotype.Component;
import spring.student.domain.Anime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class AnimeData {
    private final static List<Anime> ANIMES = new ArrayList<>();

    static {
        List<String> anime = new ArrayList<>(Arrays.asList(
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

        ANIMES.addAll((anime.stream().map(a -> Anime.builder().id(counter.incrementAndGet()).name(a).created(LocalDateTime.now()).build()).collect(Collectors.toCollection(ArrayList::new))));


    }

    public List<Anime> getAnimes() {
        return ANIMES;
    }

}
