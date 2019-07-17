package entities;

import db.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe para fazer consultas na tabela Resposta
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class RespostaDAO {


    /**
     * Método para adicionar uma linha na tabela Resposta
     *
     * @param r = Um objeto "Resposta" que será adicionado na tabela
     * @return = Sucesso ou falha na inserção
     * @throws SQLException
     */
    public static boolean adiciona(Resposta r) {
        Connection conexao = ConnectionFactory.getDBConnection();
        boolean flag = false;
        String sql = "INSERT INTO Resposta (idQuestao, idEleicao, TituloResposta) VALUES (?,?,?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, r.getIdQuestao());
            stmt.setInt(2, r.getIdEleicao());
            stmt.setString(3, r.getTituloResposta());
            stmt.execute();
            flag = true;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            flag = false;
        }

        return flag;
    }

    /**
     * Método para listar as respostas de uma questão
     *
     * @param idQuestao = Identificação da questão
     * @return = Lista com as respostas da questão
     * @throws SQLException
     */
    public static List<Resposta> listarRespostas(int idQuestao) {
        Connection conexao = ConnectionFactory.getDBConnection();
        List<Resposta> respostas = new ArrayList<>();

        String sql = "SELECT idResposta, TituloResposta from Resposta where idQuestao=" + idQuestao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Resposta r = new Resposta();
                r.setIdResposta(rs.getInt("idResposta"));
                r.setTituloResposta(rs.getString("TituloResposta"));
                respostas.add(r);
            }
        } catch (SQLException ex) {

        }


        return respostas;

    }

    /**
     * Método para recuperar o título de uma resposta
     *
     * @param idResposta = Identificação da resposta
     * @return = Objeto "Resposta" com os atributos necessários
     * @throws SQLException
     */
    public static Resposta titulo(int idResposta) {
        Connection conexao = ConnectionFactory.getDBConnection();

        Resposta r = new Resposta();
        String sql = "SELECT idResposta, TituloResposta from Resposta where idResposta=" + idResposta + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {

                r.setIdResposta(rs.getInt("idResposta"));
                r.setTituloResposta(rs.getString("TituloResposta"));

            }
        } catch (SQLException ex) {

        }

        return r;

    }


}
