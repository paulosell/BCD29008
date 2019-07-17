import entities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Principal {

    static Scanner in = new Scanner(System.in);

    /**
     * Método para verificar o resultado de uma eleição
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void resultados() throws InterruptedException, SQLException {
        List<Eleicao> eleicoesApuradas = EleicaoDAO.eleicoesApuradas();
        if (eleicoesApuradas.size() > 0) {
            List<Eleicao> eleicoes = eleicoesApuradas;
            boolean passou = true;
            Map<Integer, Integer> idParaEleicao = new HashMap<Integer, Integer>();
            Map<Integer, Integer> EleicaoParaId = new HashMap<Integer, Integer>();

            for (int i = 0; i < eleicoes.size(); i++) {
                idParaEleicao.put(eleicoes.get(i).getIdEleicao(), i + 1);
                EleicaoParaId.put(i + 1, eleicoes.get(i).getIdEleicao());

            }
            int sair = eleicoes.size() + 1;
            System.out.println("###############################################################");
            System.out.println("#          Escolha a eleição para conferir o resultado:       #");
            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

            for (int i = 0; i < eleicoes.size(); i++) {
                System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());

            }
            System.out.println("    (" + sair + ") - " + "Sair");
            System.out.println("#                                                             #");
            String idEleicaoString = " ";
            while (passou) {
                try {
                    idEleicaoString = in.nextLine();
                    Integer.parseInt(idEleicaoString);
                    if (Integer.parseInt(idEleicaoString) <= eleicoes.size() + 1 && Integer.parseInt(idEleicaoString) > 0) {
                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número da lista abaixo        #");
                        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                        for (int i = 0; i < eleicoes.size(); i++) {
                            System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                        }
                        System.out.println("    (" + sair + ") - " + "Sair");
                        System.out.println("#                                                             #");
                        passou = true;
                    }


                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número da lista abaixo        #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                    for (int i = 0; i < eleicoes.size(); i++) {
                        System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                    passou = true;
                }

            }

            int idEleicao = Integer.parseInt(idEleicaoString);
            if (idEleicao == sair) return;
            idEleicao = EleicaoParaId.get(idEleicao);

            List<Apuracao> resultado = ApuracaoDAO.resultadoEleicao(idEleicao);
            List<Questao> questoes = QuestaoDAO.listarQuestoes(idEleicao);


            for (int i = 0; i < questoes.size(); i++) {
                List<Resposta> respostas = RespostaDAO.listarRespostas(questoes.get(i).getIdQuestao());

                List<Resposta> vencedores = ApuracaoDAO.vencedor(idEleicao, questoes.get(i).getIdQuestao(), respostas.get(0).getIdResposta(), respostas.get(1).getIdResposta());
                System.out.println();
                System.out.println("###############################################################");
                System.out.println("         " + (i + 1) + " - " + questoes.get(i).getTituloQuestao());
                System.out.println("#                                                             #");
                for (int j = 0; j < respostas.size(); j++) {
                    System.out.print("   " + (j + 1) + " - " + respostas.get(j).getTituloResposta());
                    boolean flag = false;

                    for (int k = 0; k < resultado.size(); k++) {
                        if (respostas.get(j).getIdResposta() == resultado.get(k).getIdResposta()) {
                            System.out.println(" " + resultado.get(k).getQtdVotos());
                            flag = true;
                        }
                    }
                    if (!flag) System.out.println("   0");

                }
                if (vencedores.size() == 0) {
                    System.out.println("# >>>              Nenhum voto válido na questão          <<< #");
                    System.out.println("###############################################################");

                } else if (vencedores.size() == 1) {
                    System.out.println(" >>>        Alternativa vencedora: " + vencedores.get(0).getTituloResposta());
                    System.out.println("#                                                             #");
                    System.out.println("###############################################################");
                } else {
                    System.out.print(" >>>        Empate entre as opções:");
                    for (int h = 0; h < vencedores.size(); h++) {
                        System.out.print(vencedores.get(h).getTituloResposta() + " ");

                    }
                    System.out.println("#                                                             #");
                    System.out.println("###############################################################");
                }
                Thread.sleep(2000);
            }
            clearConsole(6);
        } else {
            System.out.println("#####################################################################################");
            System.out.println("#                             Nenhuma eleição apurada!!!                            #");
            System.out.println("#####################################################################################");
            Thread.sleep(1000);
            clearConsole(6);
        }
    }

    /**
     * Método para apurar uma eleição
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void apurar() throws InterruptedException, SQLException {
        List<Eleicao> eleicoes = EleicaoDAO.listarTodos();
        List<Eleicao> eleicoesParaApurar = new ArrayList<>();
        for (int i = 0; i < eleicoes.size(); i++) {
            if (!(eleicoes.get(i).getFimEleicao() == null) && !eleicoes.get(i).isEstadoEleicao() && !eleicoes.get(i).isApuradaEleicao()) {
                eleicoesParaApurar.add(eleicoes.get(i));
            }
        }

        if (eleicoesParaApurar.size() > 0) {
            eleicoes = eleicoesParaApurar;

            boolean passou = true;
            Map<Integer, Integer> idParaEleicao = new HashMap<Integer, Integer>();
            Map<Integer, Integer> EleicaoParaId = new HashMap<Integer, Integer>();

            for (int i = 0; i < eleicoes.size(); i++) {
                idParaEleicao.put(eleicoes.get(i).getIdEleicao(), i + 1);
                EleicaoParaId.put(i + 1, eleicoes.get(i).getIdEleicao());

            }
            int sair = eleicoes.size() + 1;
            System.out.println("###############################################################");
            System.out.println("#                Escolha a eleição para apurar:               #");
            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

            for (int i = 0; i < eleicoes.size(); i++) {
                System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());

            }
            System.out.println("    (" + sair + ") - " + "Sair");
            System.out.println("#                                                             #");
            String idEleicaoString = " ";
            while (passou) {
                try {
                    idEleicaoString = in.nextLine();
                    Integer.parseInt(idEleicaoString);
                    if (Integer.parseInt(idEleicaoString) <= eleicoes.size() + 1 && Integer.parseInt(idEleicaoString) > 0) {
                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número da lista abaixo        #");
                        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                        for (int i = 0; i < eleicoes.size(); i++) {
                            System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                        }
                        System.out.println("    (" + sair + ") - " + "Sair");
                        System.out.println("#                                                             #");
                        passou = true;
                    }


                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número da lista abaixo        #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                    for (int i = 0; i < eleicoes.size(); i++) {
                        System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                    passou = true;
                }

            }


            int idEleicao = Integer.parseInt(idEleicaoString);
            if (idEleicao == sair) return;
            idEleicao = EleicaoParaId.get(idEleicao);
            List<Questao> questoes = QuestaoDAO.listarQuestoes(idEleicao);
            for (int i = 0; i < questoes.size(); i++) {
                List<Votos> votosapurados = VotosDAO.apurarVotos(idEleicao, questoes.get(i).getIdQuestao());

                for (int j = 0; j < votosapurados.size(); j++) {
                    Apuracao re = new Apuracao(votosapurados.get(j).getIdResposta(), questoes.get(i).getIdQuestao(), idEleicao, votosapurados.get(j).getIdVotos());
                    ApuracaoDAO.adiciona((re));

                }

            }
            EleicaoDAO.apurada(idEleicao);

            System.out.println("#####################################################################################");
            System.out.println("#                             Eleição apurada com sucesso!!                         #");
            System.out.println("#####################################################################################");
            Thread.sleep(1000);
            clearConsole(6);


        } else {
            System.out.println("#####################################################################################");
            System.out.println("#                        Nenhuma eleição para apurar no momento                      #");
            System.out.println("#####################################################################################");
            Thread.sleep(1000);
            clearConsole(6);
        }

    }

    /**
     * Método para fechar uma eleição
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void fecharEleicao() throws InterruptedException, SQLException {
        // Claramente não precisava ter duas listas aqui.

        List<Eleicao> eleicoes = EleicaoDAO.listarTodos(); // Dava pra listar as eleições com de acordo com as
        List<Eleicao> eleicoesAbertas = new ArrayList<>(); // Comparações abaixo


        for (int i = 0; i < eleicoes.size(); i++) {
            if (eleicoes.get(i).isEstadoEleicao()) {
                eleicoesAbertas.add(eleicoes.get(i));
            }
        }
        if (eleicoesAbertas.size() > 0) {
            eleicoes = eleicoesAbertas;

            boolean passou = true;
            Map<Integer, Integer> idParaEleicao = new HashMap<Integer, Integer>();
            Map<Integer, Integer> EleicaoParaId = new HashMap<Integer, Integer>();

            for (int i = 0; i < eleicoes.size(); i++) {
                idParaEleicao.put(eleicoes.get(i).getIdEleicao(), i + 1);
                EleicaoParaId.put(i + 1, eleicoes.get(i).getIdEleicao());

            }
            int sair = eleicoes.size() + 1;
            System.out.println("###############################################################");
            System.out.println("#                Escolha a eleição para fechar:               #");
            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

            for (int i = 0; i < eleicoes.size(); i++) {
                System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());

            }
            System.out.println("    (" + sair + ") - " + "Sair");
            System.out.println("#                                                             #");
            String idEleicaoString = " ";
            while (passou) {
                try {
                    idEleicaoString = in.nextLine();
                    Integer.parseInt(idEleicaoString);
                    if (Integer.parseInt(idEleicaoString) <= eleicoes.size() + 1 && Integer.parseInt(idEleicaoString) > 0) {
                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número da lista abaixo        #");
                        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                        for (int i = 0; i < eleicoes.size(); i++) {
                            System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                        }
                        System.out.println("    (" + sair + ") - " + "Sair");
                        System.out.println("#                                                             #");
                        passou = true;
                    }


                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número da lista abaixo        #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                    for (int i = 0; i < eleicoes.size(); i++) {
                        System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                    passou = true;
                }

            }

            int id = Integer.parseInt(idEleicaoString);
            if (id == sair) return;
            id = EleicaoParaId.get(id);

            if (EleicaoDAO.fechaEleicao(id)) {
                System.out.println("#####################################################################################");
                System.out.println("#                             Eleição fechada com sucesso!!                         #");
                System.out.println("#####################################################################################");
                Thread.sleep(1000);
                clearConsole(6);
            } else {
                System.out.println("#####################################################################################");
                System.out.println("#                                Erro ao fechar a eleição!!                         #");
                System.out.println("#####################################################################################");
                Thread.sleep(1000);
                clearConsole(6);
            }
        } else {
            System.out.println("#####################################################################################");
            System.out.println("#                        Nenhuma eleição aberta para ser fechada                    #");
            System.out.println("#####################################################################################");
            Thread.sleep(1000);
            clearConsole(6);
        }
    }

    /**
     * Método para abrir uma eleição
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void abrirEleicao() throws InterruptedException, SQLException {
        // Claramente não precisava ter duas listas aqui.

        List<Eleicao> eleicoes = EleicaoDAO.listarTodos();   // Dava pra listar as eleições com de acordo com as
        List<Eleicao> eleicoesFechadas = new ArrayList<>();  // Comparações abaixo

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date();
        String hoje = dateFormat.format((data));


        for (int i = 0; i < eleicoes.size(); i++) {
            if (eleicoes.get(i).getInicioEleicao().matches(hoje)) {
                if (!eleicoes.get(i).isEstadoEleicao() && eleicoes.get(i).getFimEleicao() == null) {
                    eleicoesFechadas.add(eleicoes.get(i));
                }
            }
        }

        if (eleicoesFechadas.size() > 0) {
            for (int i = 0; i < eleicoesFechadas.size(); i++) {
                int idEleicao = eleicoesFechadas.get(i).getIdEleicao();
                List<Questao> questoes = QuestaoDAO.listarQuestoes(idEleicao);
                if (questoes.size() > 0) {
                    for (int h = 0; h < questoes.size(); h++) {
                        int idQuestao = questoes.get(h).getIdQuestao();
                        Questao q = QuestaoDAO.listarQuestao(idEleicao, idQuestao);
                        List<Resposta> respostas = RespostaDAO.listarRespostas(idQuestao);

                        if (!(q.getRespostasMaximasQuestao() <= respostas.size() - 2)) {
                            eleicoesFechadas.remove(i);
                            break;
                        }
                    }
                } else {
                    eleicoesFechadas.remove(i);
                }

            }
        } else {
            System.out.println("#####################################################################################");
            System.out.println("#  As eleições criadas não possuem o número correto de respostas para as perguntas  #");
            System.out.println("#####################################################################################");
            Thread.sleep(1000);
            clearConsole(6);
            return;
        }

        if (eleicoesFechadas.size() > 0) {
            eleicoes = eleicoesFechadas;
            boolean passou = true;
            Map<Integer, Integer> idParaEleicao = new HashMap<Integer, Integer>();
            Map<Integer, Integer> EleicaoParaId = new HashMap<Integer, Integer>();

            for (int i = 0; i < eleicoes.size(); i++) {
                idParaEleicao.put(eleicoes.get(i).getIdEleicao(), i + 1);
                EleicaoParaId.put(i + 1, eleicoes.get(i).getIdEleicao());

            }
            int sair = eleicoes.size() + 1;
            System.out.println("###############################################################");
            System.out.println("#               Escolha a eleição para ser aberta:            #");
            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

            for (int i = 0; i < eleicoes.size(); i++) {
                System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());

            }
            System.out.println("    (" + sair + ") - " + "Sair");
            System.out.println("#                                                             #");
            String idEleicaoString = " ";
            while (passou) {
                try {
                    idEleicaoString = in.nextLine();
                    Integer.parseInt(idEleicaoString);
                    if (Integer.parseInt(idEleicaoString) <= eleicoes.size() + 1 && Integer.parseInt(idEleicaoString) > 0) {
                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número da lista abaixo        #");
                        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                        for (int i = 0; i < eleicoes.size(); i++) {
                            System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                        }
                        System.out.println("    (" + sair + ") - " + "Sair");
                        System.out.println("#                                                             #");
                        passou = true;
                    }


                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número da lista abaixo        #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                    for (int i = 0; i < eleicoes.size(); i++) {
                        System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                    passou = true;
                }

            }


            int id = Integer.parseInt(idEleicaoString);
            if (id == sair) return;
            id = EleicaoParaId.get(id);

            if (EleitoresDAO.eleicaoTemEleitor(id)) {
                if (EleicaoDAO.abreEleicao(id)) {
                    System.out.println("#####################################################################################");
                    System.out.println("#                                Eleição aberta                                     #");
                    System.out.println("#####################################################################################");
                    Thread.sleep(1000);
                    clearConsole(6);
                } else {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número da lista abaixo        #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                    for (int i = 0; i < eleicoes.size(); i++) {
                        System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                }
            } else {
                System.out.println("#####################################################################################");
                System.out.println("#                    Você deve adicionar eleitores na eleição!                      #");
                System.out.println("#####################################################################################");
                Thread.sleep(2000);
                clearConsole(6);
                ;
            }
        } else {
            System.out.println("#####################################################################################");
            System.out.println("#                          Nenhuma eleição pronta para abrir!!                      #");
            System.out.println("#####################################################################################");
            Thread.sleep(1000);
            clearConsole(6);
            ;

        }

    }

    /**
     * Método para listar as eleições criadas e que não estão aptas para abertura
     *
     * @throws InterruptedException
     * @throws SQLException
     */

    public static void mostrarEleicoes() throws InterruptedException, SQLException {
        // Claramente não precisava ter duas listas aqui.

        List<Eleicao> eleicoes = EleicaoDAO.listarTodos();   // Dava pra listar as eleições com de acordo com as


        if (eleicoes.size() > 0) {


            Map<Integer, Integer> idParaEleicao = new HashMap<Integer, Integer>();
            Map<Integer, Integer> EleicaoParaId = new HashMap<Integer, Integer>();

            for (int i = 0; i < eleicoes.size(); i++) {
                idParaEleicao.put(eleicoes.get(i).getIdEleicao(), i + 1);
                EleicaoParaId.put(i + 1, eleicoes.get(i).getIdEleicao());

            }

            System.out.println("###############################################################");
            System.out.println("#                    Todas eleições já criadas                #");
            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

            for (int i = 0; i < eleicoes.size(); i++) {
                System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());

            }

            System.out.println("#                                                             #");
            Thread.sleep(1000);
            clearConsole(6);

        } else {
            System.out.println("#####################################################################################");
            System.out.println("#                               Nenhuma eleição criada                              #");
            System.out.println("#####################################################################################");
            Thread.sleep(1000);
            clearConsole(6);

        }
    }

    /**
     * Método para criar uma eleição
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void criarEleicao() throws InterruptedException, SQLException {


        System.out.println("###############################################################");
        System.out.println("#             Dê um título à eleição:                         #");


        String titulo = in.nextLine();
        System.out.println("###############################################################");
        System.out.println("#         Informe a data da sua eleição (ano-mes-dia)         #");


        String inicio = in.nextLine();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date hoje = new Date();


        if (titulo.trim().isEmpty() || titulo == null) {
            System.out.println("###############################################################");
            System.out.println("#            O título da eleição não pode ser vazio           #");
            System.out.println("###############################################################");
            Thread.sleep(1000);
            clearConsole(6);
            return;

        }
        if (inicio.compareTo(dateFormat.format(hoje)) < 0) {
            System.out.println("###############################################################");
            System.out.println("#            Você não pode criar eleição no passado           #");
            System.out.println("###############################################################");
            Thread.sleep(1000);
            clearConsole(6);
            return;
        }
        Eleicao e = new Eleicao(titulo, inicio, false);
        int ret = EleicaoDAO.adiciona(e);


        if (ret == 1) {
            System.out.println("###############################################################");
            System.out.println("#                  Eleição criada com sucesso                 #");
            System.out.println("###############################################################");
            Thread.sleep(1000);
            clearConsole(6);
        } else if (ret == 2) {
            System.out.println("###############################################################");
            System.out.println("#       Eleição já existente ou formato de data errado        #");
            System.out.println("###############################################################");
            Thread.sleep(1000);
            clearConsole(6);
        }


    }

    /**
     * Método para criar uma questão
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void criarQuestao() throws InterruptedException, SQLException {
        boolean passou = true;
        List<Eleicao> eleicoes = EleicaoDAO.listarEleicoesFechadas();
        Map<Integer, Integer> idParaEleicao = new HashMap<Integer, Integer>();
        Map<Integer, Integer> EleicaoParaId = new HashMap<Integer, Integer>();

        if (eleicoes.size() > 0) {
            for (int i = 0; i < eleicoes.size(); i++) {
                idParaEleicao.put(eleicoes.get(i).getIdEleicao(), i + 1);
                EleicaoParaId.put(i + 1, eleicoes.get(i).getIdEleicao());

            }
            int sair = eleicoes.size() + 1;
            System.out.println("###############################################################");
            System.out.println("#          Escolha a eleição para adicionar a questão:        #");
            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

            for (int i = 0; i < eleicoes.size(); i++) {
                System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());

            }
            System.out.println("    (" + sair + ") - " + "Sair");
            System.out.println("#                                                             #");
            String idEleicaoString = " ";
            while (passou) {
                try {
                    idEleicaoString = in.nextLine();
                    Integer.parseInt(idEleicaoString);
                    if (Integer.parseInt(idEleicaoString) <= eleicoes.size() + 1 && Integer.parseInt(idEleicaoString) > 0) {
                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número da lista abaixo        #");
                        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                        for (int i = 0; i < eleicoes.size(); i++) {
                            System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                        }
                        System.out.println("    (" + sair + ") - " + "Sair");
                        System.out.println("#                                                             #");
                        passou = true;
                    }


                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número da lista abaixo        #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                    for (int i = 0; i < eleicoes.size(); i++) {
                        System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                    passou = true;
                }

            }

            int idEleicao = Integer.parseInt(idEleicaoString);
            if (idEleicao == sair) return;

            idEleicao = EleicaoParaId.get(idEleicao);


            System.out.println("###############################################################");
            System.out.println("#     Informe o número mínimo de respostas para a questão:    #");

            String minString = " ";

            passou = true;
            while (passou) {
                try {
                    minString = in.nextLine();
                    Integer.parseInt(minString);
                    System.out.println(Integer.parseInt(minString));
                    if (Integer.parseInt(minString) >= 1) {

                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número maior que 0            #");
                        System.out.println("###############################################################");

                        passou = true;
                    }
                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número maior que 0            #");
                    System.out.println("###############################################################");
                    passou = true;
                }

            }
            int min = Integer.parseInt(minString);
            String maxString = " ";
            System.out.println("###############################################################");
            System.out.println("#     Informe o número máximo de respostas para a questão:    #");
            passou = true;
            while (passou) {
                try {
                    maxString = in.nextLine();
                    Integer.parseInt(maxString);
                    if (Integer.parseInt(maxString) >= 1) {
                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número maior que 0            #");
                        System.out.println("###############################################################");

                        passou = true;
                    }
                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número maior que 0            #");
                    System.out.println("###############################################################");
                    passou = true;
                }
            }
            int max = Integer.parseInt(maxString);


            if (min > max) {
                System.out.println("######################################################################################");
                System.out.println("#   Número máximo de respostas não pode ser menor que o número mínimo de respostas   #");
                System.out.println("######################################################################################");
                Thread.sleep(1000);
                clearConsole(6);
                return;
            }
            System.out.println("###############################################################");
            System.out.println("#                Informe o título da questão:                 #");

            String questao = in.nextLine();
            Questao q = new Questao(idEleicao, min, max, questao);
            if (QuestaoDAO.adiciona(q)) {
                System.out.println("######################################################################################");
                System.out.println("#                          Questão adicionada com sucesso!!                          #");
                System.out.println("######################################################################################");
                List<Questao> qq = QuestaoDAO.listarQuestoes(idEleicao);

                int idQuestao = qq.get(qq.size() - 1).getIdQuestao();
                Resposta nulo = new Resposta(idQuestao, idEleicao, "Nulo");
                Resposta branco = new Resposta(idQuestao, idEleicao, "Branco");
                RespostaDAO.adiciona(nulo);
                RespostaDAO.adiciona(branco);
                Thread.sleep(1000);
                clearConsole(6);
            } else {
                System.out.println("######################################################################################");
                System.out.println("#                            Erro ao criar a questão!!                               #");
                System.out.println("######################################################################################");
                Thread.sleep(1000);
                clearConsole(6);
            }

        } else {
            System.out.println("######################################################################################");
            System.out.println("#                               Nenhuma eleição criada                               #");
            System.out.println("######################################################################################");
            Thread.sleep(1000);
            clearConsole(6);
        }
    }

    /**
     * Método para criar uma resposta
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void criarResposta() throws InterruptedException, SQLException {

        boolean passou = true;
        List<Eleicao> eleicoes = EleicaoDAO.listarEleicoesFechadas();
        Map<Integer, Integer> idParaEleicao = new HashMap<Integer, Integer>();
        Map<Integer, Integer> EleicaoParaId = new HashMap<Integer, Integer>();

        if (eleicoes.size() > 0) {
            for (int i = 0; i < eleicoes.size(); i++) {
                idParaEleicao.put(eleicoes.get(i).getIdEleicao(), i + 1);
                EleicaoParaId.put(i + 1, eleicoes.get(i).getIdEleicao());

            }

            int sair = eleicoes.size() + 1;
            System.out.println("###############################################################");
            System.out.println("#         Escolha a eleição para adicionar alternativa:       #");
            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

            for (int i = 0; i < eleicoes.size(); i++) {
                System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());

            }
            System.out.println("    (" + sair + ") - " + "Sair");
            System.out.println("#                                                             #");
            String idEleicaoString = " ";
            while (passou) {
                try {
                    idEleicaoString = in.nextLine();
                    Integer.parseInt(idEleicaoString);
                    if (Integer.parseInt(idEleicaoString) <= eleicoes.size() + 1 && Integer.parseInt(idEleicaoString) > 0) {
                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número da lista abaixo        #");
                        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                        for (int i = 0; i < eleicoes.size(); i++) {
                            System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                        }
                        System.out.println("    (" + sair + ") - " + "Sair");
                        System.out.println("#                                                             #");
                        passou = true;
                    }


                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número da lista abaixo        #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                    for (int i = 0; i < eleicoes.size(); i++) {
                        System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                    passou = true;
                }

            }

            int idEleicao = Integer.parseInt(idEleicaoString);
            if (idEleicao == sair) return;

            idEleicao = EleicaoParaId.get(idEleicao);


            List<Questao> questoes = QuestaoDAO.listarQuestoes(idEleicao);

            if (questoes.size() == 0) {
                System.out.println("######################################################################################");
                System.out.println("#                   Nenhuma questão adicionada para a eleição                        #");
                System.out.println("######################################################################################");
                Thread.sleep(1000);
                clearConsole(6);
            }


            Map<Integer, Integer> idParaQuestao = new HashMap<Integer, Integer>();
            Map<Integer, Integer> QuestaoParaId = new HashMap<Integer, Integer>();

            for (int i = 0; i < questoes.size(); i++) {
                idParaQuestao.put(questoes.get(i).getIdQuestao(), i + 1);
                QuestaoParaId.put(i + 1, questoes.get(i).getIdQuestao());

            }

            sair = questoes.size() + 1;
            System.out.println("###############################################################");
            System.out.println("#         Escolha a questão para adicionar alternativa:       #");
            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

            for (int i = 0; i < questoes.size(); i++) {
                System.out.println("    (" + idParaQuestao.get(questoes.get(i).getIdQuestao()) + ") - " + questoes.get(i).getTituloQuestao());

            }
            System.out.println("    (" + sair + ") - " + "Sair");
            System.out.println("#                                                             #");

            passou = true;
            String idQuestaoString = " ";
            while (passou) {
                try {
                    idQuestaoString = in.nextLine();
                    Integer.parseInt(idQuestaoString);
                    if (Integer.parseInt(idQuestaoString) <= questoes.size() + 1 && Integer.parseInt(idQuestaoString) > 0) {
                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número da lista abaixo        #");
                        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                        for (int i = 0; i < questoes.size(); i++) {
                            System.out.println(idParaQuestao.get(questoes.get(i).getIdQuestao()) + "- " + questoes.get(i).getTituloQuestao());

                        }
                        for (int i = 0; i < questoes.size(); i++) {
                            System.out.println("    (" + idParaQuestao.get(questoes.get(i).getIdQuestao()) + ") - " + questoes.get(i).getTituloQuestao());

                        }
                        System.out.println("    (" + sair + ") - " + "Sair");
                        System.out.println("#                                                             #");
                        passou = true;
                    }
                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número da lista abaixo        #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                    for (int i = 0; i < questoes.size(); i++) {
                        System.out.println(idParaQuestao.get(questoes.get(i).getIdQuestao()) + "- " + questoes.get(i).getTituloQuestao());

                    }
                    for (int i = 0; i < questoes.size(); i++) {
                        System.out.println("    (" + idParaQuestao.get(questoes.get(i).getIdQuestao()) + ") - " + questoes.get(i).getTituloQuestao());

                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                    passou = true;
                }
            }

            int idQuestao = Integer.parseInt(idQuestaoString);
            if (idQuestao == sair) return;


            idQuestao = QuestaoParaId.get(idQuestao);

            Questao q = QuestaoDAO.listarQuestao(idEleicao, idQuestao);
            List<Resposta> respostas = RespostaDAO.listarRespostas(idQuestao);

            if ((q.getRespostasMaximasQuestao() > respostas.size() - 2)) {
                System.out.println("###############################################################");
                System.out.println("#   Você ainda deve adicionar " + (q.getRespostasMaximasQuestao() - (respostas.size() - 2)) + " respostas para esta questão!  #");

            }
            System.out.println("###############################################################");
            System.out.println("#                   Digite a alternativa:                     #");
            String resposta = in.nextLine();
            Resposta r = new Resposta(idQuestao, idEleicao, resposta);
            if (RespostaDAO.adiciona(r)) {
                System.out.println("######################################################################################");
                System.out.println("#                          Resposta adicionada com sucesso!!                         #");
                System.out.println("######################################################################################");
            } else {
                System.out.println("######################################################################################");
                System.out.println("#                            Erro ao adicionar a resposta!!                          #");
                System.out.println("######################################################################################");
            }

            q = QuestaoDAO.listarQuestao(idEleicao, idQuestao);
            respostas = RespostaDAO.listarRespostas(idQuestao);


            if ((q.getRespostasMaximasQuestao() > respostas.size() - 2)) {
                System.out.println("###############################################################");
                System.out.println("#   Você ainda deve adicionar " + (q.getRespostasMaximasQuestao() - (respostas.size() - 2)) + " respostas para esta questão!  #");
                System.out.println("###############################################################");
            }

            Thread.sleep(1000);
            clearConsole(6);

        } else {
            System.out.println("######################################################################################");
            System.out.println("#                               Nenhuma eleição criada                               #");
            System.out.println("######################################################################################");
            Thread.sleep(1000);
            clearConsole(6);
        }
    }

    /**
     * Método para adicionar eleitores em uma eleição
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void adicionarEleitor() throws InterruptedException, SQLException {
        boolean passou = true;
        List<Eleicao> eleicoes = EleicaoDAO.listarEleicoesFechadas();
        if (eleicoes.size() > 0) {
            Map<Integer, Integer> idParaEleicao = new HashMap<Integer, Integer>();
            Map<Integer, Integer> EleicaoParaId = new HashMap<Integer, Integer>();

            for (int k = 0; k < eleicoes.size(); k++) {
                idParaEleicao.put(eleicoes.get(k).getIdEleicao(), k + 1);
                EleicaoParaId.put(k + 1, eleicoes.get(k).getIdEleicao());
            }


            int sair = eleicoes.size() + 1;
            System.out.println("###############################################################");
            System.out.println("#          Escolha a eleição para adicionar eleitores         #");
            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

            for (int i = 0; i < eleicoes.size(); i++) {
                System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());

            }
            System.out.println("    (" + sair + ") - " + "Sair");
            System.out.println("#                                                             #");
            String idEleicaoString = " ";
            while (passou) {
                try {
                    idEleicaoString = in.nextLine();
                    Integer.parseInt(idEleicaoString);
                    if (Integer.parseInt(idEleicaoString) <= eleicoes.size() + 1 && Integer.parseInt(idEleicaoString) > 0) {
                        passou = false;
                    } else {
                        System.out.println("###############################################################");
                        System.out.println("#         Você deve escolher um número da lista abaixo        #");
                        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                        for (int i = 0; i < eleicoes.size(); i++) {
                            System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                        }
                        System.out.println("    (" + sair + ") - " + "Sair");
                        System.out.println("#                                                             #");
                        passou = true;
                    }


                } catch (Exception e) {
                    System.out.println("###############################################################");
                    System.out.println("#         Você deve escolher um número da lista abaixo        #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                    for (int i = 0; i < eleicoes.size(); i++) {
                        System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                    passou = true;
                }

            }
            int idEleicao = Integer.parseInt(idEleicaoString);
            if (idEleicao == sair) return;

            idEleicao = EleicaoParaId.get(idEleicao);


            System.out.println("######################################################################################");
            System.out.println("#  Informe o arquivo onde se encontra os eleitores (deve estar dentro de resources)  #");
            String arquivoEleitores = in.nextLine();
            URL url = Principal.class.getResource(arquivoEleitores);


            if (url != null) {

                File arquivo = new File(url.getPath());
                try {
                    Scanner leitor = new Scanner((arquivo));
                    while (leitor.hasNextLine()) {
                        Eleitores el = new Eleitores(leitor.nextLine(), idEleicao);
                        EleitoresDAO.adiciona(el);

                    }

                    System.out.println("######################################################################################");
                    System.out.println("#                           Eleitores inseridos com sucesso!!                        #");
                    System.out.println("######################################################################################");
                    Thread.sleep(1000);
                    clearConsole(6);

                } catch (FileNotFoundException ex) {
                    System.out.println("######################################################################################");
                    System.out.println("#                          Erro ao encontrar o arquivo informado                     #");
                    System.out.println("######################################################################################");
                    Thread.sleep(1000);
                    clearConsole(6);
                }
            } else {
                System.out.println("######################################################################################");
                System.out.println("#                          Erro ao encontrar o arquivo informado                     #");
                System.out.println("######################################################################################");
                Thread.sleep(1000);
                clearConsole(6);
            }
        } else {
            System.out.println("######################################################################################");
            System.out.println("#                               Nenhuma eleição criada                               #");
            System.out.println("######################################################################################");
            Thread.sleep(1000);
            clearConsole(6);
        }
    }

    /**
     * Método para votar em uma eleição
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void votar() throws InterruptedException, SQLException {
        System.out.println("###############################################################");
        System.out.println("#                   Insira seu login:                         #");


        String login = in.nextLine();

        System.out.println("###############################################################");
        System.out.println("#                  Insira sua senha:                          #");


        String senha = in.nextLine();
        List<Eleitores> eleitores = EleitoresDAO.listarTodos();
        List<Pessoa> pessoas = PessoaDAO.listarTodos();
        List<Integer> eleicoesEleitor = new ArrayList<>();
        boolean isSenha = false;

        for (int i = 0; i < pessoas.size(); i++) {
            if (pessoas.get(i).getSenhaPessoa().matches(senha)) {
                isSenha = true;

                break;
            }
        }

        if (isSenha) {
            for (int i = 0; i < eleitores.size(); i++) {
                if (eleitores.get(i).getLoginPessoa().matches(login) && !eleitores.get(i).isVotou())
                    eleicoesEleitor.add(eleitores.get(i).getIdEleicao());
            }

            if (eleicoesEleitor.size() > 0) {
                List<Eleicao> eleicoesAbertas = EleicaoDAO.listarTodasPossiveisEleicoes(eleicoesEleitor.toString());
                if (eleicoesAbertas.size() > 0) {
                    List<Eleicao> eleicoes = eleicoesAbertas;
                    boolean passou = true;
                    Map<Integer, Integer> idParaEleicao = new HashMap<Integer, Integer>();
                    Map<Integer, Integer> EleicaoParaId = new HashMap<Integer, Integer>();

                    for (int i = 0; i < eleicoes.size(); i++) {
                        idParaEleicao.put(eleicoes.get(i).getIdEleicao(), i + 1);
                        EleicaoParaId.put(i + 1, eleicoes.get(i).getIdEleicao());

                    }

                    int sair = eleicoes.size() + 1;
                    System.out.println("###############################################################");
                    System.out.println("#          Escolha a eleição para votar:                      #");
                    System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

                    for (int i = 0; i < eleicoes.size(); i++) {
                        System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());

                    }
                    System.out.println("    (" + sair + ") - " + "Sair");
                    System.out.println("#                                                             #");
                    String idEleicaoString = " ";
                    while (passou) {
                        try {
                            idEleicaoString = in.nextLine();
                            Integer.parseInt(idEleicaoString);
                            if (Integer.parseInt(idEleicaoString) <= eleicoes.size() + 1 && Integer.parseInt(idEleicaoString) > 0) {
                                passou = false;
                            } else {
                                System.out.println("###############################################################");
                                System.out.println("#         Você deve escolher um número da lista abaixo        #");
                                System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                                for (int i = 0; i < eleicoes.size(); i++) {
                                    System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                                }
                                System.out.println("    (" + sair + ") - " + "Sair");
                                System.out.println("#                                                             #");
                                passou = true;
                            }


                        } catch (Exception e) {
                            System.out.println("###############################################################");
                            System.out.println("#         Você deve escolher um número da lista abaixo        #");
                            System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");
                            for (int i = 0; i < eleicoes.size(); i++) {
                                System.out.println("    (" + idParaEleicao.get(eleicoes.get(i).getIdEleicao()) + ") - " + eleicoes.get(i).getTituloEleicao());
                            }
                            System.out.println("    (" + sair + ") - " + "Sair");
                            System.out.println("#                                                             #");
                            passou = true;
                        }

                    }
                    int idEleicao = Integer.parseInt(idEleicaoString);
                    if (idEleicao == sair) return;
                    idEleicao = EleicaoParaId.get(idEleicao);
                    List<Questao> questoes = QuestaoDAO.listarQuestoes(idEleicao);
                    List<Votos> votosParaAdicionar = new ArrayList<>();
                    for (int i = 0; i < questoes.size(); i++) {
                        System.out.println("###############################################################");
                        System.out.println("  " + questoes.get(i).getTituloQuestao());
                        System.out.println("# >> Respostas mínimas: " + questoes.get(i).getRespostasMinimasQuestao());
                        System.out.println("# >> Respostas máximas: " + questoes.get(i).getRespostasMaximasQuestao());
                        System.out.println("#  Finalizar antes de responder o número mínimo anula o voto  #");
                        System.out.println("# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #");

                        List<Resposta> respostas = RespostaDAO.listarRespostas(questoes.get(i).getIdQuestao());
                        for (int k = 0; k < questoes.get(i).getRespostasMaximasQuestao(); k++) {

                            Map<Integer, Integer> idParaVoto = new HashMap<Integer, Integer>();
                            Map<Integer, Integer> VotoParaId = new HashMap<Integer, Integer>();

                            for (int j = respostas.size() - 1; j > -1; j--) {
                                idParaVoto.put(respostas.get(j).getIdResposta(), -j + (respostas.size()));
                                VotoParaId.put(-j + (respostas.size()), respostas.get(j).getIdResposta());
                            }

                            for (int j = respostas.size() - 1; j > 0; j--) {
                                System.out.println("   (" + idParaVoto.get(respostas.get(j).getIdResposta()) + ") - " + respostas.get(j).getTituloResposta());
                            }
                            System.out.println("   (" + respostas.size() + ") - Finalizar");
                            passou = true;
                            String idRespostaString = " ";
                            while (passou) {
                                try {
                                    idRespostaString = in.nextLine();
                                    Integer.parseInt(idRespostaString);
                                    passou = false;
                                } catch (Exception e) {
                                    System.out.println("###############################################################");
                                    System.out.println("#       Digite um NÚMERO fora da lista para anular o voto     #");
                                    System.out.println("###############################################################");
                                    for (int j = respostas.size() - 1; j > 0; j--) {
                                        System.out.println("   (" + idParaVoto.get(respostas.get(j).getIdResposta()) + ") - " + respostas.get(j).getTituloResposta());
                                    }
                                    System.out.println("   (" + respostas.size() + ") - Finalizar");
                                    passou = true;
                                }
                            }


                            int idResposta = Integer.parseInt(idRespostaString);
                            int rm = -1;
                            for (int a = 0; a < respostas.size(); a++) {
                                if (idResposta < respostas.size() - 1) {
                                    if (respostas.get(a).getIdResposta() == VotoParaId.get(idResposta) && VotoParaId.get(idResposta) != null)
                                        rm = a;
                                }
                            }

                            if (idResposta == respostas.size() && k + 1 > questoes.get(i).getRespostasMinimasQuestao())
                                break;
                            if (idResposta < 0 || idResposta > (respostas.size() - 1)) {
                                Votos v = new Votos(VotoParaId.get((respostas.size())), questoes.get(i).getIdQuestao(), idEleicao);
                                votosParaAdicionar.add(v);


                            } else {
                                Votos v = new Votos(VotoParaId.get(idResposta), questoes.get(i).getIdQuestao(), idEleicao);
                                votosParaAdicionar.add(v);

                            }
                            if (rm >= 0) respostas.remove(rm);

                        }
                    }
                    if (votosParaAdicionar.size() > 0) {
                        for (int g = 0; g < votosParaAdicionar.size(); g++) {
                            VotosDAO.adiciona(votosParaAdicionar.get(g));
                        }
                        EleitoresDAO.votou(login, idEleicao);
                        System.out.println("###############################################################");
                        System.out.println("#                 Votos computados com sucesso!!!              #");
                        System.out.println("###############################################################");
                        Thread.sleep(1000);
                        clearConsole(6);
                    }
                } else {
                    System.out.println("###############################################################");
                    System.out.println("#             Nenhuma eleição aberta para o usuário           #");
                    System.out.println("###############################################################");
                    Thread.sleep(1000);
                    clearConsole(6);
                }
            } else {
                System.out.println("###############################################################");
                System.out.println("#             Nenhuma eleição aberta para o usuário           #");
                System.out.println("###############################################################");
                Thread.sleep(1000);
                clearConsole(6);
            }
        } else {
            System.out.println("###############################################################");
            System.out.println("#                 Usuário e/ou senha inválidos                #");
            System.out.println("###############################################################");
            Thread.sleep(1000);
            clearConsole(6);
        }
    }

    /**
     * Método para escrever o menu principal
     */
    public static void menu() {
        System.out.println();
        System.out.println("\t\t\t\t\t\t ##############################################################");
        System.out.println("\t\t\t\t\t   #                                                            # #");
        System.out.println("\t\t\t\t     #                                                         \t  #   #");
        System.out.println("\t\t\t\t\t#############################################################\t  #");
        System.out.println("\t\t\t\t\t#                                                           #\t  #");
        System.out.println("\t\t\t\t\t# >>>>>>               Urna Eletrônica               <<<<<< #\t  #");
        System.out.println("\t\t\t\t\t#                                                           #\t  #");
        System.out.println("\t\t\t\t\t#############################################################\t  #");
        System.out.println("\t\t\t\t\t#                                                           #\t  #");
        System.out.println("\t\t\t\t\t#                      Escolha uma opção                    #\t  #");
        System.out.println("\t\t\t\t\t# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - #\t  #");
        System.out.println("\t\t\t\t\t#       (1) - Criar eleição                                 #\t  #");
        System.out.println("\t\t\t\t\t#       (2) - Adicionar/atualizar eleitores de uma eleição  #\t  #");
        System.out.println("\t\t\t\t\t#       (3) - Adicionar questões a uma eleição              #\t  #");
        System.out.println("\t\t\t\t\t#       (4) - Adicionar respostas a uma questão             #\t  #");
        System.out.println("\t\t\t\t\t#       (5) - Abrir eleição                                 #\t  #");
        System.out.println("\t\t\t\t\t#       (6) - Fechar eleição                                #\t  #");
        System.out.println("\t\t\t\t\t#       (7) - Apurar eleição                                #\t  #");
        System.out.println("\t\t\t\t\t#       (8) - Resultados das eleições                       #\t  #");
        System.out.println("\t\t\t\t\t#       (9) - Listar eleicoes criadas                       #\t  #");
        System.out.println("\t\t\t\t\t#      (10) - Votar                                         #\t  #");
        System.out.println("\t\t\t\t\t#       (0) - Sair                                          #    #");
        System.out.println("\t\t\t\t\t#                                                           #  #  ");
        System.out.println("\t\t\t\t\t#############################################################");
    }

    /**
     * Método para apresentar o menu principal
     *
     * @throws InterruptedException
     * @throws SQLException
     */
    public static void display() throws InterruptedException, SQLException {
        menu();

        while (true) {
            boolean passou = true;
            String op = "";
            while (passou) {
                try {
                    op = in.nextLine();

                    Integer.parseInt(op);
                    passou = false;
                } catch (Exception e) {
                    menu();
                    passou = true;
                }
            }


            switch (Integer.parseInt(op)) {
                case 1:
                    criarEleicao();
                    display();
                case 2:
                    adicionarEleitor();
                    display();
                case 3:
                    criarQuestao();
                    display();
                case 4:
                    criarResposta();
                    display();

                case 5:
                    abrirEleicao();
                    display();
                case 6:
                    fecharEleicao();
                    display();
                case 7:
                    apurar();
                    display();
                case 8:
                    resultados();
                    display();
                case 9:
                    mostrarEleicoes();
                    display();
                case 10:
                    votar();
                    display();
                case 0:
                    System.exit(0);
                default:

                    //clearConsole(10);
                    System.out.flush();
                    System.out.println("\t\t\t\t\t###############################################################");
                    System.out.println("\t\t\t\t\t#                     Opção inválida                          #");
                    System.out.println("\t\t\t\t\t###############################################################");
                    //clearConsole(10);
                    Thread.sleep(2000);
                    //clearConsole(10);


                    display();
            }
        }
    }

    /**
     * Método para separar mensagens que aparecem no console
     *
     * @param lin
     */
    public static void clearConsole(int lin) {
        for (int i = 0; i < lin; ++i) System.out.println();

    }

    public static void main(String[] args) throws InterruptedException, SQLException {
        display();
    }
}
