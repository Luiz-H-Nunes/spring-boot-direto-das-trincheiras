package spring.student.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producer{
    private String name;
    @EqualsAndHashCode.Include
    private Long id;
    private LocalDateTime created;



}


