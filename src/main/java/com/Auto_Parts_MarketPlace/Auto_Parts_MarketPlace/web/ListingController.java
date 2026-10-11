package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.web;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing.ListingService;
import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing.ListingType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ListingController {

    private final ListingService service;

    public ListingController(ListingService service) {
        this.service = service;
    }

    private void addEmail(Principal principal, Model model) {
        model.addAttribute("email", principal != null ? principal.getName() : null);
    }

    @GetMapping("/listings")
    public String list(@RequestParam(defaultValue = "SELL") ListingType type,
                       @RequestParam(defaultValue = "") String q,
                       Principal principal, Model model) {
        addEmail(principal, model);
        model.addAttribute("currentType", type.name());
        model.addAttribute("pageTitle",
                type == ListingType.SELL ? "Продаются запчасти" : "Ищут запчасти");
        model.addAttribute("q", q);
        model.addAttribute("items", service.search(type, q));
        return "listings";
    }

    @GetMapping("/listing/{id}")
    public String view(@PathVariable Long id, Principal principal, Model model) {
        addEmail(principal, model);
        model.addAttribute("l", service.get(id));
        return "listing";
    }

    @GetMapping("/my/listings")
    public String mine(Principal principal, Model model) {
        addEmail(principal, model);
        model.addAttribute("items", service.mine(principal.getName()));
        return "my-listings";
    }

    @GetMapping("/my/listings/new")
    public String form(Principal principal, Model model) {
        addEmail(principal, model);
        return "listing-form";
    }

    @PostMapping("/my/listings")
    public String create(@RequestParam ListingType type,
                         @RequestParam String title,
                         @RequestParam(defaultValue = "") String partNumber,
                         @RequestParam(defaultValue = "") String carInfo,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) Integer price,
                         @RequestParam(required = false) String city,
                         Principal principal) {
        service.create(principal.getName(), type, title, partNumber, carInfo, description, price, city);
        return "redirect:/my/listings";
    }

    @PostMapping("/my/listings/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        service.delete(id, principal.getName());
        return "redirect:/my/listings";
    }
}