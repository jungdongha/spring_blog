package me.shinsunyoung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
//이건 dto까지 만들었으니가 아마도 만드는 함수를 정의할듯
public class BlogService {
    private final BlogRepository blogRepository;
    // jpaRepository에서 제공한 save를 Addariclerequest클래스에 저장된 값들을 데이터베이스로 옮겨줌
    public Article save(AddArticleRequest addArticleRequest) {
        return blogRepository.save(addArticleRequest.toEntity());
    }

    public List<Article> finaALl(){
        return blogRepository.findAll();
    }

}
