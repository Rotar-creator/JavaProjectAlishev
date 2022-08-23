package ru.rotar.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.rotar.dto.PersonDTO;
import ru.rotar.models.Person;
import ru.rotar.services.PeopleService;
import ru.rotar.util.PersonErrorResponse;
import ru.rotar.util.PersonNotCreatedException;
import ru.rotar.util.PersonNotFoundException;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService,
                            ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }


    /*    @GetMapping()
        public List<Person> fiandAll() {
            return peopleService.faidAll();
        }*/
    @GetMapping()
    public List<PersonDTO> fiandAll() {
        return peopleService.faidAll().stream().map(this::converToPersonDTO)
                .collect(Collectors.toList());
    }

    /*    @GetMapping("/{id}")
        public Person getPearson(@PathVariable("id") int id) {
            return peopleService.findOne(id);
        }*/
    @GetMapping("/{id}")
    public PersonDTO getPearson(@PathVariable("id") int id) {
        return converToPersonDTO(peopleService.findOne(id));
    }


    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO,
                                             BindingResult bindingResult) {
        log.info(String.valueOf("77777" + personDTO));
        if (bindingResult.hasErrors()) {
            //TODO
            StringBuilder errorMSG = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMSG.append(error.getField())
                        .append("-").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMSG.toString());
        }
        peopleService.safe(convertToPerson(personDTO));
        // тправляем HTTP ответ c пустым телом и со статусом 200
        log.info(String.valueOf("77777 ++ OK"));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    ResponseEntity<PersonErrorResponse> hendleException(PersonNotFoundException e) {
        PersonErrorResponse person = new PersonErrorResponse(
                "Person with this id wasn't found",
                System.currentTimeMillis()
        );
        //В HTTP ответе тело ответа(response) и статус в заголовке
        return new ResponseEntity<>(person, HttpStatus.NOT_FOUND); // 404 stays
    }

    @ExceptionHandler
    ResponseEntity<PersonErrorResponse> hendleException(PersonNotCreatedException e) {
        PersonErrorResponse person = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        //В HTTP ответе тело ответа(response) и статус в заголовке
        return new ResponseEntity<>(person, HttpStatus.BAD_REQUEST);

    }

    private Person convertToPerson(PersonDTO personDTO) {
        //оздаем мапер для быстрого преобразования дто класса с основным классом
        /*ModelMapper modelMapper = new ModelMapper();*/
        // берем дто и мапин с основным классом
        Person person = modelMapper.map(personDTO, Person.class);

/*        Person person = new Person();
        person.setName(personDTO.getName());
        person.getAge(personDTO.getAge());
        person.setEmail(personDTO.getEmail());*/
        return person;
    }

/*    //метод обогащения данных персона
    private void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
    }*/

    private PersonDTO converToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
