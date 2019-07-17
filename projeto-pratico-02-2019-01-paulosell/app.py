from flask_login import login_required

from models import *
from forms import *
from datetime import date
import dadostabela


@nav.navigation()
def menunavbar():
    menu = Navbar('Urna eletrônica')
    menu.items = [View('Home', 'inicio'), View('Login', 'autenticar')]
    return menu


@nav.navigation()
def menuadmin():
    menu = Navbar('Administrador')
    menu.items = [View('Home', 'admin')]
    menu.items.append(
        Subgroup('Eleições', View('Criar eleição', 'criar_eleicao'), View('Inserir eleitor', 'inserir_eleitor'),
                 View('Inserir perguntas', 'criar_perguntas'),
                 View('Inserir respostas', 'criar_respostas')))
    menu.items.append(View('Abrir eleição', 'abrir_eleicao'))
    menu.items.append(View('Fechar eleição', 'fechar_eleicao'))
    menu.items.append(View('Apurar eleição', 'apurar_eleicao'))
    menu.items.append(View('Resultados', 'eleicao_resultados'))
    menu.items.append(View('Logout', 'logout'))
    return menu


@nav.navigation()
def menueleitor():
    menu = Navbar('Eleitor')
    menu.items = [View('Home', 'eleitor'), View('Votar', 'votar'), View('Resultados', 'eleicao_resultados'),
                  View('Logout', 'logout')]
    return menu


@app.route('/criar_perguntas', methods=['GET', 'POST'])
def criar_perguntas():
    if session.get('logged_in') and session.get('admin'):
        form = CriarPerguntaForm()

        eleicoes = Eleicao.query.filter(Eleicao.fimEleicao == None).filter_by(loginPessoa=session['user'], estadoEleicao=False).all()
        if len(eleicoes) > 0:

            form.eleicoes.choices = [(g.idEleicao, g.tituloEleicao) for g in
                                    Eleicao.query.filter(Eleicao.fimEleicao == None).filter_by(loginPessoa=session['user'], estadoEleicao=False).all()]

            if request.method == 'GET':
                return render_template('criar_pergunta.html', title='Criar perguntas', menu=1, form=form)

            if form.validate_on_submit():
                q = Questao(idEleicao=int(form.eleicoes.data), tituloQuestao=form.questao.data,
                                  respostasMaximasQuestao=int(form.max.data), respostasMinimasQuestao=int(form.min.data))

                if (form.min.data <= form.max.data):

                    try:
                        db.session.add(q)
                        db.session.commit()
                        qq = Questao.query.filter_by(idEleicao=form.eleicoes.data, tituloQuestao=form.questao.data).first()
                        r = Resposta(tituloResposta='Branco', idQuestao=qq.idQuestao, idEleicao=form.eleicoes.data)

                        db.session.add(r)
                        db.session.commit()
                        flash("Questão inserida com sucesso")
                        return redirect(url_for('admin'))
                    except:
                        flash("Questão com mesmo nome já criada")
                        return redirect(url_for('admin'))
                else:
                    flash("Verifique o número máximo e mínimo de respostas")
                    return redirect(url_for('criar_perguntas'))

            flash("Número de respostas mínimas e máximas inconsistentes")
            return redirect(url_for('admin'))
        else:
            flash("Nenhuma eleição possível para inserir questão")
            return redirect(url_for('admin'))
    elif session.get('logged_in') and not session.get('admin'):
        flash('Voce precisa iniciar sessão como administrador')
        return redirect(url_for('eleitor'))
    else:
        flash('Voce precisa iniciar sessão como administrador')
        return redirect(url_for('autenticar'))


@app.route('/criar_respostas', methods=['GET', 'POST'])
def criar_respostas():
    if session.get('logged_in') and session.get('admin'):
        form = CriarRespostaForm()

        eleicoes = Eleicao.query.filter(Eleicao.fimEleicao == None).filter_by(loginPessoa=session['user'],
                                                                              estadoEleicao=False).all()

        if len(eleicoes) > 0:
            form.eleicoes.choices = [(g.idEleicao, g.tituloEleicao) for g in
                                     Eleicao.query.filter(Eleicao.fimEleicao == None).filter_by(
                                         loginPessoa=session['user'], estadoEleicao=False).all()]

            form.questoes.choices = [(g.idQuestao, g.tituloQuestao) for g in
                                     Questao.query.filter_by(idEleicao=Eleicao.query.filter(Eleicao.fimEleicao == None).filter_by(
                loginPessoa=session['user'], estadoEleicao=False).first().idEleicao).all()]

            if request.method == 'GET':
                return render_template('criar_resposta.html', title='Criar respostas', form=form, menu=1)

            form.questoes.choices = [(g.idQuestao, g.tituloQuestao) for g in Questao.query.all()]

            if form.validate_on_submit():
                resposta = Resposta(idEleicao=int(form.eleicoes.data), idQuestao=int(form.questoes.data),
                                    tituloResposta=form.resposta.data)
                try:
                    db.session.add(resposta)
                    db.session.commit()
                    flash("Resposta inserida")
                    return redirect(url_for('admin'))
                except:
                    flash("Resposta já criada")
                    return redirect(url_for('admin'))

            flash("Erro ao processar a solicitação")
            return redirect(url_for('admin'))

    elif session.get('logged_in') and not session.get('admin'):
        flash('Voce precisa iniciar sessão como administrador')
        return redirect(url_for('eleitor'))
    else:
        flash('Voce precisa iniciar sessão como administrador')
        return redirect(url_for('autenticar'))


@app.route('/questao/<id>')
def questao(id):
    questoes = Questao.query.filter_by(idEleicao=id).all()
    questArray = []

    for questao in questoes:
        questaoObj = {}
        questaoObj['id'] = questao.idQuestao
        questaoObj['titulo'] = questao.tituloQuestao
        questArray.append(questaoObj)

    return jsonify({'questoes': questArray})


@app.route("/logout")
def logout():
    '''
    Para encerrar a sessão autenticada de um usuário
    :return: redireciona para a página inicial
    '''
    if session.get('logged_in'):
        session.pop('logged_in', None)
        session.pop('username', None)
        session.pop('user', None)
        session.pop('admin', None)
        session.pop('eleicao_resultado', None)
        session.pop('eleicao_para_votar', None)
        return redirect(url_for('inicio'))
    else:
        return redirect(url_for('inicio'))


@app.errorhandler(404)
def page_not_found(e):
    '''
    Para tratar erros de páginas não encontradas - HTTP 404
    :param e:
    :return:
    '''
    return render_template('404.html'), 404


@app.route('/inserir_eleitor', methods=['GET', 'POST'])
def inserir_eleitor():
    if session.get('logged_in') and session.get('admin'):

        if request.method == 'GET':
            e =  Eleicao.query.filter(Eleicao.fimEleicao == None).filter_by(loginPessoa=session['user'], estadoEleicao=False).all()
            p = Pessoa.query.all()

            data = []

            for pessoas in p:
                json = {"pessoa": pessoas.nomePessoa,
                        "login": pessoas.loginPessoa}
                data.append(json)
            return render_template('inserir_eleitor.html', title='Inserir eleitor', eleicoes=e, menu=1, data=data,
                                   columns=dadostabela.columns_criar_eleicao)

        else:
            if request.is_json:
                e = Eleicao.query.filter_by(tituloEleicao=str(request.get_json()['titulo_eleicao'])).first()
                eleitores = request.get_json()['eleitores']

                if len(eleitores) > 0:
                    for i in range(len(eleitores)):
                        eleitor = Eleitores(loginPessoa=eleitores[i]['login'], idEleicao=e.idEleicao, votou=False)
                        db.session.add(eleitor)
                else:
                    flash("Nenhum eleitor selecionado")
                    return redirect(url_for('admin'))
                try:
                    db.session.commit()
                except:
                    flash("Você selecionou um eleitor que ja está cadastrado. Nenhum novo eleitor inserido")
                    db.session.rollback()

            return redirect(url_for('admin'))

    elif session.get('logged_in') and not session.get('admin'):
        flash('Voce precisa iniciar sessão como administrador')
        return redirect(url_for('eleitor'))

    else:
        flash('Voce precisa iniciar sessão como administrador')
        return redirect(url_for('autenticar'))


@app.route('/criar_eleicao', methods=['GET', 'POST'])
def criar_eleicao():
    if session.get('logged_in') and session.get('admin'):
        if request.method == 'GET':
            form = CriarEleicaoForm()
            return render_template('criar_eleicao.html', title='Criar eleição', form=form, menu=1)

        form = CriarEleicaoForm()

        if form.validate_on_submit():
            e = Eleicao(tituloEleicao=form.tituloEleicao.data, inicioEleicao=form.inicio.data,
                        loginPessoa=session['user'])
            try:
                db.session.add(e)
                db.session.commit()
                flash("Eleição criada com sucesso")
                return redirect(url_for('admin'))
            except:
                flash("Eleição com o mesmo nome já criada")
                return redirect(url_for('criar_eleicao'))
        else:
            flash("A data de inicio não pode ser no passado")

        return render_template('criar_eleicao.html', form=form, menu=1)
    elif session.get('logged_in') and not session.get('admin'):
        flash('Voce precisa iniciar sessão como administrador')
        return redirect(url_for('eleitor'))
    else:
        flash('Voce precisa iniciar sessão como administrador')
        return redirect(url_for('autenticar'))


@app.route('/administrador', methods=['GET', 'POST'])
def admin():
    if session.get('logged_in') and session.get('admin'):
        return render_template('admin.html', title='Painel de adminstrador', user=session['user'], menu=1)
    elif session.get('logged_in') and not session.get('admin'):
        flash('Voce precisa iniciar sessao como administrador')
        return redirect(url_for('eleitor'))
    else:
        flash('Voce precisa iniciar sessao como administrador')
        return redirect(url_for('autenticar'))


@app.route('/fechar_eleicao', methods=['GET', 'POST'])
def fechar_eleicao():
    if session.get('logged_in') and session.get('admin'):
        form = BotaoForm()

        if request.method == 'GET':
            e = Eleicao.query.filter_by(estadoEleicao=True, loginPessoa=session['user']).all()

            if len(e) > 0:
                data = []
                for eleicoes in e:
                    a = {"eleicao": eleicoes.tituloEleicao
                         }
                    data.append(a)
            else:
                flash("Nenhuma eleição possível para fechar")
                return redirect(url_for('admin'))

            return render_template('fechar_eleicao.html', title='Fechar eleição', form=form,
                                   menu=1, data=data, columns=dadostabela.columns_fechar_eleicao)

        if request.is_json:
            eleicoes  = request.get_json()['eleicoes']
            for i in range (len(eleicoes)):
                e = Eleicao.query.filter_by(tituloEleicao=eleicoes[i]['eleicao']).first()
                e.fimEleicao = date.today()
                e.estadoEleicao = False
                db.session.add(e)
                db.session.commit()
            flash("Eleicoes fechadas")
            return redirect(url_for('abrir_eleicao'))
        else:
            flash("Eleicoes fechadas")
            return redirect(url_for('admin'))

    elif session.get('logged_in') and not session.get('admin'):
        flash('Voce precisa iniciar sessao como administrador')
        return redirect(url_for('eleitor'))
    else:
        flash('Voce precisa iniciar sessao como administrador')
        return redirect(url_for('autenticar'))


@app.route('/apurar_eleicao', methods=['GET', 'POST'])
def apurar_eleicao():
    if session.get('logged_in') and session.get('admin'):
        form = BotaoForm()
        e = Eleicao.query.filter(Eleicao.fimEleicao != None).filter_by(estadoEleicao=False,
                                                                       apuradaEleicao=False,
                                                                       loginPessoa=session['user']).all()
        if len(e) > 0:
            data = []
            for eleicoes in e:
                a = {"eleicao": eleicoes.tituloEleicao
                     }
                data.append(a)

        else:
            flash("Todas eleições foram apuradas")
            return redirect(url_for('admin'))

        if request.method == 'GET':

            return render_template('apurar_eleicao.html', title='Apurar eleição', form=form,
                                   menu=1, data=data, columns=dadostabela.columns_apurar_eleicao)

        else:
            if (request.is_json):
                eleicoes = request.get_json()
                eleicoes_para_apurar = []
                for eleicao in eleicoes:
                    e = Eleicao.query.filter_by(tituloEleicao=eleicao['eleicao']).first()
                    eleicoes_para_apurar.append(e)
                for eleicao in eleicoes_para_apurar:
                    q = Questao.query.filter_by(idEleicao=eleicao.idEleicao).all()
                    for questao in q:
                        r = Resposta.query.filter_by(idEleicao=questao.idEleicao, idQuestao = questao.idQuestao).all()
                        for resposta in r:
                            voto = Votos.query.filter_by(idEleicao=resposta.idEleicao,idQuestao=resposta.idQuestao,
                                                         idResposta=resposta.idResposta).count()
                            if (voto == 0):
                                apurada = Apuracao(idEleicao=resposta.idEleicao, idResposta=resposta.idResposta,
                                                   idQuestao=resposta.idQuestao, qtdVotos=0)
                                db.session.add(apurada)
                                db.session.commit()
                            else:
                                apurada = Apuracao(idEleicao=resposta.idEleicao, idResposta=resposta.idResposta,
                                                   idQuestao=resposta.idQuestao, qtdVotos=voto)
                                db.session.add(apurada)
                                db.session.commit()
                    eleicao.apuradaEleicao = True
                    db.session.add(eleicao)
                    db.session.commit()


                flash("Eleições apuradas")
                return redirect(url_for('admin'))

            flash("Eleições apuradas")
            return redirect(url_for('admin'))

    elif session.get('logged_in') and not session.get('admin'):
        flash('Voce precisa iniciar sessao como administrador')
        return redirect(url_for('eleitor'))
    else:
        flash('Voce precisa iniciar sessao como administrador')
        return redirect(url_for('autenticar'))


@app.route('/abrir_eleicao', methods=['GET', 'POST'])
def abrir_eleicao():
    if session.get('logged_in') and session.get('admin'):
        if request.method == 'GET':
            e = Eleicao.query.filter_by(fimEleicao=None, inicioEleicao=date.today(), estadoEleicao=False,
                                        loginPessoa=session['user']).all()
            if len(e) > 0:
                data = []
                for eleicoes in e[:]:
                    eleitor = Eleitores.query.filter_by(idEleicao=eleicoes.idEleicao).first()
                    q = Questao.query.filter_by(idEleicao=eleicoes.idEleicao).all()
                    if len(q) > 0 and eleitor is not None:
                        for questoes in q:
                            r = Resposta.query.filter_by(idEleicao=eleicoes.idEleicao, idQuestao=questoes.idQuestao).all()
                            if not (questoes.respostasMaximasQuestao <= len(r)-1):
                                e.remove(eleicoes)
                                break
                    else:
                        e.remove(eleicoes)

                if len(e) > 0:
                    for eleicoes in e:
                        json = {"eleicao": eleicoes.tituloEleicao
                                }
                        data.append(json)
                else:
                    flash("Nenhuma eleição possível para abrir")
                    return redirect(url_for('admin'))

                return render_template('abrir_eleicao.html', title='Abrir eleição',
                                   menu=1, data=data, columns=dadostabela.columns_abrir_eleicao)
            else:
                flash("Nenhuma eleição possível para abrir")
                return redirect(url_for('admin'))

        if request.is_json:
            eleicoes  = request.get_json()['eleicoes']
            for i in range (len(eleicoes)):
                e = Eleicao.query.filter_by(tituloEleicao=eleicoes[i]['eleicao']).first()
                e.estadoEleicao = True
                db.session.add(e)
                db.session.commit()
            flash("Eleicoes abertas")
            return redirect(url_for('admin'))
        else:
            flash("Eleicoes abertas")
            return redirect(url_for('admin'))

    elif session.get('logged_in') and not session.get('admin'):
        flash('Voce precisa iniciar sessao como administrador')
        return redirect(url_for('eleitor'))
    else:
        flash('Voce precisa iniciar sessão como administrador')
        return redirect(url_for('autenticar'))


@app.route('/login', methods=['GET', 'POST'])
def autenticar():
    if not session.get('logged_in'):
        form = LoginForm()

        if form.validate_on_submit():
            pessoa = Pessoa.query.filter_by(loginPessoa=form.username.data).first()
            if pessoa is not None:
                if pessoa.check_password(form.password.data):
                    if form.admin.data:
                        if pessoa.adminPessoa:
                            session['logged_in'] = True
                            session['user'] = pessoa.nomePessoa
                            session['username'] = pessoa.loginPessoa
                            session['admin'] = True
                            return redirect(url_for('admin'))
                        else:
                            flash('Usuario não é administrador')
                            return redirect(url_for('autenticar'))
                    else:
                        session['logged_in'] = True
                        session['username'] = pessoa.loginPessoa
                        session['user'] = pessoa.nomePessoa
                        session['admin'] = False
                        return redirect(url_for('eleitor'))
                else:
                    flash("Usuário ou senha incorretos")
            else:
                flash("Usuário ou senha incorretos")

        return render_template('login.html', title='Autenticação de usuários', form=form, menu=0)
    elif session.get('logged_in') and session.get('admin'):
        return redirect(url_for('admin'))
    elif session.get('logged_in') and not session.get('admin'):
        return redirect(url_for('eleitor'))


@app.route('/eleitor', methods=['GET', 'POST'])
def eleitor():
    if session.get('logged_in') and not session.get('admin'):
        return render_template('eleitor.html',  title='Painel de eleitor', user=session['user'], menu=2)
    elif session.get('logged_in') and session.get('admin'):
        flash('Voce precisa iniciar sessao como eleitor para acessar essa pagina')
        return redirect(url_for('admin'))
    else:
        flash('Voce precisa iniciar sessao como eleitor')
        return redirect(url_for('autenticar'))


@app.route('/votar', methods=['GET', 'POST'])
def votar():
    if session.get('logged_in') and not session.get('admin'):
        form = EleicaoParaVotarForm()

        if request.method == 'GET':

            eleicao_eleitor = Eleitores.query.filter_by(loginPessoa=session['username'],votou=False).all()
            eleicoes_abertas = []

            for eleicao in eleicao_eleitor:
                e = Eleicao.query.filter_by(idEleicao=eleicao.idEleicao, estadoEleicao=True).all()
                for i in range(len(e)):
                    eleicoes_abertas.append(e[i])

            if len(eleicoes_abertas) > 0:
                for eleicao in eleicoes_abertas:
                    form.eleicoes.choices.append((eleicao.idEleicao,eleicao.tituloEleicao))

                return render_template('votar.html', title='Votação', form=form, menu = 2)
            else:
                flash("Nenhuma eleição aberta para voto. Consulte um administrador")
                return redirect(url_for('eleitor'))

        else:
            session['eleicao_para_votar'] = form.eleicoes.data
            return redirect(url_for('votacao'))

    elif session.get('logged_in') and session.get('admin'):
        flash('Voce precisa iniciar sessao como eleitor')
        return redirect(url_for('admin'))
    else:
        flash('Voce precisa iniciar sessao como eleitor')
        return redirect(url_for('autenticar'))

@app.route('/votacao', methods=['GET','POST'])
def votacao():
    if session.get('logged_in') and not session.get('admin'):
        if session.get('eleicao_para_votar') is not None:
            questoes = Questao.query.filter_by(idEleicao=session['eleicao_para_votar']).all()

            tupla_questao_resposta = []

            for questao in questoes:
                respostas = Resposta.query.filter_by(idQuestao=questao.idQuestao).all()
                lista_respostas = []
                for resposta in respostas:
                    lista_respostas.append(resposta)
                tupla_questao_resposta.append((questao, lista_respostas))

            if request.method == 'GET':

                return render_template('votacao.html', title='Votação', lista=tupla_questao_resposta, menu=2)

            else:

                dados = {}
                for campo in request.form.items():
                    if campo[0].split('-')[0] != 'submit':
                        dados[campo[0].split('-')[0]] = []

                for campo in request.form.items():
                    if campo[0].split('-')[0] != 'submit':
                        dados[campo[0].split('-')[0]].append(campo[0].split('-')[1])

                votos = []
                num_q = Questao.query.filter_by(idEleicao=session['eleicao_para_votar']).all()
                if len(dados.items()) < len(num_q):
                    flash('Você não respondeu todas as questões')
                    return redirect(url_for('eleitor'))

                for questao in dados.items():
                    q = Questao.query.filter_by(idQuestao=questao[0]).first()
                    if len(questao[1]) > q.respostasMaximasQuestao or len(questao[1]) < q.respostasMinimasQuestao:
                        flash('Numero de respostas inválidos em alguma questão')
                        return redirect(url_for('eleitor'))

                    for resposta in questao[1]:
                        voto = Votos(idQuestao=q.idQuestao, idResposta=resposta, idEleicao=session['eleicao_para_votar'])
                        votos.append(voto)

                eleitor = Eleitores.query.filter_by(loginPessoa=session['username'], idEleicao=session['eleicao_para_votar']).first()
                eleitor.votou = True
                db.session.add(eleitor)
                db.session.commit()
                for voto in votos:
                    db.session.add(voto)
                    db.session.commit()
                flash('Votos computados')
                return redirect(url_for('eleitor'))
        else:
            flash("Você deve selecionar a eleição antes")
            return redirect(url_for('votar'))

    elif session.get('logged_in') and session.get('admin'):
        flash('Voce precisa iniciar sessao como eleitor')
        return redirect(url_for('admin'))
    else:
        flash('Voce precisa iniciar sessao como eleitor')
        return redirect(url_for('autenticar'))

@app.route('/eleicao_resultados', methods=['GET', 'POST'])
def eleicao_resultados():
    if session.get('logged_in'):
        form = EleicaoParaVotarForm()
        if request.method == 'GET':

            eleicao_eleitor = Eleitores.query.filter_by(loginPessoa=session['username']).all()
            eleicoes_apuradas = []

            for eleicao in eleicao_eleitor:
                e = Eleicao.query.filter_by(idEleicao=eleicao.idEleicao, estadoEleicao=False, apuradaEleicao=True).all()
                for i in range(len(e)):
                    eleicoes_apuradas.append(e[i])


            if len(eleicoes_apuradas) > 0:
                for eleicao in eleicoes_apuradas:
                    form.eleicoes.choices.append((eleicao.idEleicao, eleicao.tituloEleicao))


                if session.get('admin'):
                    menu = 1
                else:
                    menu = 2

                return render_template('eleicao_resultados.html', title='Resultados', form=form, menu=menu)
            else:
                flash("Nenhuma eleição apurada. Consulte um administrador")
                if session.get('admin'):
                    return redirect(url_for('admin'))
                else:
                    return redirect((url_for('eleitor')))
        else:
            session['eleicao_resultado'] = form.eleicoes.data
            return redirect(url_for('resultados'))

    else:
        flash('Voce precisa iniciar sessao')
        return redirect(url_for('autenticar'))

@app.route('/resultados', methods=['GET'])
def resultados():
    if session.get('logged_in'):
        if (session.get('eleicao_resultado')) is not None:
            eleicao = Eleicao.query.filter_by(idEleicao=session['eleicao_resultado']).first()
            questoes = Questao.query.filter_by(idEleicao=eleicao.idEleicao).all()
            tupla = []

            for questao in questoes:
                respostas = Resposta.query.filter_by(idQuestao=questao.idQuestao, idEleicao=questao.idEleicao).all()
                tupla_r = []
                for resposta in respostas:
                    voto = Apuracao.query.filter_by(idEleicao=resposta.idEleicao, idQuestao=resposta.idQuestao,
                                                    idResposta=resposta.idResposta).first()
                    tupla_r.append((resposta.tituloResposta, voto.qtdVotos))
                tupla.append((questao.tituloQuestao,tupla_r))

            if session.get('admin'):
                menu = 1
            else:
                menu = 2
            return render_template('resultados.html', questoes=tupla, title='Resultados', eleicao=eleicao.tituloEleicao, menu=menu)

        else:
            flash("Você deve escolher a eleição antes.")
            return redirect(url_for('eleicao_resultados'))

    else:
        flash('Voce precisa iniciar sessao')
        return redirect(url_for('autenticar'))

@app.route('/')
def inicio():
    if session.get('logged_in') is not None:
        if (session.get('admin')):
            return admin()
        else:
            return eleitor()
    else:
        return render_template("index.html", menu=0)


if __name__ == '__main__':
    db.create_all()
    app.run(host='0.0.0.0', port=5000, debug=True)
