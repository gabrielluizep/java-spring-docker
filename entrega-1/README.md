# Documentação das tecnologias escolhidas

### Tech Stack

- Docker
- Docker Compose
- RabbitMQ
- Java
- Java Spring Boot

### Cliente

A princípio, não será desenvolvido um cliente, apenas o servidor e os dispositivos inteligentes. O cliente poderá ser devnolvido caso haja tempo hábil. Uma alternativa é utilizar React com PWA para desenvolver um cliente web que possa ser acessado de qualquer dispositivo e instalado como um aplicativo.

### Servidor

Para o servidor, será desenvolvida uma API RESTful utilizando Java Spring Boot, de modo a tornar possível a comunicação entre o cliente e o servidor.

O servidor terá endpoints para realizar ações como listar e mandar ações dispositivos inteligentes e listar, criar, editar e apagar ambientes, um endpoint de registro para os dispositivos inteligentes e será responsável por guardar informações de estado dos dispositivos inteligentes e ambientes.

A comunicação com os dispositivos inteligentes será feita utilizando RabbitMQ, onde o servidor será o publisher e os dispositivos inteligentes serão os consumers, cada dispositivo inicialmente ser registrará no servidor, informando seu ID e suas ações, e as seguintes comunicações do servidor com o dispositivo serão feitas através de mensagens enviadas pelo RabbitMQ.

### Dispositivos inteligentes

Para os dispositivos inteligentes será desenvolvido em Java uma aplicação que fará a comunicação com o servidor por meio de RabbitMQ, onde o dispositivo inicalmente enviará para o servidor uma mensagem de registro para um endpoint e recebendo uma mensagem de sucesso continuará a comunicação com o servidor através do RabbitMQ esperando receber mensagens com ações a executar.

### Comunicação - Servidor / Dispositivos inteligentes

A comunicação entre servidor e dispositivos inteligentes será realizada por meio do RabbitMQ, sendo o servidor o publisher e os dispositivos inteligentes os consumers. O dispositivo inteligente encontrará o servidor através do nome do container que o servidor estará rodando no Docker Compose.

### Comunicação - Cliente / Servidor

A comunicação entre o cliente e o servidor será feita utilizando APIs RESTful. O cliente poderá acessar os endpoints disponibilizados pelo servidor para realizar as operações desejadas. Para que o cliente possa encontrar o servidor deve ser informado o endereço IP do servidor e porta de acesso ao inicializar o cliente.