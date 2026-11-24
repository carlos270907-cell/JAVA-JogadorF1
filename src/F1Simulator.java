import java.util.Random;
import java.util.Scanner;

public class F1Simulator {


    static final Scanner sc = new Scanner(System.in);
    static final Random random = new Random();

    static final int CUSTO_TREINO = 300;
    static final int HABILIDADE_MAXIMA = 100;
    static final int TAMANHO_HISTORICO = 5;

    // Estado do piloto
    static String nome;
    static int habilidade = 50;
    static int pontos = 0;
    static int saldo = 500;
    static int corridas = 0;

    // Desafio extra: histórico das últimas 5 corridas
    static int[] historico = new int[TAMANHO_HISTORICO];
    static int qtdHistorico = 0;

    // ===================== LEITURA =====================
    static int lerInteiro() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ===================== MÉTODOS OBRIGATÓRIOS =====================
    public static int exibirMenu() {
        System.out.println("\n========== JAVA F1 SIMULATOR ==========");
        System.out.println("Piloto: " + nome + " | Habilidade: " + habilidade);
        System.out.println("Pontos: " + pontos + " | Saldo: R$ " + saldo);
        System.out.println("1 - Visualizar piloto");
        System.out.println("2 - Treinar piloto");
        System.out.println("3 - Participar de corrida");
        System.out.println("4 - Consultar resultados");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
        return lerInteiro();
    }

    public static void mostrarPiloto(String nome, int habilidade, int pontos, int saldo, int corridas) {
        System.out.println("\n--- Piloto ---");
        System.out.println("Nome: " + nome);
        System.out.println("Habilidade: " + habilidade);
        System.out.println("Pontuação: " + pontos);
        System.out.println("Dinheiro disponível: R$ " + saldo);
        System.out.println("Corridas realizadas: " + corridas);
    }

    // Retorna a habilidade após a tentativa (sorteio de 1 a 10: 6 ou mais funciona)
    public static int treinar(int habilidade, int sorteio) {
        if (sorteio >= 6) {
            return Math.min(HABILIDADE_MAXIMA, habilidade + 5);
        }
        return habilidade;
    }

    // desempenho = habilidade + bônus + sorte (sorte de 0 a 10 gerada aqui)
    public static int calcularDesempenho(int habilidade, int bonus) {
        int sorte = random.nextInt(11);
        return habilidade + bonus + sorte;
    }

    public static int calcularPontos(int desempenho) {
        if (desempenho >= 90) return 25;
        if (desempenho >= 75) return 18;
        if (desempenho >= 60) return 15;
        return 10;
    }

    public static int calcularPremio(int desempenho) {
        if (desempenho >= 90) return 1000;
        if (desempenho >= 75) return 700;
        if (desempenho >= 60) return 500;
        return 300;
    }

    // ===================== AUXILIARES =====================
    public static String calcularPosicao(int desempenho) {
        if (desempenho >= 90) return "1º lugar";
        if (desempenho >= 75) return "2º lugar";
        if (desempenho >= 60) return "3º lugar";
        return "5º lugar";
    }

    public static int bonusEstrategia(int estrategia) {
        if (estrategia == 1) return 0;
        if (estrategia == 2) return 5;
        return 10; // 3
    }

    public static String nomeEstrategia(int estrategia) {
        if (estrategia == 1) return "Conservadora";
        if (estrategia == 2) return "Equilibrada";
        return "Agressiva";
    }

    // Guarda os pontos da corrida sem ultrapassar a capacidade do vetor
    public static void registrarHistorico(int pontosCorrida) {
        if (qtdHistorico == TAMANHO_HISTORICO) {
            for (int i = 1; i < TAMANHO_HISTORICO; i++) {
                historico[i - 1] = historico[i];
            }
            historico[TAMANHO_HISTORICO - 1] = pontosCorrida;
        } else {
            historico[qtdHistorico] = pontosCorrida;
            qtdHistorico++;
        }
    }

    // ===================== OPÇÕES DO MENU =====================
    public static void opcaoTreinar() {
        if (habilidade >= HABILIDADE_MAXIMA) {
            System.out.println("Habilidade já está no máximo (100)!");
            return;
        }
        if (saldo < CUSTO_TREINO) {
            System.out.println("Saldo insuficiente! Treinar custa R$ " + CUSTO_TREINO + ".");
            return;
        }

        saldo -= CUSTO_TREINO; // cobrado antes do resultado
        int sorteio = random.nextInt(10) + 1;
        System.out.println("Treinar custa R$ " + CUSTO_TREINO + ". Sorteio: " + sorteio);

        int novaHabilidade = treinar(habilidade, sorteio);
        if (novaHabilidade > habilidade) {
            System.out.println("Treinamento concluído! Habilidade: " + novaHabilidade);
        } else {
            System.out.println("Treinamento falhou! Habilidade: " + novaHabilidade);
        }
        habilidade = novaHabilidade;
        System.out.println("Saldo atual: R$ " + saldo);
    }

    public static void opcaoCorrida() {
        System.out.println("\nEstratégias:");
        System.out.println("1 - Conservadora (+0)");
        System.out.println("2 - Equilibrada (+5)");
        System.out.println("3 - Agressiva (+10)");
        System.out.print("Estratégia: ");
        int estrategia = lerInteiro();

        if (estrategia < 1 || estrategia > 3) {
            System.out.println("Estratégia inválida! A corrida não foi iniciada.");
            return;
        }

        int bonus = bonusEstrategia(estrategia);
        int desempenho = calcularDesempenho(habilidade, bonus);
        int sorte = desempenho - habilidade - bonus;
        int pontosCorrida = calcularPontos(desempenho);
        int premio = calcularPremio(desempenho);

        pontos += pontosCorrida;
        saldo += premio;
        corridas++;
        registrarHistorico(pontosCorrida);

        System.out.println("Estratégia: " + estrategia + " - " + nomeEstrategia(estrategia));
        System.out.println("Sorte: " + sorte + " | Desempenho: " + desempenho);
        System.out.println("Resultado: " + calcularPosicao(desempenho)
                + " | +" + pontosCorrida + " pontos | +R$ " + premio);
        System.out.println("Saldo atual: R$ " + saldo);
    }

    public static void opcaoResultados() {
        System.out.println("\n--- Resultados ---");
        System.out.println("Corridas realizadas: " + corridas);
        System.out.println("Pontos acumulados: " + pontos);
        System.out.println("Saldo atual: R$ " + saldo);

        System.out.println("Pontos das últimas corridas:");
        if (qtdHistorico == 0) {
            System.out.println("Nenhuma corrida realizada ainda.");
        } else {
            for (int i = 0; i < qtdHistorico; i++) {
                System.out.println("  Corrida " + (i + 1) + " do histórico: " + historico[i] + " pontos");
            }
        }
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        System.out.print("Informe o nome do piloto: ");
        nome = sc.nextLine().trim();

        int opcao;
        do {
            opcao = exibirMenu();

            switch (opcao) {
                case 1:
                    mostrarPiloto(nome, habilidade, pontos, saldo, corridas);
                    break;
                case 2:
                    opcaoTreinar();
                    break;
                case 3:
                    opcaoCorrida();
                    break;
                case 4:
                    opcaoResultados();
                    break;
                case 0:
                    System.out.println("Encerrando o simulador. Até a próxima, " + nome + "!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }


}