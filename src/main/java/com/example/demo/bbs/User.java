package com.example.demo.bbs;

import java.sql.Timestamp;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter; 

@Entity
@Table(name = "user_tbl",
		indexes = {
				@Index(name = "user_tbl_idx_email",  columnList="email", unique = false),
				@Index(name = "user_tbl_idx_name",  columnList="name", unique = false),
				@Index(name = "user_tbl_idx_email_uuid", columnList="email_uuid", unique = false)
			}
		)

public class User extends EntityCommon { 
	
	private static final long serialVersionUID = -6023492649132057963L;

	@Id
	@Column( length = 191 )
	@Getter @Setter public String userId ;  
	
	@Getter @Setter public String passwd;	
	@Getter @Setter public String email; 
	
	@OneToOne
	@JoinColumn(name = "roleCode")
	@Getter @Setter public Code role ;
	
	@Getter @Setter public String name;
	
	@Column( length = 191, name="email_uuid"  )
	@Getter @Setter public String emailUuid;
	
	@Getter @Setter public Timestamp lastLoginDt ;
	@Getter @Setter public Timestamp lastLogOutDt ;
	
	public User() {
	}

	public User( String userId, String passwd , Code role ) {
		this.userId = userId ; 
		this.passwd = passwd ;
		this.role = role ; 
	}
	
	
	public boolean isNormalRole() {
		return this.isNormal();
	}
	
	public boolean isNormal() {
		return ! this.isAdminRole() ; 
	}
	
	public boolean isAdminRole() {
		return this.isAdmin();
	}
	
	public boolean isAdmin() {
		return null != role && "USER-ROLE-ADMIN".equalsIgnoreCase( role.codeId );
	}
	
}