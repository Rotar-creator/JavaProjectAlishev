package ru.rotar.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PersonDTO {
//Всегда используеться на уровне контроллера
    @NotEmpty(message = "zapolni pole")
    @Size(min = 2, max = 30, message = "2 30 char")
    private String name;

    @Min(value = 10, message = "min = 10")
    private int age;

    @Email
    @NotEmpty(message = "not ampty")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
