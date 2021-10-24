package br.com.fiap.challenge.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.challenge.model.Comerciante;
import br.com.fiap.challenge.model.User;
import br.com.fiap.challenge.repository.UserRepository;
import br.com.fiap.challenge.service.AuthenticatioService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private AuthenticatioService authenticationService;
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("users");
		List<User> users = repository.findAll();
		modelAndView.addObject("users", users);
		System.out.println(users);
		return modelAndView;
	}
	
	@RequestMapping("new")
	public String create(User user) {
		return "user-form";
	}
	
	@PostMapping
	public String save(@Valid User user, BindingResult result, RedirectAttributes redirect) {
		if(result.hasErrors()) return "user-form";
		user.setPassword(authenticationService.getPasswordEncoder().encode(user.getPassword()));
		repository.save(user);
		redirect.addFlashAttribute("message", messages.getMessage("message.success.newuser", null, LocaleContextHolder.getLocale()));
		return "redirect:user";
	}
	@RequestMapping("/delete/{id}")
	public String deleteUser(@PathVariable String id) {
		repository.deleteById(Long.parseLong(id));
		return "redirect:/user";
	}
	
	@RequestMapping("/update/{id}")
	public String update(@PathVariable Long id, @Valid User user, BindingResult result, Model model) {
		User teste = repository.findById(id).orElse(null);
		if (result.hasErrors()) {
			
	        user.setId(id);

	       
	        return "user-update-form";
	    }
		repository.save(user);
		return "redirect:/user";
	}

}
