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
