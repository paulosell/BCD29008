package entities;

/**
 * Classe para receber a tabela Resposta
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class Resposta {
    private int idResposta;
    private int idQuestao;
    private int idEleicao;
    private String TituloResposta;

    public Resposta() {
    }

    ;

    public Resposta(int idQuestao, int idEleicao, String TituloRespota) {
        this.idQuestao = idQuestao;
        this.idEleicao = idEleicao;
        this.TituloResposta = TituloRespota;
    }

    public Resposta(int idResposta, int idQuestao, int idEleicao, String TituloRespota) {
        this.idResposta = idResposta;
        this.idQuestao = idQuestao;
        this.idEleicao = idEleicao;
        this.TituloResposta = TituloRespota;
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

    public String getTituloResposta() {
        return this.TituloResposta;
    }

    public void setTituloResposta(String tituloResposta) {
        TituloResposta = tituloResposta;
    }


    @Override
    public String toString() {

        return String.format("|%-5d|%-5d|%-5d|%-25s|",
                this.idResposta, this.idQuestao, this.idEleicao, this.TituloResposta);
    }
}
