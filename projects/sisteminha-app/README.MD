# SISTEMINHA: Cadastro de PDV

Este projeto surgiu de um desafio de uma vaga pra fullstack web developer. A stack é formada por __React + SpringBoot__. 

## Sobre o desafio

O desafio era desenvolver uma aplicação com autenticação basic e CRUDs de PDVs (pontos de venda) e novos usuários. Requisitos era que fosse desenvolvido com React, Springboot, Gradle, banco de dados em memória, deveria ter tela de login, alguns testes. Diferencial seria separar o backend do frontend e utilizar docker-compose.

## Como construir e subir o projeto

- Dar permissão ao arquivo na raíz *start-server.sh* e em seguida executar:
```
chmod +x ./start-server.sh
./start-server.sh
```

- Duas novas janelas do terminal vão se abrir, uma com o processo do backend e outra com o processo do frontend, só monitorar daí.

Usuário e senha padrão: admin admin;

## Características do projeto

O projeto está separado em sisteminha-frontend e sisteminha-backend. O frontend é feito em React com Material-ui, é usado também o react-router, universal-cookie e moment.js. O backend é um projeto Gradle com springboot, usa um plugin que gera um uberjar com as dependencias do projeto.

No backend o CORS está liberado pra qualquer método HTTP em qualquer URL. 

A autenticação é basic, usa um customAuthenticator que faz uma consulta na base pelo usuário com login, senha e que esteja ativo.


## Considerações finais

O projeto ainda tem melhorias a serem feitas, como melhorar o tratamento de erros, testes de frontend, adicionar build pipeline, mas é um ótimo esqueleto para prototipar uma aplicação nova. 

Próximos passos são adicionar build pipeline, adicionar integração com Heroku, configurar docker-compose.
