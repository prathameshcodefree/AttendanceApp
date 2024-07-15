package com.attendance.model;

import com.expense.model.constant.TokenType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor	
@AllArgsConstructor
@Entity
@Table(name = "token_log")
public class TokenLog {

	@Id
	@Column(name = "token_log_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;	

	@Column(name = "token", length = 500)
	private String token;
	
	@Enumerated(EnumType.STRING)
	private TokenType tokentype;
	 
	@Column(name="expired")
	private boolean expired;
	
	@Column(name="revoked")
	private boolean revoked;

	
	
	

	
	
	

}