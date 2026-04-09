package spring.student.repository;

import org.springframework.stereotype.Repository;
import spring.student.domain.Anime;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
@Repository
public class AnimeHardCodeRepository {
    private static List<Anime> ANIMES = new ArrayList<>();

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

    public static List<Anime> findAll() {
        return ANIMES;
    }

    public  Optional<Anime> findById(Long id) {
        return  ANIMES.stream().filter(anime -> anime.getId().equals(id)).findFirst();

    }

    public  List<Anime> findByName(String name) {
        return ANIMES.stream().filter(anime -> anime.getName().equals(name)).toList();
    }

    public  Anime save(Anime anime) {
         ANIMES.add(anime);
        return anime;
    }

    public  Anime delete(Anime anime) {
        ANIMES.remove(anime);
        return anime;
    }

    public  Anime update(Anime anime) {
        delete(anime);
        save(anime);
        return anime;
    }


}
