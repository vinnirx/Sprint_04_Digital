package br.com.fiap.challenge.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.challenge.exception.ComercianteNotFoundException;
import br.com.fiap.challenge.model.Comerciante;
import br.com.fiap.challenge.model.User;
import br.com.fiap.challenge.repository.ComercianteRepository;


@Controller
@RequestMapping("/comerciante")
public class ComercianteController {
	
	@Autowired
	private ComercianteRepository repository;
	
	@Autowired
	private MessageSource message;
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("comerciantes");
		List<Comerciante> comerciantes = repository.findAll();
		modelAndView.addObject("comerciantes", comerciantes);
		return modelAndView;
	}
	
	@PostMapping
	public String save(@Valid Comerciante comerciante, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) return "comerciante-form";
		repository.save(comerciante);
		redirect.addFlashAttribute("message", message.getMessage("comerciante.new.success", null, LocaleContextHolder.getLocale()));
		return "redirect:/comerciante";
	}
	
	@RequestMapping("new")
	public String create(Comerciante comerciante) {
		return "comerciante-form";
	}
	
	@GetMapping("/hold/{id}")
	public String hold(@PathVariable Long id, Authentication auth) {
		Optional<Comerciante> optional = repository.findById(id);
		
		if(optional.isEmpty())
			throw new ComercianteNotFoundException("Comerciante não encontrada"); 
		
		Comerciante comerciante = optional.get();
		
		if(comerciante.getUser() != null) 
			throw new NotAllowedException("Comerciante já atribuído no sistema");
		
		
		User user = (User) auth.getPrincipal();
		comerciante.setUser(user);
		
		repository.save(comerciante);
		
		return "redirect:/comerciante";
	}
	
	@GetMapping("/release/{id}")
	public String release(@PathVariable Long id, Authentication auth) {
		Optional<Comerciante> optional = repository.findById(id);
		
		if(optional.isEmpty())
			throw new ComercianteNotFoundException("Comerciante não encontrada"); 
		
		Comerciante comerciante = optional.get();
		User user = (User) auth.getPrincipal();
		
		if(!comerciante.getUser().equals(user)) 
			throw new NotAllowedException("Comerciante está com outro usuário");
		
		comerciante.setUser(null);
		
		repository.save(comerciante);
		
		return "redirect:/comerciante";
	}
	@RequestMapping("/delete/{id}")
	public String deleteComerciante(@PathVariable String id) {
		repository.deleteById(Long.parseLong(id));
		return "redirect:/comerciante";
	}
	
	@RequestMapping("/update/{id}")
	public String update(@PathVariable Long id, @Valid Comerciante comerciante, BindingResult result, Model model) {
		Comerciante teste = repository.findById(id).orElse(null);
		if (result.hasErrors()) {
			
	        comerciante.setId(id);
	        comerciante.setTitle(teste.getTitle());
	        comerciante.setDescription(teste.getDescription());
	        comerciante.setUser(teste.getUser());
	       
	        return "comerciante-update-form";
	    }
		repository.save(comerciante);
		return "redirect:/comerciante";
	}
	
	
	
	
	
	
	
	
}
