# Compilador

Compilador é um processador de linguagens. Um programa que traduz um programa escrito em uma linguagem (linguagem fonte) e gera um programa equivalente escrito em outra linguagem (linguagem alvo ou linguagem objeto). <br /> <br />

Suas funcionalidades são divididas em duas etapas: análise e síntese. <br />

Análise <br />
> * Também denominada de front-end do compilador. <br />
> * O compilador subdivide o programa fonte e o submete a análise a fim de verificar sua correção conforme a linguagem de programação fonte. <br />
> * Compreende análise léxica, análise sintática e análise semântica.  <br />
> * Na presença de uma não conformidade com as especificações da linguagem fonte, o compilador apresenta mensagem esclarecedora do erro. <br />
> * O compilador armazena informações sobre o programa fonte em uma estrutura denominada tabela de símbolos. <br />

Síntese <br />
> * Também denominada de back-end do compilador. <br />
> * O compilador constroi o programa objeto com base na representação intermediária (código intermediário) e nas informações da tabela de símbolos. <br />
> * Alguns compiladores podem realizar também otimizações nos código gerados <br />

## Análise Léxica

* A primeira fase do compilador é a análise léxica ou leitura (scanning). <br />
* O analisador léxico lê o arquivo do programa fonte, caractere a caractere, e os agrupa em sequências significativas denominadas lexemas. <br />
* Para cada lexema obtido do código fonte, o analisado léxico gera um token no formato abaixo. Esse token é passado para o analisador sintático. <br />

Nela, também foi implementada a tabela de símbolos, uma estrutura de dados na qual são armazenadas informações sobre os nomes utilizados no programa: <br />
* Nome, tipo, escopo, espaço de memória alocado <br />
* No caso de funções: quantidade e tipos de argumentos , tipo de retorno, método de passagem de parâmetro <br /><br />

A estrutura deve ser projetada de forma a permitir que o compilador armazene e recupere rapidamente dados de nomes encontrados no programa fonte. <br />

## Análise Sintática

A segunda fase do compilador é a análise sintática. Ele recebe como entrada os tokens identificados pelo analisador léxico e cria uma representação da estrutura gramatical da sequência de tokens. <br />
> * A representação utilizada pelo analisador sintático é uma árvore sintática. <br />
> * O analisador sintático verifica se a ordem em que os tokens aparecem no código fonte está de acordo com a sintaxe da linguagem. <br />

## Análise Semântica

A terceira fase do compilador. O analisador semântico utiliza a tabela de símbolos e a árvore sintática gerada pelo analisador sintático para verificar se o programa está semanticamente de acordo com as especificações da linguagem fonte. <br />

Sua atividade principal realizada pelo analisador semântico é a verificação de tipos: consiste em verificar se cada operador possui operandos compatíveis <br />

## Geração de Código Intermediário
É uma representação intermediária (código intermediário) de baixo nível pode ser gerada pelo compilador a fim de facilitar a tradução para a linguagem alvo. <br />

-> Exemplo de representação intermediária: código de três endereços. <br />
> * Sequência de instruções do tipo assembly <br />
> * Cada instrução possui até três operandos <br />
> * Cada instrução de atribuição possui no máximo um operador do lado direito <br />
> * O compilador precisa gerar um nome temporário para guardar o valor computado <br />


### Execução
Para a execução do arquivo utilize o codigo no terminal:

```* java -jar /ArquivoJAR/Compilador.java```

Alunos: <br />
Bruna Gomes <br />
Diego Simões <br />
Marina Bernardes <br />
