### Executando o projeto

Estando no diretório da entrega 4, execute o comando abaixo:

```bash
docker compose up
```

O comando executará o docker-compose.yml, criando a seguinte estrutura de containers:

- **rabbitmq**: RabbitMQ, responsável por receber as mensagens e distribuí-las para os consumidores.
- **server**: Servidor SpringBoot, responsável por receber as requisições HTTP e publicar as mensagens no RabbitMQ.
- **curtain**: Dispositivo IoT de cortina.
- **light**: Dispositivo IoT de lâmpada.
- **air-conditioner**: Dispositivo IoT de ar-condicionado.
- **tv**: Dispositivo IoT de televisão.
- **sound-system**: Dispositivo IoT de sistema de som.

> Os containeres usam uma utilidade [wait-for-it.sh](https://github.com/vishnubob/wait-for-it) para garantir que o servidor inicialize apenas quando o RabbitMQ estiver pronto para receber as mensagens e que os dispositivos IoT inicializem apenas quando o servidor estiver pronto para receber as requisições HTTP.

### Testando o projeto

- GET http://localhost:8080/devices

```bash
curl --request GET \
  --url http://localhost:8080/devices \
  --header 'Content-Type: application/json'
```

- GET http://localhost:8080/devices/{id}

```bash
curl --request GET \
  --url http://localhost:8080/devices/{id} \
  --header 'Content-Type: application/json'
```

- PUT http://localhost:8080/devices/{id}

```bash
curl --request PUT \
  --url http://localhost:8080/devices/1441 \
  --header 'Content-Type: application/json' \
  --data '{
	"operation": "setTemperature-99"
}'
```

- POST http://localhost:8080/rooms

```bash	
curl --request POST \
  --url http://localhost:8080/rooms \
  --header 'Content-Type: application/json' \
  --data '{
	"name": "sala",
	"devices": [1441, 5567]
}'
```

- GET http://localhost:8080/rooms

```bash
curl --request GET \
  --url http://localhost:8080/rooms \
  --header 'Content-Type: application/json'
```

- GET http://localhost:8080/rooms/{name}

```bash
curl --request GET \
  --url http://localhost:8080/rooms/{name} \
  --header 'Content-Type: application/json'
```

- PUT http://localhost:8080/rooms/{name}

```bash
curl --request PUT \
  --url http://localhost:8080/rooms/{name} \
  --header 'Content-Type: application/json' \
  --data '{
	"devices": [1441, 5567, 9287]
}'
```

- PUT http://localhost:8080/rooms/{name}

```bash
curl --request PUT \
  --url http://localhost:8080/rooms/sala \
  --header 'Content-Type: application/json' \
  --data '{
	"power": false
}'
```