# Chat API em Tempo Real (Spring Boot + WebSocket)

Este projeto é uma **API de chat em tempo real** construída com **Spring Boot**, usando **WebSocket** para comunicação instantânea, **REST** para login e histórico de mensagens, e **JPA/Hibernate** para persistência.

O foco do projeto é **didático e incremental**, seguindo uma arquitetura simples, clara e organizada por responsabilidade.

---

## Visão Geral

Funcionalidades principais:

* Login simples por `username` (cria o usuário automaticamente se não existir)
* Comunicação em tempo real via WebSocket
* Registro de presença (usuário online/offline)
* Envio e persistência de mensagens
* Consulta de histórico de conversa entre dois usuários

Não há autenticação com senha ou token neste estágio — isso é **intencional**, para manter o escopo enxuto e facilitar evolução futura (JWT, Spring Security, etc).

---

## Arquitetura de Pacotes

```
com.example.demo
├── auth        # Login e DTOs de autenticação
├── user        # Entidade User e repositório
├── message     # Mensagens, repositório e serviço
├── history     # Controller de histórico de chat
├── presence    # Controle de usuários online
├── websocket   # WebSocket handler e configuração
```

A separação segue o princípio de **responsabilidade única**:

* Controllers lidam apenas com HTTP/WebSocket
* Services concentram regras de negócio
* Repositories lidam com persistência
* Entidades representam o domínio

---

## Tecnologias Utilizadas

* Java 21+
* Spring Boot
* Spring Web
* Spring WebSocket
* Spring Data JPA
* Hibernate
* Lombok
* Jackson
* Banco relacional (H2, PostgreSQL, MySQL, etc)

---

## Fluxo de Login

### Endpoint

```
POST /login
```

### Request Body

```json
{
  "username": "kayk"
}
```

### Regras

* O username é normalizado (`trim + lowercase`)
* Se o usuário existir, ele é reutilizado
* Se não existir, um novo usuário é criado automaticamente

### Response

```json
{
  "userId": "uuid-gerado",
  "username": "kayk"
}
```

Esse endpoint serve como **identificação inicial do usuário** para uso posterior no WebSocket.

---

## WebSocket (Chat em Tempo Real)

### Endpoint WebSocket

```
/ws/chat?userId={UUID}
```

O `userId` é obrigatório e é usado para:

* Marcar o usuário como online
* Identificar o remetente das mensagens

### Payload de Mensagem

```json
{
  "receiverId": "uuid-do-destinatario",
  "content": "Olá!"
}
```

### Comportamento

* Mensagem é persistida no banco
* Se o destinatário estiver online:

  * A mensagem é enviada em tempo real via WebSocket
* Se estiver offline:

  * A mensagem fica salva para histórico

---

## Presença (Usuários Online)

O controle de presença é feito em memória usando `ConcurrentHashMap`.

### Responsabilidades

* Registrar usuários ao conectar no WebSocket
* Remover usuários ao desconectar
* Verificar se um usuário está online
* Obter a sessão WebSocket de um usuário

Isso permite:

* Saber quem está online
* Enviar mensagens apenas se o usuário estiver conectado

---

## Histórico de Mensagens

### Endpoint

```
GET /messages/history?userAId={UUID}&userBId={UUID}
```

### Resposta

Lista de mensagens ordenadas por data (ASC), contendo:

* Remetente
* Destinatário
* Conteúdo
* Timestamp

Esse endpoint retorna **toda a conversa entre dois usuários**, independentemente de quem enviou.

---

## Modelo de Dados

### User

* `id` (UUID)
* `username` (único)

### Message

* `id` (UUID)
* `sender` (User)
* `receiver` (User)
* `content` (String)
* `timestamp` (LocalDateTime)

---

## Pontos Fortes do Projeto

* Arquitetura simples e clara
* Separação correta de responsabilidades
* Uso real de WebSocket (não simulação)
* Código fácil de evoluir para:

  * JWT
  * Spring Security
  * STOMP
  * Notificações
  * Chat em grupo

---

