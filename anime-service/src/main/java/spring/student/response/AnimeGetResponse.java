package spring.student.response;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
@Data
public class AnimeGetResponse {
    private String name;
    private Long id;
    private LocalDateTime created;

}
