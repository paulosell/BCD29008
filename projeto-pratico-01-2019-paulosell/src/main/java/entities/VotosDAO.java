package entities;

import db.ConnectionFactory;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para fazer consultas na tabela Votos
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class VotosDAO {


    /**
     * Método para adicionar uma linha na tabela Votos
     *
     * @param v = Um objeto "Votos" que será adicionado na tabela
     * @throws SQLException
     */
    public static void adiciona(Votos v) {
        Connection conexao = ConnectionFactory.getDBConnection();
        String sql = "INSERT INTO Votos (idResposta, idQuestao, idEleicao) VALUES (?,?,?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, v.getIdResposta());
            stmt.setInt(2, v.getIdQuestao());
            stmt.setInt(3, v.getIdEleicao());
            stmt.execute();
        } catch (SQLException ex) {

        }

    }

    /**
     * Método para apurar os votos de uma eleição
     *
     * @param idEleicao = Identificação da eleição
     * @param idQuestao = Identificação da questão
     * @return = Lista com os votos apurados
     * @throws SQLException
     */
    public static List<Votos> apurarVotos(int idEleicao, int idQuestao) {
        Connection conexao = ConnectionFactory.getDBConnection();
        List<Votos> votosApurados = new ArrayList<>();

        String sql = "SELECT idResposta, idQuestao, idEleicao, count(idResposta) as qtdVotos from Votos where " +
                "idEleicao = " + idEleicao + " and idQuestao = " + idQuestao + " group by idResposta order by qtdVotos;";

        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Votos v = new Votos(
                        rs.getInt("idResposta"),
                        rs.getInt("idQuestao"),
                        rs.getInt("idEleicao"),
                        rs.getInt("qtdVotos"));

                votosApurados.add(v);
            }
        } catch (SQLException ex) {


        }


        return votosApurados;
    }

}
