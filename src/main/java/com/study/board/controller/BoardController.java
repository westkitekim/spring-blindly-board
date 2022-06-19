package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    // GetMapping : 어떤 url로 접근할 것인지 지정해주는 어노테이션
    @GetMapping("/board/write") // localhost:8090/board/write 라는 url로 접근하면 boardwrite.html 문서를 보여줌
    public String boardWriteForm() {

        return "boardwrite";
    }

    @PostMapping("/board/writepro") // Process의 pro
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception { // Entity 형식 그대로 클라 데이터를 (title, content) 매개변수로 받아줄 수 있음

        boardService.write(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    /*
        sort : 정렬 기준 컬럼
        direction : 정렬순서

        nowPage : 현재페이지
        startPage : 블럭에서 보여줄 시작페이지
        endPage : 블럭에서 보여줄 마지막 페이지
     */
    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) { // data.domain의 Pageable class 사용

        Page<Board> list = boardService.boardList(pageable);

        // pageable이 가지고 있는 페이지는 0부터 시작하기 때문에 +1
        int nowPage = list.getPageable().getPageNumber() + 1; // pageable.getPageNumber(); 동일
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @GetMapping("/board/view") // localhost:8090/board/view?id=1
    public String boardView(Model model, Integer id) { // 앞단에 넘겨줄 때 Model을 적어줌

        model.addAttribute("board", boardService.boardView(id));

        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {

        boardService.boardDelete(id);
        return "redirect:/board/list"; // 일반적으로 삭제 후 리스트로 넘어감
    }

    // @PathVariable : url이 들어왔을 때, /뒤의 {id} 부분이 @PathVariable("id")에 인식이 돼서 그 뒤의 Integer 타입의 id로 들어온다는 것
    // 쿼리스트링처럼 ?로 구성되지 않고 깔끔하게 데이터로만 구성 -> modify/8
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,
                              Model model) {

        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board,
                              Model model, MultipartFile file) throws Exception { // @PathVariable id를 Integer id에 담아주겠다. url에 값이 있으면 PathVariable로 받아오는 것

        Board boardTemp = boardService.boardView(id); // 기존의 글이 담겨진 객체 불러옴
        boardTemp.setTitle(board.getTitle()); // 기존의 내용에 새로운 제목과 내용 덮어씌움
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file); // Repository에 저장

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    // 파일업로드시 1. 파일에 저장된 경로 2. 파일의 이름을 저장하기 위한 컬럼 => 2가지가 필요
}







