package me.shinsunyoung.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    //직렬화, 역직렬화를 위한 클래스
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMVCSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }


    @Nested
    class BlogApiTest {
        @DisplayName("addArticle : 블로그 글 추가에 성공한다.")
        @Test
        public void addArticle() throws Exception {
            //given
            final String url = "/api/articles";
            final String title = "title";
            final String content = "content";
            final AddArticleRequest userRequest = new AddArticleRequest(title, content);

            final String requestBody = objectMapper.writeValueAsString(userRequest);

            //when
            ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));

            //then

            result.andExpect(status().isCreated());

            List<Article> articles = blogRepository.findAll();
            assertThat(articles.size()).isEqualTo(1);
            assertThat(articles.get(0).getTitle()).isEqualTo(title);
            assertThat(articles.get(0).getContent()).isEqualTo(content);
        }
    }

    @DisplayName("findArticles : 블로그 글 목록 조회에 성공한다")
    @Test
    public void findArticles() throws Exception {
        //given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder().title(title).content(content).build());

        //when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON_VALUE));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));

    }

    @DisplayName("findArticles : 글목록 조회에 성공한다")
    @Test
    public void findArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());


        //when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("deleteArticle : 블로그 글 삭제에 성공한다")
    @Test

    public void deleteArticle() throws Exception {
        //when
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        //when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());
        //then
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();

    }

    @DisplayName("updateArticle : 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        final String newTitle = "newTitle";
        final String newContent = "newContent";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        //when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId()).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)));


        //then
        result.andExpect(status().isOk());
        Article article = blogRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);

    }
}