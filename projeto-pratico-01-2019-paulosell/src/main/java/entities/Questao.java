package entities;

/**
 * Classe para receber a tabela Questao
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class Questao {
    private int idQuestao;
    private int idEleicao;
    private int respostasMinimasQuestao;
    private int respostasMaximasQuestao;
    private String TituloQuestao;

    public Questao() {
    }

    ;

    public Questao(int idEleicao, int respostasMinimasQuestao, int respostasMaximasQuestao, String TituloQuestao) {
        this.idEleicao = idEleicao;
        this.respostasMaximasQuestao = respostasMaximasQuestao;
        this.respostasMinimasQuestao = respostasMinimasQuestao;
        this.TituloQuestao = TituloQuestao;
    }

    public Questao(int idQuestao, int idEleicao, int respostasMinimasQuestao, int respostasMaximasQuestao, String TituloQuestao) {
        this.idQuestao = idQuestao;
        this.idEleicao = idEleicao;
        this.respostasMaximasQuestao = respostasMaximasQuestao;
        this.respostasMinimasQuestao = respostasMinimasQuestao;
        this.TituloQuestao = TituloQuestao;
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

    public int getRespostasMinimasQuestao() {
        return this.respostasMinimasQuestao;
    }

    public void setRespostasMinimasQuestao(int respostasMinimasQuestao) {
        this.respostasMinimasQuestao = respostasMinimasQuestao;
    }

    public int getRespostasMaximasQuestao() {
        return this.respostasMaximasQuestao;
    }

    public void setRespostasMaximasQuestao(int respostasMaximasQuestao) {
        this.respostasMaximasQuestao = respostasMaximasQuestao;
    }

    public String getTituloQuestao() {
        return this.TituloQuestao;
    }

    public void setTituloQuestao(String tituloQuestao) {
        TituloQuestao = tituloQuestao;
    }

    @Override
    public String toString() {

        return String.format("|%-5d|%-5d|%-5d|%-5d|%-25s|",
                this.idQuestao, this.idEleicao, this.respostasMaximasQuestao, this.respostasMinimasQuestao, this.TituloQuestao);
    }


}
