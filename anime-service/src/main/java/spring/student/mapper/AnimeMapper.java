package spring.student.mapper;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import spring.student.controllers.AnimeListController;
import spring.student.domain.Anime;
import spring.student.request.AnimePostRequest;
import spring.student.response.AnimeGetResponse;
import spring.student.response.AnimePostResponse;@Mapper


public interface AnimeMapper {

    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    @Mapping(target = "id", expression = "java(AnimeMapper.generateIdAnime())")
    Anime toAnime(AnimePostRequest animePostRequest);

    @Mapping(target = "created",
            expression = "java(java.time.LocalDateTime.now())")
    AnimePostResponse toAnimePostResponse(Anime anime);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    static Long generateIdAnime() {
        return Anime.listAnime().stream()
                .mapToLong(Anime::getId)
                .max()
                .orElse(0L) + 1;
    }
}

