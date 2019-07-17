package entities;

import db.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para fazer consultas na tabela Apuracao
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class ApuracaoDAO {

    /**
     * Método para adicionar uma linha na tabela Apuracao
     *
     * @param re = Um objeto "Apuracao" que será adicionado na tabela
     * @throws SQLException
     */
    public static void adiciona(Apuracao re) {
        Connection conexao = ConnectionFactory.getDBConnection();
        String sql = "INSERT INTO Apuracao (idResposta, idQuestao, idEleicao, qtdVotos) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, re.getIdResposta());
            stmt.setInt(2, re.getIdQuestao());
            stmt.setInt(3, re.getIdEleicao());
            stmt.setInt(4, re.getQtdVotos());

            stmt.execute();


        } catch (SQLException ex) {

        }

    }

    /**
     * Método para verificar as respostas vencedoras da eleição
     * que e que não são nulos ou brancos
     *
     * @param idEleicao = Identificação da eleição
     * @param idQuestao = Identificação da eleição
     * @param idNulo    = Identificação do voto nulo
     * @param idBranco  = Identificação do voto branco
     * @return = Lista de respostas vencedoras
     * @throws SQLException
     */
    public static List<Resposta> vencedor(int idEleicao, int idQuestao, int idNulo, int idBranco) {
        Connection conexao = ConnectionFactory.getDBConnection();
        List<Resposta> vencedores = new ArrayList<>();


        String sql = "SELECT * from Apuracao where qtdVotos in (select max(qtdVotos) from Apuracao where idEleicao =" + idEleicao + " and " +
                "idQuestao =" + idQuestao + " and idResposta!=" + idNulo + " and idResposta!= " + idBranco + ") and idEleicao =" + idEleicao + " and idQuestao =" + idQuestao + "" +
                " and idResposta!= " + idBranco + " and idResposta!= " + idNulo + ";";

        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Apuracao ap = new Apuracao(
                        rs.getInt("idApuracao"),
                        rs.getInt("idResposta"),
                        rs.getInt("idQuestao"),
                        rs.getInt("idEleicao"),
                        rs.getInt("qtdVotos"));


                Resposta re = RespostaDAO.titulo(ap.getIdResposta());
                vencedores.add(re);

            }

        } catch (SQLException ex) {

        }
        return vencedores;
    }


    /**
     * Método para verificar o resultado de uma eleição
     *
     * @param idEleicao = Identificação da eleição
     * @return = Lista com o resultado da eleição
     * @throws SQLException
     */
    public static List<Apuracao> resultadoEleicao(int idEleicao) {
        Connection conexao = ConnectionFactory.getDBConnection();
        List<Apuracao> apuradas = new ArrayList<>();

        String sql = "SELECT * from Apuracao where idEleicao =" + idEleicao + " order by idQuestao;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Apuracao ap = new Apuracao(
                        rs.getInt("idApuracao"),
                        rs.getInt("idResposta"),
                        rs.getInt("idQuestao"),
                        rs.getInt("idEleicao"),
                        rs.getInt("qtdVotos"));

                apuradas.add(ap);

            }
        } catch (SQLException ex) {


        }

        return apuradas;
    }


}
