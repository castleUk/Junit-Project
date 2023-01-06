package site.castleuk.junitproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import site.castleuk.junitproject.domain.BookRepository;
import site.castleuk.junitproject.util.MailSenderStub;
import site.castleuk.junitproject.web.dto.BookRespDto;
import site.castleuk.junitproject.web.dto.BookSaveReqDto;

@DataJpaTest
public class BookServiceTest {

  @Autowired
  private BookRepository bookRepository;

  @Test
  public void 책등록하기_테스트() {
    //given
    BookSaveReqDto dto = new BookSaveReqDto();
    dto.setTitle("junit강의");
    dto.setAuthor("성욱");

    //stub
    MailSenderStub mailSenderStub = new MailSenderStub();

    //when
    BookService bookService = new BookService(bookRepository, mailSenderStub);
    BookRespDto bookRespDto = bookService.책등록하기(dto);

    //then
    assertEquals(dto.getTitle(), bookRespDto.getTitle());
    assertEquals(dto.getAuthor(), bookRespDto.getAuthor());
  }
}
