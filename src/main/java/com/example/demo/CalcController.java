package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CalcController {
	@GetMapping("/calc")
	public String myCalc (Model model, 
				@RequestParam(value = "a", defaultValue = "0") Double a ,
				@RequestParam(value = "b", defaultValue = "0") Double b ,
				@RequestParam(value = "op", defaultValue = "+") String op
			) {
		
		Double c = a + b ;
		
		model.addAttribute( "c", c );
		
		return "Calc.html";
	}
}