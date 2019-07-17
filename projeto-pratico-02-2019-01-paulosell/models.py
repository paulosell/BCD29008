from flask import Flask, render_template, flash, redirect, url_for, request, session
from flask_sqlalchemy import SQLAlchemy
from werkzeug.security import generate_password_hash, check_password_hash
from flask import Flask, render_template, jsonify
from flask_bootstrap import Bootstrap
from flask_nav import Nav
from flask_nav.elements import Navbar, View, Subgroup, Link


SECRET_KEY = 'aula de BCD - string aleatória'


app = Flask(__name__)
app.secret_key = 'SECRET_KEY'

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///urna-python.sqlite'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS']= False
db = SQLAlchemy(app)
boostrap = Bootstrap(app) # isso habilita o template bootstrap/base.html
nav = Nav()
nav.init_app(app) # isso habilita a criação de menus de navegação do pacote Flask-Nav




class Pessoa(db.Model):
    __tablename__       = 'Pessoa'
    nomePessoa          = db.Column(db.String(45), nullable=False)
    loginPessoa         = db.Column(db.String(45), primary_key=True, nullable=False)
    senhaPessoa         = db.Column(db.String(45), nullable=False)
    adminPessoa             = db.Column(db.Boolean, nullable=False)
    eleicoes = db.relationship('Eleicao', backref='Pessoa')


    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.nomePessoa         = kwargs.pop('nomePessoa')
        self.loginPessoa        = kwargs.pop('loginPessoa')
        self.senhaPessoa        = kwargs.pop('senhaPessoa')
        self.adminPessoa        = kwargs.pop('adminPessoa')

    def check_password(self, password):
        return self.senhaPessoa == password


class Eleicao(db.Model):
    __tablename__       = 'Eleicao'
    idEleicao           = db.Column(db.Integer, primary_key=True, autoincrement = True)
    tituloEleicao       = db.Column(db.String(45), unique = True, nullable=False)
    loginPessoa         = db.Column(db.String(45), db.ForeignKey('Pessoa.loginPessoa'), nullable = False)
    inicioEleicao       = db.Column(db.String(45), nullable=False)
    fimEleicao          = db.Column(db.String(45), nullable=True)
    estadoEleicao       = db.Column(db.Boolean, nullable=False)
    apuradaEleicao      = db.Column(db.Boolean, nullable=False)

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.tituloEleicao = kwargs.pop('tituloEleicao')
        self.inicioEleicao = kwargs.pop('inicioEleicao')
        self.estadoEleicao = False
        self.apuradaEleicao = False
        self.loginPessoa       = kwargs.pop('loginPessoa')


class Eleitores(db.Model):
    __tablename__ = "Eleitores"
    loginPessoa = db.Column(db.String, db.ForeignKey('Pessoa.loginPessoa'), nullable = False, primary_key=True)
    idEleicao   = db.Column(db.Integer, db.ForeignKey('Eleicao.idEleicao'), nullable = False, primary_key=True)
    votou       = db.Column(db.Boolean, nullable = False)

    def _init_(self, **kwargs):
        super().__init__(**kwargs)
        self.loginPessoa = kwargs.pop('loginPessoa')
        self.idEleicao   = kwargs.pop('idEleicao')
        self.votou       = False

class Questao(db.Model):
    __tablename__ = "Questao"
    idQuestao               = db.Column(db.Integer, primary_key=True, autoincrement=True)
    respostasMinimasQuestao = db.Column(db.Integer, nullable=False)
    respostasMaximasQuestao = db.Column(db.Integer, nullable=False)
    idEleicao               = db.Column(db.Integer, db.ForeignKey('Eleicao.idEleicao'), nullable=False)
    tituloQuestao           = db.Column(db.String,  nullable=False)

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.respostasMaximasQuestao = kwargs.pop('respostasMaximasQuestao')
        self.respostasMinimasQuestao = kwargs.pop('respostasMinimasQuestao')
        self.tituloQuestao           = kwargs.pop('tituloQuestao')
        self.idEleicao               = kwargs.pop('idEleicao')

class Resposta(db.Model):
    __tablename__ = "Resposta"
    idResposta = db.Column(db.Integer, primary_key=True, autoincrement=True)
    tituloResposta = db.Column(db.String, nullable=False)
    idQuestao      = db.Column(db.Integer, db.ForeignKey('Questao.idQuestao'), nullable=False)
    idEleicao      = db.Column(db.Integer, db.ForeignKey('Eleicao.idEleicao'), nullable=False)

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.tituloResposta = kwargs.pop('tituloResposta')
        self.idQuestao      = kwargs.pop('idQuestao')
        self.idEleicao      = kwargs.pop('idEleicao')

class Apuracao(db.Model):
    __tablename__ = "Apuracao"
    idResposta = db.Column(db.Integer, db.ForeignKey('Resposta.idResposta'), nullable=False, primary_key=True)
    idQuestao  = db.Column(db.Integer, db.ForeignKey('Questao.idQuestao'), nullable=False, primary_key=True)
    idEleicao  = db.Column(db.Integer, db.ForeignKey('Eleicao.idEleicao'), nullable=False, primary_key=True)
    qtdVotos   = db.Column(db.Integer, nullable=False)

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.idEleicao = kwargs.pop('idEleicao')
        self.idQuestao = kwargs.pop('idQuestao')
        self.idResposta = kwargs.pop('idResposta')
        self.qtdVotos = kwargs.pop('qtdVotos')

class Votos(db.Model):
    __tablename__ = "Votos"
    idVotos    = db.Column(db.Integer, primary_key=True, autoincrement=True)
    idResposta = db.Column(db.Integer, db.ForeignKey('Resposta.idResposta'), nullable=False)
    idQuestao = db.Column(db.Integer, db.ForeignKey('Questao.idQuestao'), nullable=False)
    idEleicao = db.Column(db.Integer, db.ForeignKey('Eleicao.idEleicao'), nullable=False)


    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.idEleicao = kwargs.pop('idEleicao')
        self.idQuestao = kwargs.pop('idQuestao')
        self.idResposta = kwargs.pop('idResposta')


