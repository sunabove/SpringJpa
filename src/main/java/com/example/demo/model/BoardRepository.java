package com.example.demo.model;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BoardRepository extends PagingAndSortingRepository<Board, Long> {
	
	Board findByBoardId(Long boardId); 
	
}