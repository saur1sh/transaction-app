package com.demo.bankapp.service.abstractions;

import com.demo.bankapp.model.User;

import java.util.List;

public interface IUserService {

	List<User> findAll();

	User findByUserName(String username);

	User findByTcno(String tcno);

	User createNewUser(User user);
	
	boolean isUsernameExist(String username);
	
	boolean isTcnoExist(String tcno);

}
