package me.shinsunyoung.springbootdeveloper.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;

@Getter
@AllArgsConstructor
@NoArgsConstructor
//요청을 받아줄 객체 생성, 전달자 역할
public class AddArticleRequest {

    private String title;
    private String content;

    public Article toEntity() {
        return Article.builder().title(title).content(content).build();

    }


}
