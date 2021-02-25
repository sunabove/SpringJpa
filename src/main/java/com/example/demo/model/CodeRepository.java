package com.example.demo.model;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CodeRepository extends PagingAndSortingRepository<Code, String> {
	
	Code findByCodeId(String codeId);
	
	ArrayList<Code> findByGrpCode(Code grpCode ); 
	
}