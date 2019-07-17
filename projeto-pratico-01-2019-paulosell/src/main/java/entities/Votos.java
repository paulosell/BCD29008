package entities;

/**
 * Classe para receber a tabela Votos
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class Votos {
    private int idVotos;
    private int idResposta;
    private int idQuestao;
    private int idEleicao;

    public Votos() {
    }

    ;

    public Votos(int idRespota, int idQuestao, int idEleicao) {
        this.idEleicao = idEleicao;
        this.idQuestao = idQuestao;
        this.idResposta = idRespota;
    }

    public Votos(int idResposta, int idQuestao, int idEleicao, int idVotos) {
        this.idVotos = idVotos;
        this.idEleicao = idEleicao;
        this.idQuestao = idQuestao;
        this.idResposta = idResposta;
    }

    public int getIdVotos() {
        return this.idVotos;
    }

    public void setIdVotos(int idVotos) {
        this.idVotos = idVotos;
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

    public int getIdEleicao() {
        return this.idEleicao;
    }

    public void setIdEleicao(int idEleicao) {
        this.idEleicao = idEleicao;
    }

    @Override
    public String toString() {
        return String.format("|%-5d|%-5d|%-5d|%-5d|",
                this.idVotos, this.idResposta, this.idQuestao, this.idEleicao);
    }
}
