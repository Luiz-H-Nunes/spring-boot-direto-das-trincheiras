package spring.student.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import spring.student.domain.Producer;
import spring.student.request.ProducerPostRequest;
import spring.student.response.ProducerGetRespose;

@Mapper()
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);
    @Mapping(target = "created" ,expression = "java(java.time.LocalDateTime.now)")
    @Mapping(
            target = "id",
            expression = "java(producerService.getProducers().stream()" +
                    ".mapToLong(Producer::getId)" +
                    ".max().orElse(0L) + 1)"
    )

    Producer toProducer(ProducerPostRequest producerPostRequest);

    ProducerGetRespose toProducerGetRespose(Producer producer);
}
