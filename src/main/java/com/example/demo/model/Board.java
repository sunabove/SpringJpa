package com.example.demo.model; 
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter; 

@Entity 
@Table(name = "board_tbl")

public class Board extends EntityCommon { 

	private static final long serialVersionUID = -1390808608446429471L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column( updatable = false, nullable = false)
	@Getter @Setter public Long boardId ; 
	
	@Getter @Setter public String name ; 
	
	public Board() {
	}
	
}