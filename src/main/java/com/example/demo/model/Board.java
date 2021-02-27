package com.example.demo.model; 
import javax.persistence.*;

import lombok.*;  

@Entity @Table(name = "board_tbl")
@Data @EqualsAndHashCode(callSuper=false)
public class Board extends EntityCommon { 

	private static final long serialVersionUID = -1390808608446429471L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column( updatable = false, nullable = false)
	public Long boardId ; 
	
	public String name ; 
	
	public Board() {
	}
	
}