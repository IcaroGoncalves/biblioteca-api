# biblioteca‑api

> 📚 **Biblioteca API** — Gerencie livros, autores, usuários, empréstimos e reservas em Java 21 + Spring Boot.

---

## 🎯 Visão Geral

Esta API modular segue **Domain‑Driven Design** e **package‑by‑feature**, com camadas limpas:

- **autor**: cadastro de autores  
- **book**: cadastro de livros  
- **loan**: controle de empréstimos  
- **user**: cadastro de usuários  

Cada feature contém:
- `model` / entidade JPA  
- `dto` / transferência de dados  
- `repository` / interface Spring Data  
- `service` / regras de negócio  
- `controller` / endpoints REST  

---

## 📦 Estrutura de Pacotes

```text
src
└── main
    └── java
        └── com.biblio.biblioteca_api
            ├── autor
            │   ├── Autor.java
            │   └── AutorService.java
            ├── book
            │   ├── Book.java
            │   ├── BookDTO.java
            │   ├── BookRepository.java
            │   └── BookService.java
            ├── loan
            │   ├── Loan.java
            │   ├── LoanDTO.java
            │   ├── LoanRepository.java
            │   └── LoanService.java
            ├── user
            │   ├── Usuario.java
            │   ├── UsuarioDTO.java
            │   ├── UsuarioRepository.java
            │   └── UsuarioService.java
            └── BibliotecaApiApplication.java

```

### Requisição GET paginada para listar todos os livros

```bash
curl -X GET "http://localhost:8080/api/books?page=0&size=10&sort=titulo,asc" \
     -H "Accept: application/json"
{
  "content": [
    {
      "id": "a1b2c3d4-e5f6-47a8-9abc-def012345678",
      "titulo": "O Hobbit",
      "isbn": "9780547928227",
      "anoPublicacao": 1937,
      "exemplaresDisponiveis": 6
    },
    {
      "id": "b2c3d4e5-f6a7-48b9-0bcd-ef1234567890",
      "titulo": "Neuromancer",
      "isbn": "9780441569595",
      "anoPublicacao": 1984,
      "exemplaresDisponiveis": 4
    }
  ],
  "pageable": { /* detalhes de paginação */ },
  "totalElements": 5,
  "totalPages": 1,
  "last": true,
  "first": true,
  "number": 0,
  "size": 10
}
