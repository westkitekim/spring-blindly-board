package com.study.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Entity는 JPA에서 DB의 테이블을 의미
@Entity
@Data // lombok - get/set 자동 설정
public class Board { // Table 이름과 일치

    // DB의 필드를 넣어줌
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : mariaDB에서 사용
    private Integer id;

    private String title;

    private String content;

    private String filename;

    private String filepath;
}


