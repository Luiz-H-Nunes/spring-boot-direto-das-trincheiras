package spring.student.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@Builder
public class ProducerGetRespose {
    private String name;
    private Long id;
    private LocalDateTime created ;

}
