package org.zerock.b01.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.service.ReplyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController // 리턴값 자체가 JSON
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor    // 의존성 주입을 위해.
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "Replies POST", description = "POST방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)  // consumes : 해당 메소드를 받아서 소비하는 데이터가 어떤 종류인지 명시함.
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                      BindingResult bindingResult) throws BindException { // @RequestBody : JSON문자열을 ReplyDTO로 변환하기 위해 표시.
        log.info(replyDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return resultMap;
    }

    @Operation(summary = "Replies of Board", description = "GET방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{bno}")  // 호출하는 경로의 값을 직접 파라미터의 변수로 처리할 수 있음
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }

    @Operation(summary = "Read Reply", description = "GET방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno) {
        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }

    @Operation(summary = "Delete Reply", description = "DELETE방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno) {
        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }

    @Operation(summary = "Modify Reply", description = "PUT방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO) {

        replyDTO.setRno(rno);

        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }
}
