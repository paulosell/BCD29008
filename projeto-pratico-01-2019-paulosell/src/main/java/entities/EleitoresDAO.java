package entities;

import db.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe para fazer consultas na tabela Eleitores
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class EleitoresDAO {

    /**
     * Método para adicionar uma linha na tabela Eleitores
     *
     * @param el = Um objeto "Eleitores" que será adicionado na tabela
     * @throws SQLException
     */
    public static void adiciona(Eleitores el) throws SQLException {
        Connection conexao = ConnectionFactory.getDBConnection();

        String sql = "INSERT INTO Eleitores(loginPessoa, idEleicao, Votou) VALUES (?,?,?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, el.getLoginPessoa());
            stmt.setInt(2, el.getIdEleicao());
            stmt.setBoolean(3, el.isVotou());
            stmt.execute();
        } catch (SQLException ex) {

        }

    }

    /**
     * Método para verificar se uma eleição possui eleitores cadastrados
     *
     * @param idEleicao = Identificação da eleição
     * @return = Verdadeiro se a eleição tem eleitores e falso se a eleição não tem eleitores
     * @throws SQLException
     */
    public static boolean eleicaoTemEleitor(int idEleicao) throws SQLException {
        Connection conexao = ConnectionFactory.getDBConnection();
        List<Eleitores> eleitores = new ArrayList<>();
        boolean flag = false;
        String sql = "SELECT * FROM Eleitores where idEleicao =" + idEleicao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleitores el = new Eleitores(
                        rs.getString("loginPessoa"),
                        rs.getInt("idEleicao"),
                        rs.getBoolean("Votou"));

                eleitores.add(el);
            }
        } catch (SQLException ex) {


        }

        if (eleitores.size() > 0) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }


    /**
     * Método para indicar que o eleitor votou em uma eleição
     *
     * @param login     = Login do eleitor
     * @param idEleicao = Identificação da eleição
     * @throws SQLException
     */
    public static void votou(String login, int idEleicao) throws SQLException {
        Connection conexao = ConnectionFactory.getDBConnection();

        String sql = "UPDATE Eleitores set Votou = true where loginPessoa = '" + login + "' and idEleicao = " + idEleicao + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException ex) {

        }


    }

    /**
     * Método para listar todos os eleitores criados
     *
     * @return = Lista com todos os eleitores
     * @throws SQLException
     */
    public static List<Eleitores> listarTodos() throws SQLException {
        List<Eleitores> eleitores = new ArrayList<>();
        Connection conexao = ConnectionFactory.getDBConnection();

        String sql = "SELECT * from Eleitores";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleitores el = new Eleitores(
                        rs.getString("loginPessoa"),
                        rs.getInt("idEleicao"),
                        rs.getBoolean("Votou"));

                eleitores.add(el);
            }
        } catch (SQLException ex) {


        }

        return eleitores;
    }
}
