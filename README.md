# API Spring Boot 3.1.5 com Java 17

Este é um projeto de exemplo de uma API construída usando Spring Boot 3.1.5, Java 17, PostgreSQL como banco de dados, Docker para gerenciamento de contêineres, testes unitários com JUnit 5, integração contínua usando GitHub Actions, logs configurados, Flyway para controle de versionamento do banco de dados, ModelMapper para mapeamento de objetos, Lombok para redução de boilerplate, a utilização do padrão `@Embeddable`, autenticação e autorização com Spring Security e JWT.

## Pré-requisitos

- JDK 17 instalado
- Docker
- Docker Compose
- PostgreSQL

## Configuração do Banco de Dados

Este projeto utiliza o PostgreSQL como banco de dados. Certifique-se de ter um servidor PostgreSQL em execução localmente ou atualize as configurações em `application.yml` com as credenciais do seu banco de dados.

## Docker Compose

O Docker Compose é usado para orquestrar os contêineres necessários para a aplicação. Utilize o seguinte comando para construir e executar a aplicação em um ambiente isolado:

`docker-compose up`

## Integração Contínua com GitHub Actions
Este projeto utiliza GitHub Actions para integração contínua. Ao criar pull requests ou fazer push para a branch principal, os testes serão automaticamente executados.

## Autenticação e Autorização com Spring Security e JWT
A segurança é implementada utilizando o Spring Security com JSON Web Token (JWT) para autenticação e autorização de endpoints.

## Logs
Os logs estão configurados para registrar informações relevantes sobre o funcionamento da aplicação. Eles podem ser encontrados no arquivo de log específico da aplicação.

## Flyway para Controle de Versionamento do Banco de Dados
As migrações de banco de dados são controladas usando o Flyway. As migrações SQL podem ser encontradas no diretório src/main/resources/db/migration.

## ModelMapper e Lombok
O ModelMapper é usado para mapeamento de objetos, simplificando a transferência de dados entre as camadas da aplicação. O Lombok reduz o código boilerplate, tornando o código mais limpo e legível.

## Padrão @Embeddable
O padrão @Embeddable foi utilizado para facilitar a criação de objetos complexos embutidos em entidades.
