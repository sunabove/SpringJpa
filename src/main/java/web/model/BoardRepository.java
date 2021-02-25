package web.model;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BoardRepository extends PagingAndSortingRepository<Board, String> {
	
	Board findByBoardId(Long boardId); 
	
}