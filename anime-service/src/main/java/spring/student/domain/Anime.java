package spring.student.domain;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import spring.student.response.AnimeGetResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Anime {
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private LocalDateTime created;



}
