package site.castleuk.junitproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.castleuk.junitproject.domain.Book;
import site.castleuk.junitproject.domain.BookRepository;
import site.castleuk.junitproject.web.dto.BookRespDto;
import site.castleuk.junitproject.web.dto.BookSaveReqDto;

@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepository bookRepository;

  // 1. 책등록
  @Transactional(rollbackFor = RuntimeException.class)
  public BookRespDto 책등록하기(BookSaveReqDto dto) {
    Book bookPS = bookRepository.save(dto.toEntity());
    return new BookRespDto().toDto(bookPS);
  }
  // 2. 책목록보기

  // 3. 책한건보기

  // 4. 책 삭제

  // 5. 책 수정

}
