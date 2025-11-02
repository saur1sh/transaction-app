package com.demo.bankapp.service.concretions;

import com.demo.bankapp.exception.UserNotFoundException;
import com.demo.bankapp.model.User;
import com.demo.bankapp.repository.UserRepository;
import com.demo.bankapp.service.abstractions.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Cacheable(value = "usersCache", unless = "#result==null")
	public List<User> findAll() {
		return repository.findAll();
	}

	@Override
	@CacheEvict("usersCache")
	public User createNewUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repository.save(user);
	}

	@Override
	public User findByUserName(String username) {
		User user = repository.findByUsername(username);

		if (user == null)
			throw new UserNotFoundException(username);
		else
			return user;
	}

	@Override
	public User findByTcno(String tcno) {
		User user = repository.findByTcno(tcno);

		if (user == null)
			throw new UserNotFoundException("with TC No: " + tcno);
		else
			return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = repository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), emptyList());
	}

	@Override
	public boolean isUsernameExist(String username) {
		User user = repository.findByUsername(username);
		return user != null;
	}
	
	@Override
	public boolean isTcnoExist(String tcno) {
		User user = repository.findByTcno(tcno);
		return user != null;
	}

}
