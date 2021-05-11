public class Labirinto { 
    private static final char LINHA = 10; 
    private static final char COLUNA = 10; 
    private static final double PROBABILIDADE = 0.7;

    private static final char PAREDE_VERTICAL = '│'; 
    private static final char PAREDE_HORIZONTAL = '─'; 
    private static final char PAREDE_SUPESQ = '┌';
    private static final char PAREDE_INFDIR = '┘';
    private static final char PAREDE_SUPDIR = '┐';
    private static final char PAREDE_INFESQ = '└';
    
    //┌ ─ ┐  └ ─ ┘ │ ☺ ☻ ♦ ☀ † ◦ ▪ ⸙
    private static final char PAREDE_INTERNA = '⸙';
    private static final char VAZIO = ' '; 
    private static final char INICIO = '☻';
    private static final char DESTINO = '†';
    private static final char CAMINHO = '◦';
    private static final char SEM_SAIDA = '▪';

    private static char[][] tabuleiro;
    private static int horizontal;
    private static int vertical;
    private static int linhaInicio;
    private static int colunaInicio;

    public static void main(String Arg[]) throws InterruptedException { 

		tabuleiro = new char[LINHA][COLUNA]; 
		inicializarMatriz(); 
		imprimir(); 
        System.out.println("\n- Procurando solução -\n"); 

	    boolean achou = procurarCaminho(linhaInicio, colunaInicio); 
	    if (achou) { 
		System.out.println("ACHOU O CAMINHO!"); 
	    } else { 
		System.out.println("Não tem caminho!"); 
        }
	}

    // Cria paredes, caminhos possiveis, destino e ponto de partida
    public static void inicializarMatriz() { 
		int i, j; 

        //Cantos vertices
        tabuleiro[0][0] = PAREDE_SUPESQ;
        tabuleiro[0][COLUNA - 1] = PAREDE_SUPDIR;
        tabuleiro[LINHA - 1][COLUNA - 1] = PAREDE_INFDIR;
        tabuleiro[LINHA - 1][0] = PAREDE_INFESQ;

        // Paredes  ESQUERDA  DIREIRA
        for (int a = 1; a < LINHA - 1; a++) {
            tabuleiro[a][0] = PAREDE_VERTICAL;
            tabuleiro[a][COLUNA - 1] = PAREDE_VERTICAL;            
        }

        // Paredes SUPERIOR INFERIOR
        for (int b = 1; b < COLUNA - 2; b++) {
            tabuleiro[0][b] = PAREDE_HORIZONTAL; 
            tabuleiro[LINHA - 1][b] = PAREDE_HORIZONTAL;  			
        }


        // Preencer obstatulos de arvores e vazios ' '
		for (i = 1; i < LINHA - 1; i++) {
            for (j = 1; j < COLUNA - 2; j++) {
                if (Math.random() > PROBABILIDADE) {
                    tabuleiro[i][j] = PAREDE_INTERNA;
                } else {
                    tabuleiro[i][j] = VAZIO;
                }
            }
        }

        // Colocar ponto de inicio e destino
        linhaInicio = gerarNumero(1, LINHA / 2 - 2);
        colunaInicio = gerarNumero(1, COLUNA / 2 - 2);
        tabuleiro[linhaInicio][colunaInicio] = INICIO;
        
        int linhaDestino = gerarNumero((LINHA - 1) / 2, (LINHA - 1) - 2);
        int colunaDestino = gerarNumero((COLUNA - 1) / 2, (COLUNA - 1) - 2);
        tabuleiro[linhaDestino][colunaDestino] = DESTINO; 
	}
    
    // Gera numeros aleatorios
    public static int gerarNumero(int minimo, int maximo) {
        int valor = (int) Math.round(Math.random()  * (maximo - minimo));
        return minimo + valor;
    }

    //Imprime a MATRIZ
    public static void imprimir() throws InterruptedException { 
        Thread.sleep(350);
		for (int i = 0; i < LINHA; i++) { 
			for (int j = 0; j < COLUNA; j++) { 
				System.out.print(tabuleiro[i][j]); 
			} 
			System.out.println(); 
		} 
	}
   
    // Tenta mudar de posicao 
    public static boolean procurarCaminho(int linhaAtual, int colunaAtual) throws InterruptedException { 
        int proxLinha; 
        int proxColuna; 
        boolean achou = false; 

        //Antiga posicao
        horizontal = linhaAtual;
        vertical = colunaAtual;

        // tenta subir 
        proxLinha = linhaAtual - 1; 
        proxColuna = colunaAtual; 
        achou = tentarCaminho(proxLinha, proxColuna);

        // tenta descer 
        if (!achou) { 
            proxLinha = linhaAtual + 1; 
            proxColuna = colunaAtual; 
            achou = tentarCaminho(proxLinha, proxColuna); 
        } 

        // tenta à esquerda 
        if (!achou) { 
            proxLinha = linhaAtual; 
            proxColuna = colunaAtual - 1; 
            achou = tentarCaminho(proxLinha, proxColuna); 
        } 

        // tenta à direita 
        if (!achou) { 
            proxLinha = linhaAtual; 
            proxColuna = colunaAtual + 1; 
            achou = tentarCaminho(proxLinha, proxColuna);
        } 
        return achou; 
    }

    // Verifica se o caminho esta livre
    private static boolean tentarCaminho(int proxLinha, int proxColuna) throws InterruptedException { 
        boolean achou = false; 
        if (tabuleiro[proxLinha][proxColuna] == DESTINO) { 
            achou = true; 
        } else if (posicaoVazia(proxLinha, proxColuna)) { 
            tabuleiro[proxLinha][proxColuna] = CAMINHO;
            imprimir();
            achou = procurarCaminho(proxLinha, proxColuna); 
            if (!achou) { 
                tabuleiro[proxLinha][proxColuna] = SEM_SAIDA;
                tabuleiro[horizontal][vertical] = SEM_SAIDA;
                imprimir();
            } 
        } 
        return achou; 
    }

    // Checa se posicao esta vazia
    public static boolean posicaoVazia(int linha, int coluna) { 
        boolean vazio = false; 
        if (linha >= 0 && coluna >= 0 && linha < LINHA && coluna < COLUNA) { 
            vazio = (tabuleiro[linha][coluna] == VAZIO);  
        }
        return vazio; 
    }
}