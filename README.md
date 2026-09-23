# PetShop

Projeto em Java para cadastro e gerenciamento de tutores e seus pets.

## Objetivo

O sistema permite manter um cadastro simples de tutores e animais, usando somente as classes:

- `Pet.java`
- `Tutor.java`
- `PetShop.java`

O projeto foi feito sem pacotes e com recursos básicos da linguagem, conforme as orientações da disciplina.

## Funcionalidades

- cadastrar tutor com um ou mais pets
- listar todos os cadastros
- buscar tutor pelo código
- excluir tutor e todos os seus pets
- excluir um pet de um tutor
- cadastro inicial preenchido automaticamente ao iniciar o programa

## Como funciona

- `PetShop` controla o menu principal e as operações do sistema.
- `Tutor` guarda os dados do tutor e a lista de pets.
- `Pet` representa cada animal cadastrado.

## Como executar

Compile e execute os arquivos Java na mesma pasta:

```bash
javac Pet.java Tutor.java PetShop.java
java PetShop
```