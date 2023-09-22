# Desafio Fullstack 1 -  Bootcamp Ada & Cielo 

# Requisitos

* [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)

# Configuração


Utilizamos o SDK AWS para interagir com os serviços SNS e SQS. Para isso, é necessário gerar credenciais para autenticação dos serviços.

Deve-se ter configurado um usuário do IAM AWS capaz de interagir com os serviços envolvidos, o que pode tornar necessário a criação de um IAM Group incluindo as Policies relativas ao SNS. 

A forma que utilizamos foi o arquivo padrão de credenciais AWS, por padrão o caminho é `~/.aws/credentials`, mas outras formas de autenticação podem ser utilizadas e estão descritas na [documentação oficial do SDK](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html).


