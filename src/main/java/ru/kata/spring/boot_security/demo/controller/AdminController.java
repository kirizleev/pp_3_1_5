package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.List;

@Controller
public class AdminController {

	private UserService userService;

	@Autowired
	public void setUserService(UserServiceImp userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/admin")
	public String printUsers(ModelMap model) {

		List<User> users = userService.listUsers();

		model.addAttribute("users", users);
		return "admin";
	}

	@GetMapping("/edit_{id}")
	public String editUser(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", userService.getById(id));
		return "edit";
	}

	@PostMapping()
	public String create(@ModelAttribute("person") User user) {
		userService.add(user);
		return "redirect:/admin";
	}

	@PatchMapping("/edit_{id}")
	public String updateUser(@ModelAttribute("user") User user) {
		userService.edit(user);
		return "redirect:/admin";
	}

	@DeleteMapping("/delete_{id}")
	public String deleteUser(@ModelAttribute("user") User user) {
		userService.delete(user);
		return "redirect:/admin";
	}

	@GetMapping("/new")
	public String newUser(@ModelAttribute("user") User user) {
		return "/new";
	}

	@GetMapping()
	public String createUser(@ModelAttribute("user") User user) {
		userService.add(user);
		return "redirect:/admin";
	}
}