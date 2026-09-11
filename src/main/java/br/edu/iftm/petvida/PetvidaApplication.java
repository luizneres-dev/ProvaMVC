package br.edu.iftm.petvida;

import br.edu.iftm.petvida.model.Animal;
import br.edu.iftm.petvida.model.Tutor;
import br.edu.iftm.petvida.repository.AnimalRepository;
import br.edu.iftm.petvida.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetvidaApplication implements CommandLineRunner {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private AnimalRepository animalRepository;

    public static void main(String[] args) {
        SpringApplication.run(PetvidaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // ---- Dados da Secao 2 (fixos para todos os alunos) ----
        Tutor marina = new Tutor(1, "Marina Alves", "34 99101-0001");
        Tutor carlos = new Tutor(2, "Carlos Prado", "34 99101-0002");
        tutorRepository.salvar(marina);
        tutorRepository.salvar(carlos);

        Animal mimi = new Animal(2, "Mimi", "gato", 3, marina);
        Animal thor = new Animal(3, "Thor", "cao", 1, carlos);
        Animal lila = new Animal(4, "Lila", "gato", 11, carlos);
        animalRepository.salvar(mimi);
        animalRepository.salvar(thor);
        animalRepository.salvar(lila);

        // ---- Semente de personalizacao (Secao 3) - NN = 70 ----
        Tutor meuTutor = new Tutor(170, "Luiz", "34 97070-7070");
        tutorRepository.salvar(meuTutor);

        Animal meuAnimal = new Animal(170, "Pet_70", "cao", 70, meuTutor);
        animalRepository.salvar(meuAnimal);
    }
}

