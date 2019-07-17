package entities;

import db.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para fazer consultas na tabela Questao
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class QuestaoDAO {

    /**
     * Método para adicionar uma linha na tabela Questao
     *
     * @param q = Um objeto "Questao" que será adicionado na tabela
     * @return = Sucesso ou falha na inserção
     * @throws SQLException
     */
    public static boolean adiciona(Questao q) {
        Connection conexao = ConnectionFactory.getDBConnection();
        boolean flag = false;

        String sql = "INSERT INTO Questao (idEleicao, respostasMinimasQuestao, respostasMaximasQuestao, TituloQuestao)" +
                " VALUES (?,?,?,?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, q.getIdEleicao());
            stmt.setInt(2, q.getRespostasMinimasQuestao());
            stmt.setInt(3, q.getRespostasMaximasQuestao());
            stmt.setString(4, q.getTituloQuestao());
            stmt.execute();
            flag = true;
        } catch (SQLException ex) {

            flag = false;
        }

        return flag;

    }

    /**
     * Método para retorar apenas uma questão
     *
     * @param idEleicao = Identificação da eleição
     * @param idQuestao = Identificação da questão
     * @return = Objeto "Questão" com os atributos necessários
     * @throws SQLException
     */
    public static Questao listarQuestao(int idEleicao, int idQuestao) {
        Connection conexao = ConnectionFactory.getDBConnection();
        Questao q = new Questao();

        String sql = "SELECT idQuestao, TituloQuestao, respostasMaximasQuestao, respostasMinimasQuestao from Questao where idEleicao = " + idEleicao + " and " +
                "idQuestao = " + idQuestao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {

                q.setIdQuestao(rs.getInt("idQuestao"));
                q.setRespostasMaximasQuestao(rs.getInt("respostasMaximasQuestao"));
                q.setRespostasMinimasQuestao(rs.getInt("respostasMinimasQuestao"));
                q.setTituloQuestao(rs.getString("TituloQuestao"));

            }
        } catch (SQLException ex) {


        }

        return q;

    }

    /**
     * Método para listar todas as questões de uma eleição
     *
     * @param idEleicao = Identificação da eleição
     * @return = Lista de questões da eleiçaõ
     * @throws SQLException
     */
    public static List<Questao> listarQuestoes(int idEleicao) {
        List<Questao> questoes = new ArrayList<>();
        Connection conexao = ConnectionFactory.getDBConnection();
        String sql = "SELECT idQuestao, TituloQuestao, respostasMaximasQuestao, respostasMinimasQuestao from Questao where idEleicao = " + idEleicao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Questao q = new Questao();
                q.setIdQuestao(rs.getInt("idQuestao"));
                q.setRespostasMaximasQuestao(rs.getInt("respostasMaximasQuestao"));
                q.setRespostasMinimasQuestao(rs.getInt("respostasMinimasQuestao"));
                q.setTituloQuestao(rs.getString("TituloQuestao"));
                questoes.add(q);
            }
        } catch (SQLException ex) {


        }

        return questoes;

    }

}