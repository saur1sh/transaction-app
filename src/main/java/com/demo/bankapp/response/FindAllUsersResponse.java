package com.demo.bankapp.response;

import com.demo.bankapp.model.User;
import lombok.Data;

import java.util.List;

@Data
public class FindAllUsersResponse {
	List<User> userList;
}
