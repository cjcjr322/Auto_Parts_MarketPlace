package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.web;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.catalog.Catalog;
import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.catalog.VinResult;
import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.catalog.VinService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class CatalogController {

    private final VinService vinService;

    public CatalogController(VinService vinService) {
        this.vinService = vinService;
    }

    private void addEmail(Principal principal, Model model) {
        model.addAttribute("email", principal != null ? principal.getName() : null);
    }

    @GetMapping("/catalog")
    public String catalog(Principal principal, Model model) {
        addEmail(principal, model);
        model.addAttribute("vin", "");
        model.addAttribute("error", null);
        return "catalog";
    }

    @GetMapping("/catalog/vin")
    public String byVin(@RequestParam(defaultValue = "") String vin,
                        Principal principal, Model model, RedirectAttributes redirect) {
        VinResult r = vinService.decode(vin);
        if (r.error() != null) {
            addEmail(principal, model);
            model.addAttribute("vin", r.vin());
            model.addAttribute("error", r.error());
            return "catalog";
        }
        redirect.addAttribute("make", r.make());
        redirect.addAttribute("model", r.model());
        redirect.addAttribute("year", r.year());
        redirect.addAttribute("vin", r.vin());
        return "redirect:/catalog/car";
    }

    @GetMapping("/catalog/car")
    public String car(@RequestParam(defaultValue = "") String make,
                      @RequestParam(name = "model", defaultValue = "") String carModel,
                      @RequestParam(defaultValue = "") String year,
                      @RequestParam(defaultValue = "") String vin,
                      Principal principal, Model model) {
        if (make.isBlank()) {
            return "redirect:/catalog";
        }
        addEmail(principal, model);
        model.addAttribute("carMake", make.trim());
        model.addAttribute("carModel", carModel.trim());
        model.addAttribute("carYear", year.trim());
        model.addAttribute("carVin", vin.trim());
        model.addAttribute("carTitle", (make + " " + carModel + " " + year).trim().replaceAll("\\s+", " "));
        model.addAttribute("sections", Catalog.SECTIONS);
        return "car";
    }

    @GetMapping("/catalog/section/{code}")
    public String section(@PathVariable String code,
                          @RequestParam(defaultValue = "") String make,
                          @RequestParam(name = "model", defaultValue = "") String carModel,
                          @RequestParam(defaultValue = "") String year,
                          Principal principal, Model model) {
        Catalog.Section section = Catalog.find(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        addEmail(principal, model);
        model.addAttribute("section", section);
        model.addAttribute("carMake", make);
        model.addAttribute("carModel", carModel);
        model.addAttribute("carYear", year);
        model.addAttribute("carTitle", (make + " " + carModel + " " + year).trim().replaceAll("\\s+", " "));
        return "section";
    }
}