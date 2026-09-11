package br.edu.iftm.petvida.repository;

import br.edu.iftm.petvida.model.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TutorRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<Tutor> tutorMapper = (rs, rowNum) -> new Tutor(
            rs.getInt("id_tutor"),
            rs.getString("nome"),
            rs.getString("telefone")
    );

    // Metodo extra (nao exigido explicitamente na tabela da prova, mas necessario
    // para a rota /tutor_NN exibir nome e telefone do tutor).
    public Tutor buscarPorId(int id) {
        String sql = "SELECT id_tutor, nome, telefone FROM tutor WHERE id_tutor = ?";
        return jdbc.queryForObject(sql, tutorMapper, id);
    }

    // Grava o tutor no banco. INSERT com jdbc.update e parametros (?),
    // usando o id que vem no objeto (nao ha geracao automatica de chave nesta versao).
    public void salvar(Tutor tutor) {
        String sql = "INSERT INTO tutor (id_tutor, nome, telefone) VALUES (?, ?, ?)";
        jdbc.update(sql, tutor.getId(), tutor.getNome(), tutor.getTelefone());
    }
}
