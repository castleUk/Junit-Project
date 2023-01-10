package site.castleuk.junitproject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import site.castleuk.junitproject.domain.Book;
import site.castleuk.junitproject.domain.BookRepository;
import site.castleuk.junitproject.util.MailSender;
import site.castleuk.junitproject.web.dto.request.BookSaveReqDto;
import site.castleuk.junitproject.web.dto.response.BookListRespDto;
import site.castleuk.junitproject.web.dto.response.BookRespDto;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

  @InjectMocks
  private BookService bookService;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private MailSender mailSender;

  //문제점 -> 서비스만 테스트하고 싶은데, 레포지토리 레이어가 함께 테스트가 된다.
  @Test
  public void 책등록하기_테스트() {
    //given
    BookSaveReqDto dto = new BookSaveReqDto();
    dto.setTitle("junit강의");
    dto.setAuthor("성욱");

    //stub (가설)
    when(bookRepository.save(any())).thenReturn(dto.toEntity());
    when(mailSender.send()).thenReturn(true);
    //when

    BookRespDto bookRespDto = bookService.책등록하기(dto);
    //then
    //assertEquals("junit하하하", bookRespDto.getTitle());
    // assertEquals(dto.getAuthor(), bookRespDto.getAuthor());

    assertThat(dto.getTitle()).isEqualTo(bookRespDto.getTitle());
    assertThat(dto.getAuthor()).isEqualTo(bookRespDto.getAuthor());
  }

  @Test
  public void 책목록보기_테스트() {
    //given

    //stub
    List<Book> books = new ArrayList<>();
    books.add(new Book(1L, "junit강의", "메타코딩"));
    books.add(new Book(2L, "스프링강의", "성욱이"));

    when(bookRepository.findAll()).thenReturn(books);
    //when
    BookListRespDto bookListRespDto = bookService.책목록보기();

    //print
    //then
    assertThat(bookListRespDto.getItems().get(0).getTitle())
      .isEqualTo("junit강의");
  }

  @Test
  public void 책한건보기_테스트() {
    //given
    Long id = 1L;
    Book book = new Book(1L, "junit강의", "성욱");
    Optional<Book> bookOP = Optional.of(book);
    //stub
    when(bookRepository.findById(id)).thenReturn(bookOP); //when
    //when
    BookRespDto bookRespDto = bookService.책한건보기(id);

    //then
    assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
    assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());
  }

  @Test
  public void 책수정하기_테스트() {
    //given
    Long id = 1L;
    BookSaveReqDto dto = new BookSaveReqDto();
    dto.setTitle("spring강의");
    dto.setAuthor("서우기");
    //stub
    Book book = new Book(1L, "junit강의", "성욱");
    Optional<Book> bookOp = Optional.of(book);
    when(bookRepository.findById(id)).thenReturn(bookOp);
    //when
    BookRespDto bookRespDto = bookService.책수정하기(id, dto);
    //then
    assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
    assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
  }
}
