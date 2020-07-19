# _Ficticius Clean_ - API de carros

#### O desafio aqui era desenvolver uma API com os seguintes requisitos:

Criar uma API Rest de cadastro de veículos para armazenar os veículos utilizados pela
empresa. O cadastro deverá conter os seguintes dados:
* Nome
* Marca
* Modelo
* Data de fabricação
* Consumo Médio de combustível dentro de cidade (KM/L)
* Consumo Médio de combustível em rodovias (KM/L)

Criar uma API para realizar o cálculo de previsão de gastos.
Deverá receber como parâmetro as seguintes informações:
* Preço da gasolina R$
* Total de km que será percorrido dentro da cidade
* Total de km que será percorrido em rodovias

O retorno deverá ser uma lista ranqueada dos veículos da empresa levando em
consideração o valor gasto com combustível. 
O retorno deverá ter os seguintes dados:
* Nome
* Marca
* Modelo
* Ano
* Quantidade de combustível gasto
* Valor total gasto com combustível


### Stack

Foi usado SpringBoot, o projeto foi criado usando o Spring Initializr, as seguintes dependências foram adicionadas no projeto:
* Spring Data JPA;
* Spring Web;
* Spring Devtools;
* Lombok;
* H2;
* Swagger e Swagger UI;


### Database

Foi usado um _database in memory_ conhecido, o H2.

Pra popular a base baixei na internet 2 arquivos csv contendo uma lista de marcas e uma lista de carros, nessas linhas não tinha o modelo, a data de fabricação nem consumos médios de cidade e estrada. Juntei as duas listas em tabelas num banco postgres e fiz um script pra imprimir cada linha de Insert, gerando como modelo o valor fixo "Standard" e datas de fabricação e consumos médios randomicos. A data varia 10000 dias e os consumos médios de cidade variam de 6 a 10 e de rodovia de 11 a 15.

```sql
// Query que gera as linhas de insert usadas no data.sql para popular a base
select 'INSERT INTO COMPANY_CARS (uuid, name, brand, model, manufacturingDate, averageCityConsumption, averageHighwayConsumption) values ('
       || '''' || uuid_generate_v4() || ''','
       || '''' || carro.nome || ''','
       || '''' || marca.nome || ''','
       || '''' || 'Standard' || ''','
       || '''' || NOW() - (random() * (interval '10000 days')) + '30 days' || ''','
       || '''' ||  (random() * (6-10) + 10) || ''','
       || '''' || (random() * (11-15) + 15) || '''' || ');'
       from marcas marca  join carros carro  on marca.id = carro.id_marca;
```


### Subindo o projeto

* Certificar-se de ter o JDK 8 instalado.
* Na raiz do projeto, executar `mvn clean install`;
* Considerando que o .jar foi gerado na pasta /target, executar `java -jar ./target/fc-api-0.0.1-SNAPSHOT.jar`


### Solução do problema

Para a primeira API do desafio foi criado um CRUD básico, com os verbos HTTP referentes a cada ação;

Para a segunda API do desafio foi feita uma conta direto na query HQL, meio ruim de testar, mas eu fiz prova real dos testes na caneta e calculadora. A conta foi a seguinte: 
```
(Total KM cidade \ Media KM.l cidade) + (Total KM rodovia \ Média) = Total Combustível no trajeto;
Total Combustível no trajeto * Valor por litro = Total Consumo no trajeto por carro;
```

### Documentação da API

A documentação da API pode ser acessada pela url http://localhost:8080/swagger-ui.html.


### Melhorias possíveis

Vocês podem notar que não fiz testes. Então como melhoria faria testes unitários usando Mockito, testes de integração usando a própria ferramenta de testes do Spring, e em ambos os casos uando a tecnica _AAA_.

Adicionaria também uma validação dos models recebidos nas requisições.

Numa aplicação do mundo real melhoraria a descrição dos endpoints.


### Considerações finais

O app está hospedado num Dyno free do Heruko, pode ser acessado pela url https://ficticious-clean-api.herokuapp.com/. Obs: o primeiro acesso demora alguns minutos para a máquina ligar.

Obrigado por terem me acompanhado até aqui. Acho que o projeto atinge o objetivo do desafio muito bem e gostei do resultado final.

Vocês sempre podem me encontrar em http://cleitoncardoso.com/ e nas minhas redes sociais.
