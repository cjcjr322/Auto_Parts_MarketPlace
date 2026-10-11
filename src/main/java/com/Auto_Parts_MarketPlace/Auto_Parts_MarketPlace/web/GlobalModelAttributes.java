package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.web;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.user.User;
import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.user.UserRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UserRepository users;

    public GlobalModelAttributes(UserRepository users) {
        this.users = users;
    }

    @ModelAttribute("userName")
    public String userName(Principal principal) {
        if (principal == null) return null;
        return users.findByEmail(principal.getName())
                .map(User::getName)
                .orElse(principal.getName());
    }
}