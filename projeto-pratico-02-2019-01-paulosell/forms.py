from flask_wtf import FlaskForm, Form
from wtforms import StringField, PasswordField, RadioField, SubmitField, BooleanField, IntegerField, FormField, \
    SelectField, FieldList, SelectMultipleField, Field, Label, widgets
from datetime import date

from wtforms.ext.sqlalchemy.fields import QuerySelectMultipleField
from wtforms.fields.html5 import  DateField
from wtforms.validators import DataRequired, ValidationError, NumberRange, Required
from wtforms.widgets import TextInput, html_params, ListWidget, CheckboxInput

from models import Resposta

'''
Veja mais na documentação do WTForms

https://wtforms.readthedocs.io/en/stable/
https://wtforms.readthedocs.io/en/stable/fields.html

Um outro pacote interessante para estudar:

https://wtforms-alchemy.readthedocs.io/en/latest/

'''

class LoginForm(FlaskForm):
    username = StringField('Nome de usuário', validators=[DataRequired("O preenchimento desse campo é obrigatório")])
    password = PasswordField('Senha', validators=[DataRequired("O preenchimento desse campo é obrigatório")])
    admin    = BooleanField('Login como administrador')
    submit = SubmitField('Entrar')

class EscolhaAdminForm(FlaskForm):
     opt = RadioField('Escolha a opção', choices=[('create','Criar Eleição'), ('manag','Gerenciar Eleição')])
     submit = SubmitField('Ir')


class EscolhaEleitorForm(FlaskForm):
    opt = RadioField('Escolha a opção', choices=[('votar', 'Votar'), ('resultado', 'Ver resultados')])
    submit = SubmitField('Ir')

class CriarEleicaoForm(FlaskForm):
    tituloEleicao = StringField('Titulo da eleição', validators=[DataRequired('O preenchimento desse campo é obrigatório')])
    inicio        = DateField('Data de início (dia-mes-ano)', format='%Y-%m-%d', validators=[DataRequired('O preenchimento desse campo é obrigatório')])
    submit        = SubmitField('Confirmar')


    def validate_on_submit(self):
        result = super(CriarEleicaoForm, self).validate()
        if (self.inicio.data < date.today()):
            return False
        else:
            return result

class GerenciaEleicaoForm(FlaskForm):
    opt = RadioField('Escolha a opção', choices=[('abrir', 'Abrir eleição'), ('fechar', 'Fechar eleição'), ('apurar', 'Apurar eleição')
                                                 , ('resultados', 'Ver resultado das eleições')])
    submit = SubmitField('Ir')

class BotaoForm(FlaskForm):
    submit = SubmitField('Ir')

class CriarPerguntaForm(FlaskForm):
    eleicoes = SelectField(u'Selecione a eleição', coerce=int)
    questao = StringField('Questão', validators=[DataRequired('O preenchimento desse campo é obrigatório')])
    min     = IntegerField('Número mínimo de respostas', validators=[DataRequired('O preenchimento desse campo é obrigatório'),NumberRange(min=1, message="Número mínimo não pode ser menor que zero")])
    max     = IntegerField('Numero máximo de respostas', validators=[DataRequired('O preenchimento desse campo é obrigatório'),NumberRange(min=1, message="Número mínimo não pode ser menor que zero")])
    submit = SubmitField('Ir')

class CriarRespostaForm(FlaskForm):
    eleicoes = SelectField(u'Selecione a eleição', coerce=int)
    questoes = SelectField(u'Selecione a questão', coerce=int)
    resposta = StringField('Digite a resposta', validators=[DataRequired("Campo obrigatório")])
    submit   = SubmitField('Ir')

class RespostasForm(FlaskForm):
    resposta = BooleanField()

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        label = kwargs.pop('label')
        self.resposta.label = Label(self['resposta'].id, label)

class EleicaoParaVotarForm(FlaskForm):
    eleicoes = SelectField('Escolha a eleição', choices=[], coerce=int)
    submit   = SubmitField('Ir')

class PerguntasRespostasForm(FlaskForm):
    respostas = SelectMultipleField(choices=[], coerce=int)

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        label = kwargs.pop('label')
        self.respostas.label = Label(self['respostas'].id, label)

