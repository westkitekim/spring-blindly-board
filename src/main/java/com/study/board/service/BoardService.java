package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

// 서비스틑 컨트롤러에서 이용
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 글 작성 - file도 받아줘야함
    public void write(Board board, MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files"; // 프로젝트 경로 지정

        UUID uuid = UUID.randomUUID(); // 랜덤 id 생성

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName); // 명시된 경로에 파일 생성, 이름은 name, 매개변수로 들어온 file 담아줄 껍데기 객체 생성

        file.transferTo(saveFile); //

        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName); // 서버에서 접근시 static 밑의 부분은 static 아래의 경로로만 접근 가능

        boardRepository.save(board);
    }

    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {

        // 기존 List<Board> 반환에서 Page<Board>로 반환
        return boardRepository.findAll(pageable); // findAll : List<Board>반환
    }

    // 특정 게시글 불러오기(게시글 상세)
    public Board boardView(Integer id) {

        // get()으로 받아옴
        return boardRepository.findById(id).get(); //findById(Integer id)
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }


}
