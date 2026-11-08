# PetShop

Projeto em Java para cadastro e gerenciamento de tutores e seus pets, com interface gráfica (Swing) e persistência em arquivo.

## Arquivos

- `PetShop.java`: contém apenas o `main`, que abre a interface.
- `PetInterface.java`: janela principal, telas de cada operação, tratamento de exceções e gravação/leitura do arquivo.
- `Tutor.java`: dados do tutor e a sua lista de pets.
- `Pet.java`: dados de cada animal, incluindo a data de nascimento (real ou estimada) e o cálculo da idade.
- `pets.png`: faixa decorativa exibida na tela principal.
- `PetShop.dat`: arquivo de dados (gerado automaticamente).

O projeto foi feito sem pacotes e sem bibliotecas externas.

## Funcionalidades

Cada opção é acionada por um botão da tela principal:

- cadastrar tutor com um ou mais pets
- imprimir todo o cadastro
- buscar tutor pelo código
- excluir tutor e todos os seus pets
- excluir um pet de um tutor (o tutor é excluído se ficar sem pets)
- encerrar o aplicativo

As datas de nascimento são digitadas em campos separados (dia, mês e ano) e precisam existir no calendário, ter ano positivo e não estar no futuro.

## Persistência

- Ao iniciar, o cadastro é lido de `PetShop.dat`. Se o arquivo não existir, é feito um cadastramento prévio e o arquivo é criado.
- O cadastro é gravado após cada alteração. Se a gravação falhar, a alteração é desfeita e o usuário é avisado.
- Se o arquivo existir mas não puder ser lido, nada é gravado, para não sobrescrever os dados.

## Como executar

Compile e execute na pasta do projeto (onde estão `pets.png` e `PetShop.dat`):

```bash
javac *.java
java PetShop
```