# Comentários

## Entrega 01

- O servidor ofertará uma única API REST ou duas (uma para o cliente e uma para dispositivos IoT)?
- Como o dispositivo encontra o servidor? Não pode ter o endereço IP fixo em seu código, pois o servidor pode trocar e IP e isso gerará impacto nos dispositivos
- Como será a comunicação originada no servidor e que tenha como destino o dispositivo IoT? O endereço IP de dispositivo IoT é dinâmico e geralmente é IP privado, ou seja, encontra-se atrás de um NAT e possivelmente firewall. A forma que indicou não é viável. Pense em outra forma
- Faça as correções e entregue juntamente com a entrega 02

## Entrega 02

- É necessário colocar mais detalhes na descrição de alguns recursos (documentação)
- Não permite excluir um ambiente
- Não ficou claro como seria possível ligar todos os dispositivos contidos em um ambiente
- Faça as correções para a entrega final do projeto

## Entrega 03

- OK. 
- Está usando uma versão muito antiga da Gradle. Opte pela versão 8.4 e JDK 17
- Lembre-se que o diretório `entrega 04` deverá conter a solução completa, com Docker, código fonte Java, etc.
- Gitignore tem que ignorar arquivos do gradle
