# Site de ofertas simplificado Peixe Urbano


Esse projeto foi desenvolvido para resolver o seguinte desafio técnico: https://github.com/PeixeUrbano/challenge-developer.

Foi desenvolvido usando as seguintes tecnologias e frameworks:

- Java;
- Springboot;
- ReactJs;
- Semantic-ui;


A aplicação foi dividida entre o projeto simple-deal-app e o projeto simple-deal-web, onde app serve o backend e web serve o frontend.

### Anatomia da aplicação

Possui duas telas principais. A tela inicial apresenta as ofertas cadastradas e um botão que permite a compra utilizando uma das opções de compra
cadastradas para a oferta. A tela de administrador apresenta a listagem de ofertas que permite cadastrar ou editar uma oferta e/ou uma opção de compra.


### Subindo a aplicação em modo desenvolvimento

Via linha de comando:

No diretório `/simple-deal-app` executar `mvn spring-boot:run`;

No diretório `/simple-deal-web` executar `npm install` e em seguida `npm start`;

Acessar http://localhost:3000;
 

### Subindo a aplicação em modo produção

Não consegui implementar essa parte ainda...

A ideia seria implementar um deployment pipeline no Travis CI para gerar um bundle do projeto web e um jar do projeto app, jogar tudo num docker
e subir num EC2 na AWS. Isso demoraria um pouco ser implementado.

### Problemas encontrados no caminho

Tive dificuldade em fazer o design do site, usei um framework com vários componentes mas nem todos atenderam as necessidades, obrigando a buscar
caminhos alternativos. 

Perdi bastante tempo fazendo o frontend, apesar de conhecer um pouco React passei por problemas tentando organizar o layout da melhor forma.


### Oportunidades de melhoria

- Implementar testes de frontend;
- Implementar deployment pipeline;
- Implementar autenticação básica;
- Implementar upload de imagem;
- Ajustes de layout;
- Melhorias nos feedbacks;