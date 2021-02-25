package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController extends ComController { 

	private static final long serialVersionUID = 993614188500846740L;

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) { 
		return "greeting.html";
	}

	@GetMapping("sample/sample_01")
	public String sample_01() {
		return "sample_01.html";
	}

}