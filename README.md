
# Projeto Mensageria - Serviços

## Serviços e Descrição

### order-service
Serviço que produz pedidos (Orders) e os publica no tópico Kafka para serem consumidos pelo serviço de estoque.

### inventory-service
Projeto completo que gerencia o estoque de produtos eletrônicos. Consome pedidos via Kafka, atualiza o estoque e publica eventos de inventário.

### notification
Serviço consumidor que lê eventos de inventário e envia notificações simuladas (e-mails/SMS) com o resultado da reserva de estoque.

### shared-models
Módulo compartilhado que contém os modelos comuns, como a classe `Order`, usada entre os demais serviços.

---

## Requisitos e Execução

### Requisitos
- Java 17
- Apache Kafka
- Maven
- Spring Boot

### Como executar
1. Certifique-se que o Kafka está rodando e os tópicos necessários foram criados: `orders` e `inventory-events`.
    ```bash
   bin/zookeeper-server-start.sh config/zookeeper.properties
   bin/kafka-server-start.sh config/server.properties 
   ```
3. Buildar todos os módulos com Maven:
   ```bash
   cd shared-models && mvn clean install
   cd ../order-service && mvn clean install
   cd ../inventory-service && mvn clean install
   cd ../notification && mvn clean install
   ```
4. Inicie os serviços em qualquer ordem (recomenda-se iniciar o Kafka antes):
     ```bash
   cd shared-models && mvn install
   cd ../order-service && mvn spring-boot:run
   cd ../inventory-service && mvn spring-boot:run
   cd ../notification && mvn spring-boot:run

   ```
5. Abra o endereco na web http://localhost:8082/ para visualizar o inventario

 ![image](https://github.com/user-attachments/assets/f2a516c4-5889-49f9-9921-778867ee1615)

---

## Conceitos de Engenharia de Software

### 1. Escalabilidade
Com o broker Kafka, a escalabilidade é alcançada através do particionamento dos tópicos. Cada partição pode ser consumida por um consumidor diferente dentro de um grupo, permitindo processamento paralelo e aumentando a capacidade do sistema para suportar mais pedidos simultaneamente.

### 2. Tolerância à falha
Tolerância à falha significa que o sistema continua operando mesmo diante de falhas de hardware, software ou rede. Um exemplo é quando um consumidor Kafka falha; o broker detecta isso e redistribui as partições para outros consumidores do grupo, garantindo que as mensagens continuem sendo processadas.

### 3. Idempotência
Idempotência é a propriedade que garante que a aplicação de uma operação múltiplas vezes tem o mesmo efeito que aplicá-la uma única vez. Para garantir idempotência em um sistema Kafka, o consumidor deve processar cada mensagem com uma lógica que evite efeitos duplicados, por exemplo, armazenando os IDs das mensagens já processadas para não executar a mesma ação duas vezes.

---

## Equipe
- Abraão Santiago Moreira

---
