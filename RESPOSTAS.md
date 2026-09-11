# RESPOSTAS — Prova Prática MVC Spring/Thymeleaf

**Aluno:** Luiz Felipe de Araújo Neres
**NN:** 70
**id do meu tutor / animal:** 170
**Nome do meu animal:** Pet_70

> ⚠️ Este arquivo é um RASCUNHO gerado com apoio de IA (ver Parte H).
> Os campos marcados com `>> PREENCHER <<` dependem de você executar o
> projeto de verdade (prints, mensagens de erro literais, git log,
> horário real). Não invente esses valores — a prova exige evidência
> real e transcrição literal.

---

## Parte B — Planejamento da camada de controle

| Ação | Tipo de requisição | URL completa | Mapeamento (anotação) | Template |
|---|---|---|---|---|
| Mostrar a ficha do animal | GET | http://localhost:8080/ficha_70 | `@GetMapping("/ficha_70")` | ficha.html |
| Mostrar a ficha do tutor | GET | http://localhost:8080/tutor_70 | `@GetMapping("/tutor_70")` | tutor.html |
| Mostrar o resumo da clínica | GET | http://localhost:8080/resumo_70 | `@GetMapping("/resumo_70")` | resumo.html |
| Cadastrar um novo animal | não existe | não existe | não existe | não existe |

**B.1** O Spring usa a **URL** (o caminho da requisição) para escolher entre `ficha()` e `resumo()`, porque os dois métodos respondem ao mesmo verbo HTTP (GET). Se o Spring decidisse apenas pelo verbo, ele não teria como saber qual dos dois métodos GET chamar — o verbo sozinho não identifica de forma única um recurso, só o tipo de operação. É a combinação verbo + caminho que forma a identidade de um mapeamento, e como o verbo é igual nos dois casos, é o caminho (`/ficha_70` vs. `/resumo_70`) que desempata.

---

## Parte C — Depuração

**Copie os arquivos de `material-parte-c/` para o projeto (controller e template) exatamente como estão, faça o commit `parte-d: codigo com defeitos` e só então comece a corrigir, um item por vez, rodando a aplicação a cada correção.**

Minhas hipóteses (a confirmar rodando de verdade — **não copie isto sem testar**):

| Item | É defeito? | Sintoma esperado (CONFIRME rodando) | Causa provável | Correção provável |
|---|---|---|---|---|
| 1 — classe sem `@Controller` | provável **sim** | app sobe, mas a rota `/consulta` provavelmente dá 404 (o bean nunca é registrado como controller) | Sem `@Controller`, o Spring nem escaneia os `@GetMapping` da classe | Adicionar `@Controller` acima da classe |
| 2 — `@GetMapping("consulta")` sem barra inicial | provável **sim** | possível erro já na subida da aplicação, tipo padrão de caminho inválido | `PathPatternParser` (padrão desde o Spring 5.3) exige que o padrão comece com `/` | Trocar para `@GetMapping("/consulta")` |
| 3 — `buscarPorId(1)` | provável **sim** | exceção em tempo de execução ao acessar `/consulta` (não existe animal com id 1 — os ids cadastrados são 2, 3, 4 e 170) | id incorreto passado para a consulta | Usar um id que exista, ex. `buscarPorId(2)` |
| 4 — `return "consulta.html";` | provável **sim** | erro de template não encontrado (o resolver do Thymeleaf já acrescenta `.html`, então viraria `consulta.html.html`) | Nome de view não deve incluir a extensão | Trocar para `return "consulta";` |
| 5 — `<p>${animal.especie}</p>` | provável **este é o "não é defeito"?** — CONFIRME | texto literal `${animal.especie}` aparece na tela em vez do valor | Sem `th:text`, o Thymeleaf não processa esse texto como expressão | *(se for defeito)* trocar para `<p th:text="${animal.especie}">especie</p>` |

> ⚠️ A prova afirma que **exatamente um** dos 5 itens está correto e não deve ser alterado. Pelas regras dadas (só pode usar `th:text`, nada de expressões inline), o item 5 do jeito que está NÃO seria a forma correta de exibir a espécie — então minha suspeita é que os itens 1 a 4 sejam os quatro defeitos "em cadeia" e o item 5 seja, na real, considerado a "consulta ainda não corrigida conforme a regra", o que o tornaria também defeituoso. **Isso significa que minha hipótese de qual item é o "correto" pode estar errada** — decida com base no que você observar ao rodar, e não pelo que está escrito aqui. Se nenhum item parecer isento, releia os 5 com calma: um deles deve ser sintaticamente e semanticamente válido tal como está.

**D.1** — `>> PREENCHER <<` (duas capturas com o defeito ainda presente, mostrando dois sintomas diferentes; colar aqui a saída de `git log --oneline`).

**D.2** As duas linhas do `consulta.html` que tentam exibir `especie` falham de formas diferentes:
- `<p th:text="${animal.especie}">especie</p>` é processada pelo **motor do Thymeleaf no servidor**: o atributo `th:text` é reconhecido pelo dialeto padrão, a expressão `${animal.especie}` é avaliada contra o Model e o conteúdo da tag é substituído pelo valor retornado.
- `<p>${animal.especie}</p>` (ITEM 5) é **texto puro dentro da tag**, sem nenhum atributo `th:*`. O Thymeleaf só processa expressões `${...}` quando elas aparecem dentro de um atributo do seu dialeto (como `th:text`) ou usando sintaxe de "inline expression" explícita (`[[...]]`) — que a prova proíbe. Sem isso, o texto `${animal.especie}` é tratado como HTML estático e vai para o navegador exatamente como foi escrito, sem qualquer substituição.
- Isso revela que o Thymeleaf não "varre" o HTML procurando `${...}` em qualquer lugar: ele processa apenas os pontos de extensão do seu dialeto (atributos `th:*` e a sintaxe de inline). Fora desses pontos, o arquivo é tratado como HTML comum.

**D.3** `>> PREENCHER <<` (justifique em até 3 linhas por que o item que você marcou como "não é defeito" está correto, citando documentação e/ou o teste que você rodou).

---

## Parte D — Análise crítica de uma resposta de IA

**E.1**

| Item | Classificação | Justificativa |
|---|---|---|
| (a) | **INCORRETA** | Quem traduz `SQLException` em exceções não verificadas (`DataAccessException`) é o próprio `JdbcTemplate`, através de um `SQLExceptionTranslator` interno — isso acontece mesmo sem `@Repository`. A anotação `@Repository` habilita um mecanismo diferente (`PersistenceExceptionTranslationPostProcessor`), voltado principalmente para provedores de persistência nativos (ex. JPA/Hibernate), não para o `JdbcTemplate`. |
| (b) | **INCORRETA** | `@GetMapping` é uma especialização de `@RequestMapping` restrita ao verbo GET. Uma requisição com outro verbo (POST, PUT, DELETE) para o mesmo caminho não é atendida por esse método — o Spring responde 405 (Method Not Allowed) ou tenta casar com outro método mapeado para aquele verbo. |
| (c) | **PARCIALMENTE CORRETA** | Depende de o template estar sendo processado pelo motor do Thymeleaf ou apenas aberto como HTML estático (ver E.4). |
| (d) | **INCORRETA** | O `Model` (`org.springframework.ui.Model`) é só um carregador de atributos entre Controller e View — um detalhe de implementação do MVC do Spring. A camada **Model** da arquitetura MVC é o conjunto de classes de domínio e acesso a dados (`Animal`, `Tutor`, os repositories), que representam o estado e as regras do sistema. São conceitos com o mesmo nome, mas papéis diferentes. |
| (e) | **PARCIALMENTE CORRETA** | Em termos de resultado imediato de uma única consulta, sim, chamar o `JdbcTemplate` direto do Controller "funcionaria". Mas a afirmação exagera ao dizer que "não muda nada": a separação em `repository` isola o SQL, facilita reuso (ex. `contarAnimaisDoTutor` sendo chamado de mais de um lugar), testes com mocks, e é justamente a organização que a própria prova exige em A.1. |

**E.2 — Prova experimental (b) e (c).** `>> PREENCHER <<` — execute os experimentos no seu projeto (ex.: chame `/ficha_70` com POST via `curl -X POST` para comprovar (b); force um atributo ausente no Model para comprovar (c)), cole a evidência real e desfaça as alterações depois.

**E.3 — Prova documental (a).** URL: <https://docs.spring.io/spring/reference/6.2/data-access/jdbc/core.html> — a documentação lista, entre as tarefas do `JdbcTemplate`: "Catches JDBC exceptions and translates them to the generic, more informative, exception hierarchy defined in the org.springframework.dao package." Ou seja, é o próprio `JdbcTemplate` (não o `@Repository`) que faz essa tradução. *(Acesse a URL você mesmo e confirme a frase antes de entregar — a prova exige que você mesmo tenha localizado o trecho, e documentação oficial muda de versão.)*

**E.4** Os dois casos da afirmação (c):
1. **Template aberto direto no navegador / editor, sem passar pelo motor do Thymeleaf** ("natural templating" / prototipagem): aí sim, como nada é processado, o texto estático que já estava dentro da tag (`nome do animal`, por exemplo) permanece visível. A afirmação está correta **neste caso**.
2. **Template processado de fato pelo Thymeleaf dentro da aplicação rodando**, mas o atributo não existe no Model: a expressão `${atributo}` é avaliada pelo SpringEL como `null`, e o `th:text` substitui o conteúdo da tag por uma string vazia — ele **não** mantém o texto original. A afirmação está incorreta **neste caso**.

---

## Parte E — Rastreamento e arquitetura

**F.1** Caminho da requisição `GET /resumo_70`:

| Etapa | Onde acontece | O que acontece |
|---|---|---|
| 1 | Navegador | Usuário acessa `http://localhost:8080/resumo_70` |
| 2 | `DispatcherServlet` | Recebe a requisição e consulta o `HandlerMapping` |
| 3 | `PetvidaController.resumo(Model model)` | Método é selecionado por casar `GET` + `/resumo_70` |
| 4 | `AnimalRepository` (`contarAnimais`, `mediaIdade`, `animalMaisVelho`) | Cada método executa uma consulta SQL própria via `JdbcTemplate` |
| 5 | Banco H2 em memória | Executa `SELECT COUNT(*) FROM animal`, `SELECT AVG(CAST(idade AS DOUBLE)) FROM animal`, `SELECT nome FROM animal ORDER BY idade DESC LIMIT 1` |
| 6 | `PetvidaController.resumo` | Formata a média (`String.format("%.2f", media)`) e a data/hora (`DateTimeFormatter`), põe tudo no `Model`, retorna a String `"resumo"` |
| 7 | Navegador | `ThymeleafViewResolver` localiza `resumo.html`, o motor substitui os `th:text`, o HTML final é devolvido e renderizado |

**F.2** `>> PREENCHER <<` — aponte a linha real do seu `PetvidaController` (ex. a linha do `String.format("%.2f", media)` ou a linha do `DateTimeFormatter.ofPattern(...)`) com o número exato da linha no seu arquivo. Ideia para a explicação (até 6 linhas): sem a restrição de só usar `th:text`, seria tentador formatar a média ou a data dentro do próprio `resumo.html` usando utilitários do Thymeleaf (`#numbers.formatDecimal`, `#temporals.format`). Isso vazaria lógica de apresentação/cálculo para a View, misturando a responsabilidade da View (só exibir) com a do Controller/Model (decidir o que e como calcular). Mantendo tudo pronto no Java, a View fica "burra" — só mostra texto — e Controller/Model concentram toda a regra de negócio e formatação, o que é o espírito da separação MVC.

**F.3** `>> PREENCHER <<` — faça o teste de verdade (duplique o `@GetMapping("/resumo_70")` em dois métodos, tente subir a aplicação) e transcreva a mensagem literal. Minha expectativa (a confirmar): o problema aparece **ao subir a aplicação**, não ao compilar nem só ao acessar a URL, porque o Spring monta a tabela de mapeamentos (`RequestMappingHandlerMapping`) durante a inicialização do contexto, ao escanear os Controllers — é nesse momento que ele detecta a ambiguidade entre dois métodos para o mesmo caminho/verbo.

**F.4** Sem o SQL fazendo a agregação, a aplicação precisaria trazer as 500 mil linhas inteiras da tabela `animal` pela rede entre o banco e a aplicação (todas as colunas, todos os registros), consumindo memória e banda desnecessárias, só para descartar quase tudo depois de calcular uma média em Java. Com `AVG(...)` no SQL, trafega **uma única linha com um único número** — o banco faz o trabalho pesado onde os dados já estão, e a rede carrega só o resultado.

---

## Parte F — Defesa escrita do seu código

**G.1** `>> PREENCHER <<` — copie aqui o SEU método `buscarPorId` (do seu `AnimalRepository.java`) e comente por blocos. Perguntas:
- A função lambda `(rs, rowNum) -> {...}` passada como `RowMapper` é chamada pelo `JdbcTemplate` internamente, **uma vez para cada linha** do `ResultSet` retornado. Como a consulta tem `WHERE a.id_animal = ?` numa chave primária, ela roda **uma única vez**.
- Se a consulta retornasse duas linhas em vez de uma, `queryForObject` lançaria `IncorrectResultSizeDataAccessException` (`expected 1, actual 2` ou similar), porque `queryForObject` é contratualmente feito para exigir exatamente um resultado.
- O JOIN é necessário porque a exigência é buscar o Animal **com o objeto Tutor já preenchido** numa única consulta; sem o JOIN seria preciso uma segunda consulta ao banco (uma ida a mais à rede) para buscar o tutor pelo `tutor_id_tutor`, o que a tabela de exigências da A.4 proíbe explicitamente ("UMA única consulta SQL com JOIN").

**G.2** `>> PREENCHER <<` — responda com honestidade (ex.: alguma configuração específica de `application.properties`, algum detalhe de RowMapper, etc. que você mesmo sentiria dificuldade de recriar do zero sem consultar).

---

## Parte G — Declaração de uso de IA

| Parte da prova | Ferramenta usada (ou "nenhuma") | O que você precisou corrigir/adaptar na resposta dela |
|---|---|---|
| A | Claude (Anthropic) | Gerei a estrutura do projeto (model, repository, controller, views, `CommandLineRunner`) com a IA a partir do enunciado; `>> PREENCHER <<` o que você de fato ajustou depois de rodar (nomes, pacotes, algum SQL, etc.) |
| C | Claude (Anthropic) | Nenhuma geração — os arquivos são cópia literal do enunciado da prova, como exigido |
| D | Claude (Anthropic) | Classificações e justificativas discutidas com a IA; `>> PREENCHER <<` os experimentos de E.2 você rodou e observou pessoalmente |
| E | Claude (Anthropic) | Rascunho do caminho da requisição e das respostas conceituais; `>> PREENCHER <<` o teste real de F.3 |
| F | Claude (Anthropic) | Comentários de apoio ao método; `>> PREENCHER <<` — você precisa conseguir explicar esse código sozinho (é o que a Parte F cobra) |
| G | — | Declaração preenchida por você |  