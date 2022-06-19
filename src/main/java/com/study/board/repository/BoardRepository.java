package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> { // Generic <Entity 이름, PK의 데이터타입>, 어떤 엔티티를 넣을 것인지

}
