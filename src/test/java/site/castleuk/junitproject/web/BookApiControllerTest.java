package site.castleuk.junitproject.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import site.castleuk.junitproject.service.BookService;
import site.castleuk.junitproject.web.dto.request.BookSaveReqDto;

//통합 테스트(C, S, R)
// 컨트롤러만 테스트하는 것이 아님
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

  @Autowired
  private BookService bookService;

  @Autowired
  private TestRestTemplate rt;

  private static ObjectMapper om;
  private static HttpHeaders headers;

  @BeforeAll
  public static void init() {
    om = new ObjectMapper();
    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
  }

  @Test
  public void saveBook_test() throws Exception {
    //given
    BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
    bookSaveReqDto.setTitle("스프링 1강");
    bookSaveReqDto.setAuthor("성욱");

    String body = om.writeValueAsString(bookSaveReqDto);

    //when
    HttpEntity<String> request = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = rt.exchange(
      "/api/v1/book",
      HttpMethod.POST,
      request,
      String.class
    );

    //then
    DocumentContext dc = JsonPath.parse(response.getBody());
    String title = dc.read("$.body.title");
    String author = dc.read("$.body.author");

    assertThat(title).isEqualTo("스프링 1강");
    assertThat(author).isEqualTo("성욱");
  }
}
