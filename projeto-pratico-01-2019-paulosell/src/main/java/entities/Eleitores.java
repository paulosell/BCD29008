package entities;

/**
 * Classe para receber a tabela Eleitores
 * do banco de dados
 *
 * @author Paulo Sell
 */
public class Eleitores {
    private String loginPessoa;
    private int idEleicao;
    private boolean Votou = false;

    public Eleitores() {
    }

    ;

    public Eleitores(String loginPessoa, int idEleicao) {
        this.loginPessoa = loginPessoa;
        this.idEleicao = idEleicao;
    }

    public Eleitores(String loginPessoa, int idEleicao, boolean votou) {
        this.loginPessoa = loginPessoa;
        this.idEleicao = idEleicao;
        this.Votou = votou;
    }

    public String getLoginPessoa() {
        return this.loginPessoa;
    }

    public void setLoginPessoa(String loginPessoa) {
        this.loginPessoa = loginPessoa;
    }

    public int getIdEleicao() {
        return this.idEleicao;
    }

    public void setIdEleicao(int idEleicao) {
        this.idEleicao = idEleicao;
    }

    public boolean isVotou() {
        return this.Votou;
    }

    public void setVotou(boolean votou) {
        Votou = votou;
    }

    @Override
    public String toString() {
        return String.format("|%-25s|%-5d|%-10b|",
                this.loginPessoa, this.idEleicao, this.Votou);
    }
}
