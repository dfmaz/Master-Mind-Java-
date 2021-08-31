import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Declaración e inicialización de variables y arrrays
        int intentos = 0, min = 1, max = 8, NUNMAX = 5, MAXINT = 10, mates, jaques;
        int[] password = new int[NUNMAX];
        int[] user = new int[NUNMAX];
        int[][] histComb = new int[MAXINT + 1][NUNMAX];
        String[] histMates = new String[MAXINT + 1];
        String[] histJaques = new String[MAXINT + 1];

        // Creación de la combinación secreta aleatoria
        do
            randomVector(min, max, NUNMAX, password);
        while (findRepeated(password, NUNMAX));

        // Ejecución  por línea de comandos (args)
        for (int i = 0; i < args.length; i++) {
            password[i] = Integer.parseInt(args[i]);
            if (Integer.parseInt(args[i]) < min || Integer.parseInt(args[i]) > max || args.length != NUNMAX) { // || args.length != NUNMAX añadido hoy
                System.out.printf("\nError. Los valores deben estar en el intervalo [%d,%d]  y la combinación debe ser de 5 dígitos.\nVuelve a ejecutar el programa.\n", min, max);
                return;
            }
        }
        if (findRepeated(password, NUNMAX)) {
            System.out.println("\nError. La combinación introducida no puede tener dígitos repetidos.\nVuelve a ejecutar el programa.");
            return;
        }

        // Mensaje de bienvenida
        welcome();

        do {
            intentos++;
            // Lectura y control de entrada de la combinación del usuario
            System.out.printf("INTENTO %2d: ", intentos);
            readVector(min, max, intentos, NUNMAX, user);
            while (findRepeated(user, NUNMAX)) {
                System.out.println("Error. La combinación tiene dígitos repetidos.");
                System.out.printf("INTENTO %2d: ", intentos);
                readVector(min, max, intentos, NUNMAX, user);
            }
            // Cálculo del número de mates y de jaques
            mates = mates(user, password, NUNMAX);
            jaques = jaques(user, password, NUNMAX);

            // Historial de los intentos de la partida
            history(user, histComb, histMates, histJaques, intentos, NUNMAX, mates, jaques);
        }
        while (mates < NUNMAX && intentos < MAXINT);
        if (mates == NUNMAX)
            System.out.printf("\n¡Lo conseguiste!. Has necesitado %d intento(s) para descrifrar el código.", intentos);
        else {
            System.out.println();
            System.out.println("- - - - - - GAME OVER - - - - - -");
            System.out.println("La combinación secreta era:");
            showVector(password, NUNMAX);
        }
    }

    // Función para mostrar un mensaje de bienvenida
    public static void welcome() {
        System.out.println();
        System.out.println("¡Bienvenido al Mastermind!");
        System.out.println("Tienes 10 intentos.");
        System.out.println("Cada intento debe ser una combinación de 5 dígitos entre el 1 y el 8.");
    }

    // Función para generar un valor aleatorio en un intervalo
    public static int randomValue(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    // Función para generar un vector de valores aleatorios en un intervalo
    public static void randomVector(int min, int max, int c, int[] v) {
        for (int i = 0; i <= c - 1; i++) {
            v[i] = randomValue(min, max);
        }
    }

    // Función que lee desde teclado un vector con valores enteros en un intervalo
    public static void readVector(int min, int max, int intentos, int c, int[] v) {
        Scanner sc = new Scanner(System.in);
        int i;
        do {
            i = -1;
            do {
                i = i + 1;
                v[i] = sc.nextInt();
            }
            while (v[i] >= min && v[i] <= max && i < c - 1);
            if (v[i] < min || v[i] > max) {
                System.out.printf("Error. Los valores deben estar en el intervalo [%d,%d].\n", min, max);
                System.out.printf("INTENTO %2d: ", intentos);
            }
        }
        while (v[i] < min || v[i] > max);
    }

    //  Función que devuelve el resultado de comprobar si en un vector de valores enteros hay alguno repetido
    public static boolean findRepeated(int[] v, int c) {
        int i, j;
        i = -1;
        do {
            i++;
            j = i + 1;
            while (v[i] != v[j] && j < c - 1)
                j++;
        }
        while (v[i] != v[j] && i < c - 2);
        return (v[i] == v[j]);
    }

    //  Función que compara la combinación secreta y la del usuario (JAQUES)
    public static int jaques(int[] v1, int[] v2, int c) {
        int j, JAQUES = 0;
        for (int i = 0; i <= c - 1; i++) {
            j = 0;
            while (v1[i] != v2[j] && j < c - 1)
                j++;
            if (v1[i] == v2[j])
                if (i != j) JAQUES++;
        }
        return JAQUES;
    }

    // Función que compara la combinación secreta y la del usuario (MATES)
    public static int mates(int[] v1, int[] v2, int c) {
        int j, MATES = 0;
        for (int i = 0; i <= c - 1; i++) {
            j = 0;
            while (v1[i] != v2[j] && j < c - 1)
                j++;
            if (v1[i] == v2[j])
                if (i == j) MATES++;
        }
        return MATES;
    }

    //  Función que muestra un vector de enteros por pantalla
    public static void showVector(int[] v, int c) {
        for (int i = 0; i <= c - 1; i++)
            System.out.printf("%2d", v[i]);
    }

    // Función que muestra el historial acumulado de la partida
    public static void history(int[] v1, int[][] histComb, String[] histMates, String[] histJaques, int intentos, int c, int MATES, int JAQUES) {
        // Generación del histórico de combinaciones
        System.arraycopy(v1, 0, histComb[intentos], 0, c);

        // Generación del histórico de mates
        switch (MATES) {
            case 0 -> histMates[intentos] = "";
            case 1 -> histMates[intentos] = "*";
            case 2 -> histMates[intentos] = "* *";
            case 3 -> histMates[intentos] = "* * *";
            case 4 -> histMates[intentos] = "* * * *";
            case 5 -> histMates[intentos] = "* * * * *";
        }

        // Generación del histórico de jaques
        switch (JAQUES) {
            case 0 -> histJaques[intentos] = "";
            case 1 -> histJaques[intentos] = "o";
            case 2 -> histJaques[intentos] = "o o";
            case 3 -> histJaques[intentos] = "o o o";
            case 4 -> histJaques[intentos] = "o o o o";
            case 5 -> histJaques[intentos] = "o o o o o";
        }

        // Mostramos el histórico de la partida hasta el intento actual
        for (int i = 1; i <= intentos; i++) {
            System.out.printf("%d:", i);
            for (int n = 0; n < c; n++) {
                System.out.printf("%d", histComb[i][n]);
            }
            System.out.println(" - " + histMates[i] + histJaques[i]);
        }
    }
}