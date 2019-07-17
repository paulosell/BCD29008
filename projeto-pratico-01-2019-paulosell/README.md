[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/paulosell/BCD29008/blob/master/LICENSE)

# Projeto prático 01
Projeto desenvolvido para a disciplina de Banco de Dados (BCD29008) do curso de Engenheria de Telecomunicações do Instituto Federal de Santa Catarina - câmpus São José, ministrada pelo professor Emerson Mello.

# Schema do banco de dados
O _schema_ do banco de dados utilizado foi criado com o auxílio da ferramenta [_mySQL Workbench_](https://www.mysql.com/products/workbench/). A modelagem do banco pode ser conferida na figura abaixo.

![image](imagem/urna.png)



# Funcionalidades implementadas no projeto

  * Criar eleição
  * Adicionar eleitores à eleição
  * Criar questão para eleição
  * Criar alternativas para questão
  * Abrir eleição
  * Fechar eleição
  * Apurar uma eleição
  * Exibir resultados de uma eleição
  * Exibir todas eleições já criadas
  * Votar
  
![image](imagem/frames.png)

# Considerações

* O arquivo contendo os eleitores deve estar dentro de `/src/main/resources` no projeto

* A eleição só poderá ser aberta no dia em que o usuário configurou
  
* Login e senha do usuário segue o padrão:
  > login: paulo
  
  > senha: senhapaulo

* O arquivo de instruções DDL e DML encontra-se na raíz do projeto

* Deve-se inserir as informações do banco dentro do (usuário, senha, host e porta) dentro da classe _`ConnectionFactory`_, no pacote _`db`_


