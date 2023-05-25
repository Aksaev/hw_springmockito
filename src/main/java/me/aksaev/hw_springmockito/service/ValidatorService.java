package me.aksaev.hw_springmockito.service;

import me.aksaev.hw_springmockito.exception.IncorrectNameException;
import me.aksaev.hw_springmockito.exception.IncorrectSurnameException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ValidatorService {
    public String validateName(String name) {
        if (!StringUtils.isAlpha(name)) {
            throw new IncorrectNameException();
        }
        return StringUtils.capitalize(name.toLowerCase());
    }

    public String validateSurname(String surname) {
        String[] surnames = surname.split("-");
        for (int i = 0; i < surnames.length; i++) {
            if (!StringUtils.isAlpha(surnames[i])) {
                throw new IncorrectSurnameException();
            }
            surnames[i] = StringUtils.capitalize(surnames[i].toLowerCase());
        }
        return String.join("-", surnames);
    }

}
