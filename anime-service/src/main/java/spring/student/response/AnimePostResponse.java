package spring.student.response;

import lombok.Data;


import java.time.LocalDateTime;
@Data
public class AnimePostResponse {
    private String name;
    private Long id;
    private LocalDateTime created;

}
