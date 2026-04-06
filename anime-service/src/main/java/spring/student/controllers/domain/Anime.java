package spring.student.controllers.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Setter
@Getter
public class Anime {
    private Long id;
    private String name;

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

}
