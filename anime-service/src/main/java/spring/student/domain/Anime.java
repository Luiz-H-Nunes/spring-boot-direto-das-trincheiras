package spring.student.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import spring.student.response.AnimeGetResponse;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Setter
@Getter
public class Anime {
    private Long id;
    private String name;
    private static HashSet<Anime> animes;

   static  {
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

         animes = anime.stream().map(a -> new Anime(counter.incrementAndGet(), a)).collect(Collectors.toCollection(HashSet::new));
    }

    public Anime() {
    }
    public Anime(Long id, String name) {
        this.id = id;
        this.name = name;

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Anime anime = (Anime) o;
        return Objects.equals(id, anime.id);
    }

    public static HashSet<Anime> listAnime() {
       return animes;
    }

}
