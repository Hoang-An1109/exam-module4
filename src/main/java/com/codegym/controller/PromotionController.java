package com.codegym.controller;

import com.codegym.model.Promotion;
import com.codegym.service.IPromotionService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/promotions")
public class PromotionController {
    @Autowired
    private IPromotionService promotionService;
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping
    public ModelAndView listPromotions() {
        ModelAndView modelAndView = new ModelAndView("/promotion/list");
        Iterable<Promotion> promotions = promotionService.findAll();
        modelAndView.addObject("promotions", promotions);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("/promotion/create");
        modelAndView.addObject("promotions", new Promotion());
        return modelAndView;
    }

    @PostMapping("/create")
    public String createPromotion(@Valid @ModelAttribute("promotions") Promotion promotion,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        new Promotion().validate(promotion,bindingResult);
        if (bindingResult.hasErrors()) {
            return "/promotion/create";
        }
        promotionService.save(promotion);
        redirectAttributes.addFlashAttribute("message", "New promotion created successfully");
        return "redirect:/promotions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirect) {
        promotionService.remove(id);
        redirect.addFlashAttribute("message", "Delete student successfully");
        return "redirect:/promotions";
    }
}
