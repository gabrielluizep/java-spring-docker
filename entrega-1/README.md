# Documentação das tecnologias escolhidas

### Tech Stack

- Docker
- Java
  - Spring Boot

### Cliente

A princípio, não será desenvolvido um cliente, apenas o servidor e os dispositivos inteligentes. O cliente poderá ser devnolvido caso haja tempo hábil. Uma alternativa é utilizar React com PWA para desenvolver um cliente web que possa ser acessado de qualquer dispositivo e instalado como um aplicativo.

### Servidor

O servidor será desenvolvido utilizando Java com Spring Boot, para comportar a API RESTful que será disponibilizada para os clientes e dispositivos inteligentes. O servidor será executado em um contêiner Docker e terá intercomunicação simulada com os dispositivos inteligentes por meio do `docker-compose`.

### Dispositivos inteligentes

Cada dispositivo inteligente será desenvolvido utilizando Java com Spring Boot, para comportar a API RESTful que será disponibilizada para o servidor. Os dispositivos inteligentes serão executados em contêineres Docker.

### Comunicação - Servidor / Dispositivos inteligentes

A comunição entre o servidor e os dispositivos inteligentes será feita utilizando APIs RESTful. Para que um dispositivo inteligente possa se comunicar com o servidor, ele deve primeiramente realizar um registro, acessando o endpoint `/register` do servidor, onde ele passará as informações de tipo de dispositivo, e operações que ele é capaz de realizar, para cada operação será passado o nome da operação e o endpoint de acesso para realizar a operação.

Para que o dispositivo possa encontrar o servidor deve ser informado o endereço IP do servidor e porta de acesso ao inicializar o dispositivo. Para que o servidor possa enviar requisições ao dispositivo, ao realizar o registro será persistido o endereço IP e porta do dispositivo.

### Comunicação - Cliente / Servidor

A comunicação entre o cliente e o servidor será feita utilizando APIs RESTful. O cliente poderá acessar os endpoints disponibilizados pelo servidor para realizar as operações desejadas. Para que o cliente possa encontrar o servidor deve ser informado o endereço IP do servidor e porta de acesso ao inicializar o cliente.