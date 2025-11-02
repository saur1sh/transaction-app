package com.demo.bankapp.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user")
public class User {

	private @Id @GeneratedValue Long id;
	private String username;
	private String password;
	private String tcno;

	private User() {
	}

	public User(String username, String password, String tcno) {
		this.username = username;
		this.password = password;
		this.tcno = tcno;
	}
}
