package ru.rotar.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rotar.models.Person;
import ru.rotar.repositories.PepleRepository;
import ru.rotar.util.PersonNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PepleRepository pepleRepository;

    @Autowired
    public PeopleService(PepleRepository pepleRepository) {
        this.pepleRepository = pepleRepository;
    }

    public List<Person> faidAll() {
        return pepleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPearsen = pepleRepository.findById(id);
        // return foundPearsen.orElse(null);
        // return foundPearsen.orElseThrow(PersonNotFoundException::new);
        return foundPearsen.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public void safe(Person person) {
        enrichPerson(person);
        pepleRepository.save(person);
    }
    //метод обогащения данных персона
    private void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
    }
}
