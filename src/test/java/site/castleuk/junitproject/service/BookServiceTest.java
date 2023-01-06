package site.castleuk.junitproject.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.castleuk.junitproject.domain.Book;
import site.castleuk.junitproject.domain.BookRepository;
import site.castleuk.junitproject.util.MailSender;
import site.castleuk.junitproject.web.dto.BookRespDto;
import site.castleuk.junitproject.web.dto.BookSaveReqDto;

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
    List<BookRespDto> bookRespDtoList = bookService.책목록보기();

    //print
    bookRespDtoList
      .stream()
      .forEach(b -> {
        System.out.println(b.getId());
        System.out.println(b.getTitle());
      });
    //then
    assertThat(bookRespDtoList.get(0).getTitle()).isEqualTo("junit강의");
  }

  @Test
  public void 책한건보기테스트() {
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
}
