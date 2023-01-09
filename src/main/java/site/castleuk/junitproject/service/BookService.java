package site.castleuk.junitproject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.castleuk.junitproject.domain.Book;
import site.castleuk.junitproject.domain.BookRepository;
import site.castleuk.junitproject.util.MailSender;
import site.castleuk.junitproject.web.dto.request.BookSaveReqDto;
import site.castleuk.junitproject.web.dto.response.BookRespDto;

@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepository bookRepository;
  private final MailSender mailSender;

  // 1. 책등록
  @Transactional(rollbackFor = RuntimeException.class)
  public BookRespDto 책등록하기(BookSaveReqDto dto) {
    Book bookPS = bookRepository.save(dto.toEntity());
    if (bookPS != null) {
      if (!mailSender.send()) {
        throw new RuntimeException("메일이 전송되지 않았습니다");
      }
    }
    return bookPS.toDto();
  }

  // 2. 책목록보기
  public List<BookRespDto> 책목록보기() {
    //테스트코드 오류 -> 본코드에 문제가 있나?
    List<BookRespDto> dtos = bookRepository
      .findAll()
      .stream()
      // .map(bookPS -> bookPS.toDto())
      .map(Book::toDto)
      .collect(Collectors.toList());
    //print
    dtos
      .stream()
      .forEach(b -> {
        System.out.println("===================================본코드");
        System.out.println(b.getId());
        System.out.println(b.getTitle());
      });

    return dtos;
  }

  // 3. 책한건보기
  public BookRespDto 책한건보기(Long id) {
    Optional<Book> bookOP = bookRepository.findById(id);
    if (bookOP.isPresent()) {
      //찾았다면
      Book bookPS = bookOP.get();
      return bookPS.toDto();
    } else {
      throw new RuntimeException("해당 아이디를 찾을수 없습니다");
    }
  }

  // 4. 책 삭제
  @Transactional(rollbackFor = RuntimeException.class)
  public void 책삭제하기(Long id) {
    bookRepository.deleteById(id);
  }

  // 5. 책 수정
  @Transactional(rollbackFor = RuntimeException.class)
  public BookRespDto 책수정하기(Long id, BookSaveReqDto dto) {
    Optional<Book> bookOP = bookRepository.findById(id);
    if (bookOP.isPresent()) {
      Book bookPS = bookOP.get();
      bookPS.update(dto.getTitle(), dto.getAuthor());
      return bookPS.toDto();
    } else {
      throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
    }
    //메서드 종료시에 더티체킹(flush)으로 update 합니다.
  }
}
