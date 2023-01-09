package site.castleuk.junitproject.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.castleuk.junitproject.service.BookService;
import site.castleuk.junitproject.web.dto.request.BookSaveReqDto;
import site.castleuk.junitproject.web.dto.response.BookListRespDto;
import site.castleuk.junitproject.web.dto.response.BookRespDto;
import site.castleuk.junitproject.web.dto.response.CMRespDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BookApiController { // 컴포지션 = has 관계

  private final BookService bookService;

  //1. 책  등록 하기
  // key=value&key=value <- 스프링의 기본 파싱 전략
  // { "key" : value, "key" : value} <- JSON타입
  // 스프링에서 JSON타입으로 넣으려면 @RequestBody 필요
  @PostMapping("/api/v1/book")
  public ResponseEntity<?> saveBook(
    @RequestBody @Valid BookSaveReqDto bookSaveReqDto,
    BindingResult bindingResult
  ) {
    //AOP 처리하는게 좋음!!
    if (bindingResult.hasErrors()) {
      Map<String, String> errorMap = new HashMap<>();
      for (FieldError fe : bindingResult.getFieldErrors()) {
        errorMap.put(fe.getField(), fe.getDefaultMessage());
      }

      System.out.println("==========================");
      System.out.println(errorMap.toString());
      System.out.println("==========================");

      throw new RuntimeException(errorMap.toString());
    }

    BookRespDto bookRespDto = bookService.책등록하기(bookSaveReqDto);
    return new ResponseEntity<>(
      CMRespDto.builder().code(1).msg("글 저장 성공").body(bookRespDto).build(),
      HttpStatus.CREATED
    ); //201 = insert
  }

  //2. 책 목록 보기
  @GetMapping("/api/v1/book")
  public ResponseEntity<?> getBookList() {
    BookListRespDto bookListRespDto = bookService.책목록보기();
    return new ResponseEntity<>(
      CMRespDto
        .builder()
        .code(1)
        .msg("글 목록 가져오기 성공")
        .body(bookListRespDto)
        .build(),
      HttpStatus.OK
    ); //200 = ok;
  }

  //3. 책 한건보기
  @GetMapping("api/v1/book/{id}")
  public ResponseEntity<?> getBookOne(@PathVariable Long id) {
    BookRespDto bookRespDto = bookService.책한건보기(id);
    return new ResponseEntity<>(
      CMRespDto
        .builder()
        .code(1)
        .msg("글 한건보기 성공")
        .body(bookRespDto)
        .build(),
      HttpStatus.OK
    ); //200 = ok;
  }

  //4. 책 삭제하기
  @DeleteMapping("/api/v1/book/{id}")
  public ResponseEntity<?> deleteBook(@PathVariable Long id) {
    bookService.책삭제하기(id);
    return new ResponseEntity<>(
      CMRespDto.builder().code(1).msg("글 삭제하기 성공").body(null).build(),
      HttpStatus.OK
    ); //200 = ok;
  }

  //5. 책 수정하기
  @PutMapping("/api/v1/book/{id}")
  public ResponseEntity<?> updateBook(
    @PathVariable Long id,
    @RequestBody @Valid BookSaveReqDto bookSaveReqDto,
    BindingResult bindingResult
  ) {
    //AOP 처리하는게 좋음!!
    if (bindingResult.hasErrors()) {
      Map<String, String> errorMap = new HashMap<>();
      for (FieldError fe : bindingResult.getFieldErrors()) {
        errorMap.put(fe.getField(), fe.getDefaultMessage());
      }

      throw new RuntimeException(errorMap.toString());
    }
    BookRespDto bookRespDto = bookService.책수정하기(id, bookSaveReqDto);
    return new ResponseEntity<>(
      CMRespDto
        .builder()
        .code(1)
        .msg("글 수정하기 성공")
        .body(bookRespDto)
        .build(),
      HttpStatus.OK
    ); //200 = ok;
  }
}
