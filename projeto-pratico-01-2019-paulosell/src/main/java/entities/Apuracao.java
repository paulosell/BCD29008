package entities;

/**
 * Classe para receber a tabela Apuracao
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class Apuracao {
    private int idApuracao;
    private int idResposta;
    private int idQuestao;
    private int idEleicao;
    private int qtdVotos;


    public Apuracao() {
    }


    public Apuracao(int idResposta, int idQuestao, int idEleicao, int qtdVotos) {
        this.idResposta = idResposta;
        this.idQuestao = idQuestao;
        this.idEleicao = idEleicao;
        this.qtdVotos = qtdVotos;
    }

    public Apuracao(int idApuracao, int idResposta, int idQuestao, int idEleicao, int qtdVotos) {
        this.idResposta = idResposta;
        this.idApuracao = idApuracao;
        this.idQuestao = idQuestao;
        this.qtdVotos = qtdVotos;
        this.idEleicao = idEleicao;
    }

    public int getIdApuracao() {
        return this.idApuracao;
    }

    public void setIdApuraaco(int idApuracao) {
        this.idApuracao = idApuracao;
    }

    public int getIdResposta() {
        return this.idResposta;
    }

    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
    }

    public int getIdQuestao() {
        return this.idQuestao;
    }

    public void setIdQuestao(int idQuestao) {
        this.idQuestao = idQuestao;
    }


    public int getQtdVotos() {
        return this.qtdVotos;
    }

    public void setQtdVotos(int qtdVotos) {
        this.qtdVotos = qtdVotos;
    }

    public int getIdEleicao() {
        return this.idEleicao;
    }

    public void setIdEleicao(int idEleicao) {
        this.idEleicao = idEleicao;
    }

    @Override
    public String toString() {
        return String.format("|%-5d|%-5d|%-5d|%-5d|%-5d|",
                this.idApuracao, this.idResposta, this.idQuestao, this.idEleicao, this.qtdVotos);
    }
}
