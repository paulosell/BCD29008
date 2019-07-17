package entities;

import db.ConnectionFactory;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe para fazer consultas na tabela Eleicao
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class EleicaoDAO {

    /**
     * Método para adicionar uma linha na tabela Eleicao
     *
     * @param e = Um objeto "Eleicao" que será adicionado na tabela
     * @return = Sucesso ou falha na inserção
     * @throws SQLException
     */
    public static int adiciona(Eleicao e) {
        Connection conexao = ConnectionFactory.getDBConnection();
        int ret = 0;

        String sql = "INSERT INTO Eleicao (TituloEleicao, InicioEleicao, EstadoEleicao) VALUES (?,?,?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, e.getTituloEleicao());
            stmt.setString(2, e.getInicioEleicao());
            stmt.setBoolean(3, e.isEstadoEleicao());
            stmt.execute();

            ret = 1;

        } catch (SQLException ex) {


            ret = 2;
        }

        return ret;
    }


    /**
     * Método para fechar uma eleição
     *
     * @param id = Identificação da eleição
     * @return = Sucesso ou falha no encerramento da eleição
     * @throws SQLException
     */
    public static boolean fechaEleicao(int id) {
        Connection conexao = ConnectionFactory.getDBConnection();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        int flag = 0;
        String sql = "UPDATE Eleicao SET EstadoEleicao = FALSE where idEleicao = " + id + " and FimEleicao is null; ";


        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.execute();
            flag = flag + 1;
        } catch (SQLException ex) {
            flag = flag - 1;


        }

        String query = "UPDATE Eleicao set FimEleicao = '" + dateFormat.format(date) + "' where idEleicao =" + id + ";";

        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.execute();
            flag = flag + 1;


        } catch (SQLException ex) {
            flag = flag - 1;


        }

        return (flag == 2);
    }

    /**
     * Método para verificar as eleições fechadas e que não foram abertas ainda
     *
     * @return = Lista de eleições fechadas
     * @throws SQLException
     */
    public static List<Eleicao> listarEleicoesFechadas() {
        Connection conexao = ConnectionFactory.getDBConnection();

        List<Eleicao> eleicoes = new ArrayList<>();
        String sql = "SELECT idEleicao, TituloEleicao FROM Eleicao where EstadoEleicao = FALSE and FimEleicao is null;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleicao e = new Eleicao(
                        rs.getInt("idEleicao"),
                        rs.getString("TituloEleicao"));
                eleicoes.add(e);
            }


        } catch (SQLException ex) {


        }

        return eleicoes;
    }

    /**
     * Método para abrir uma eleição
     *
     * @param id = Identificação da eleição
     * @return = Sucesso ou falha na abertura da eleição
     * @throws SQLException
     */
    public static boolean abreEleicao(int id) {
        Connection conexao = ConnectionFactory.getDBConnection();
        boolean flag = false;
        String sql = "UPDATE Eleicao SET EstadoEleicao = TRUE where idEleicao = " + id + " and FimEleicao is null;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.execute();


            flag = true;
        } catch (SQLException ex) {


            flag = false;
        }

        return flag;
    }

    /**
     * Método para alterar o estado de apuração de uma eleição
     *
     * @param id = Identificação da eleição
     * @return = Sucesso ou falha na alteração
     * @throws SQLException
     */
    public static boolean apurada(int id)  {
        Connection conexao = ConnectionFactory.getDBConnection();
        boolean flag = false;
        String sql = "UPDATE Eleicao SET ApuradaEleicao = TRUE where idEleicao = " + id + ";";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.execute();


            flag = true;
        } catch (SQLException ex) {


            flag = false;
        }


        return flag;
    }

    /**
     * Método para verificar eleições que um usuário pode votar
     *
     * @param lista = String com as identificações das eleições
     * @return = Lista com as eleições que o usuário pode votar
     * @throws SQLException
     */
    public static List<Eleicao> listarTodasPossiveisEleicoes(String lista) {
        Connection conexao = ConnectionFactory.getDBConnection();
        List<Eleicao> eleicoes = new ArrayList<>();
        lista = lista.substring(1, lista.length() - 1);


        String sql = "SELECT * from Eleicao where idEleicao in(" + lista + ") and EstadoEleicao = 1;";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleicao e = new Eleicao(
                        rs.getInt("idEleicao"),
                        rs.getString("TituloEleicao"),
                        rs.getString("InicioEleicao"),
                        rs.getString("FimEleicao"),
                        rs.getBoolean("EstadoEleicao"),
                        rs.getBoolean("ApuradaEleicao"));

                eleicoes.add(e);
            }

        } catch (SQLException ex) {

        }

        return eleicoes;
    }

    /**
     * Método para verificar as eleições que já foram apuradas
     *
     * @return = Lista com as eleições apuradas
     * @throws SQLException
     */
    public static List<Eleicao> eleicoesApuradas() {
        Connection conexao = ConnectionFactory.getDBConnection();


        List<Eleicao> eleicoes = new ArrayList<>();

        String sql = "SELECT * from Eleicao where ApuradaEleicao = true ";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleicao e = new Eleicao(
                        rs.getInt("idEleicao"),
                        rs.getString("TituloEleicao"),
                        rs.getString("InicioEleicao"),
                        rs.getString("FimEleicao"),
                        rs.getBoolean("EstadoEleicao"),
                        rs.getBoolean("ApuradaEleicao"));

                eleicoes.add(e);
            }


        } catch (SQLException ex) {


        }

        return eleicoes;
    }

    /**
     * Método para listar todas as eleições criadas
     *
     * @return = Lista com as eleições criadas
     * @throws SQLException
     */
    public static List<Eleicao> listarTodos() {
        Connection conexao = ConnectionFactory.getDBConnection();


        List<Eleicao> eleicoes = new ArrayList<>();

        String sql = "SELECT * from Eleicao";
        try (PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleicao e = new Eleicao(
                        rs.getInt("idEleicao"),
                        rs.getString("TituloEleicao"),
                        rs.getString("InicioEleicao"),
                        rs.getString("FimEleicao"),
                        rs.getBoolean("EstadoEleicao"),
                        rs.getBoolean("ApuradaEleicao"));

                eleicoes.add(e);
            }


        } catch (SQLException ex) {


        }

        return eleicoes;
    }

}
