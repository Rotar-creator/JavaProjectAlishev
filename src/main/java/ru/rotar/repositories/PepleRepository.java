package ru.rotar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rotar.models.Person;

public interface PepleRepository extends JpaRepository<Person,Integer> {

}
