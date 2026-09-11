package br.edu.iftm.petvida.repository;

import br.edu.iftm.petvida.model.Animal;
import br.edu.iftm.petvida.model.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AnimalRepository {

    @Autowired
    private JdbcTemplate jdbc;

    // RowMapper que monta o Animal ja com o Tutor preenchido, a partir
    // de uma unica linha vinda do JOIN entre animal e tutor.
    private final RowMapper<Animal> animalComTutorMapper = (rs, rowNum) -> {
        Tutor tutor = new Tutor(
                rs.getInt("id_tutor"),
                rs.getString("tutor_nome"),
                rs.getString("telefone")
        );
        return new Animal(
                rs.getInt("id_animal"),
                rs.getString("animal_nome"),
                rs.getString("especie"),
                rs.getInt("idade"),
                tutor
        );
    };

    // UMA unica consulta SQL com JOIN entre animal e tutor.
    public Animal buscarPorId(int id) {
        String sql = "SELECT a.id_animal, a.nome AS animal_nome, a.especie, a.idade, " +
                "t.id_tutor, t.nome AS tutor_nome, t.telefone " +
                "FROM animal a JOIN tutor t ON a.tutor_id_tutor = t.id_tutor " +
                "WHERE a.id_animal = ?";
        return jdbc.queryForObject(sql, animalComTutorMapper, id);
    }

    // Contagem feita pelo SQL, nao em Java.
    public int contarAnimais() {
        String sql = "SELECT COUNT(*) FROM animal";
        return jdbc.queryForObject(sql, Integer.class);
    }

    // Media calculada pelo SQL. Uso CAST para DOUBLE para nao perder as
    // casas decimais (armadilha do H2 citada na Secao 2 da prova).
    public double mediaIdade() {
        String sql = "SELECT AVG(CAST(idade AS DOUBLE)) FROM animal";
        return jdbc.queryForObject(sql, Double.class);
    }

    // Ordenacao e limite feitos pelo SQL; so o nome do animal mais velho volta para o Java.
    public String animalMaisVelho() {
        String sql = "SELECT nome FROM animal ORDER BY idade DESC LIMIT 1";
        return jdbc.queryForObject(sql, String.class);
    }

    // Consulta parametrizada (?).
    public int contarAnimaisDoTutor(int idTutor) {
        String sql = "SELECT COUNT(*) FROM animal WHERE tutor_id_tutor = ?";
        return jdbc.queryForObject(sql, Integer.class, idTutor);
    }

    // INSERT com jdbc.update; a chave estrangeira sai de animal.getTutor().getId().
    public void salvar(Animal animal) {
        String sql = "INSERT INTO animal (id_animal, nome, especie, idade, tutor_id_tutor) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, animal.getId(), animal.getNome(), animal.getEspecie(),
                animal.getIdade(), animal.getTutor().getId());
    }
}
