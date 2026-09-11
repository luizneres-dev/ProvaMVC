package br.edu.iftm.petvida.controller;

import br.edu.iftm.petvida.model.Animal;
import br.edu.iftm.petvida.model.Tutor;
import br.edu.iftm.petvida.repository.AnimalRepository;
import br.edu.iftm.petvida.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PetvidaController {

    // NN = 70 (semente de personalizacao da Secao 3)
    private static final int ID_TUTOR = 170;
    private static final int ID_ANIMAL = 170;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @GetMapping("/ficha_70")
    public String ficha(Model model) {
        Animal animal = animalRepository.buscarPorId(ID_ANIMAL);

        model.addAttribute("nomeAnimal", animal.getNome());
        model.addAttribute("especieAnimal", animal.getEspecie());
        model.addAttribute("idadeAnimal", animal.getIdade());
        model.addAttribute("nomeTutor", animal.getTutor().getNome());
        model.addAttribute("telefoneTutor", animal.getTutor().getTelefone());

        return "ficha";
    }

    @GetMapping("/tutor_70")
    public String tutor(Model model) {
        Tutor tutor = tutorRepository.buscarPorId(ID_TUTOR);
        int quantidadeAnimais = animalRepository.contarAnimaisDoTutor(ID_TUTOR);

        model.addAttribute("nomeTutor", tutor.getNome());
        model.addAttribute("telefoneTutor", tutor.getTelefone());
        model.addAttribute("quantidadeAnimais", quantidadeAnimais);

        return "tutor";
    }

    @GetMapping("/resumo_70")
    public String resumo(Model model) {
        int totalAnimais = animalRepository.contarAnimais();
        double media = animalRepository.mediaIdade();
        String animalMaisVelho = animalRepository.animalMaisVelho();
        String dataHora = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        // Formatacao e calculo prontos aqui no Controller, pois a view
        // so pode usar th:text (restricao da Parte A.6 / cobrada na Parte F).
        model.addAttribute("totalAnimais", totalAnimais);
        model.addAttribute("mediaIdade", String.format("%.2f", media));
        model.addAttribute("animalMaisVelho", animalMaisVelho);
        model.addAttribute("dataHora", dataHora);

        return "resumo";
    }
}
