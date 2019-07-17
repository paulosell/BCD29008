package entities;

import db.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe para fazer consultas na tabela Pessoa
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class PessoaDAO {


    /**
     * Método para listar todos as Pessoas
     *
     * @return = Lista com todas as pessoas
     * @throws SQLException
     */
    public static List<Pessoa> listarTodos() {
        Connection conexao = ConnectionFactory.getDBConnection();
        List<Pessoa> pessoas = new ArrayList<>();


        String sql = "SELECT * from Pessoa";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pessoa c = new Pessoa(
                        rs.getString("NomePessoa"),
                        rs.getString("loginPessoa"),
                        rs.getString("senhaPessoa"));
                pessoas.add(c);
            }
        } catch (SQLException ex) {

        }

        return pessoas;
    }

}
