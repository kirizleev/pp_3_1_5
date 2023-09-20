package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final UserService userService;
	private final RegistrationService registrationService;

	public AdminController(UserService userService, RegistrationService registrationService) {
		this.userService = userService;
		this.registrationService = registrationService;
	}

	@GetMapping
	public String printUsers(ModelMap model,
							 @ModelAttribute("newUser") User newUser) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		model.addAttribute("user", (User) userDetails);

		model.addAttribute("users", userService.listUsers());

		return "admin";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute("user") User user) {
		registrationService.register(user);
		return "redirect:/admin";
	}

	@PatchMapping("/edit_{id}")
	public String updateUser(@ModelAttribute("user") User user) {
		registrationService.edit(user);
		return "redirect:/admin";
	}

	@DeleteMapping("/delete_{id}")
	public String deleteUser(@ModelAttribute("userList") User user) {
		userService.delete(user);
		return "redirect:/admin";
	}
}