package samples;

/**
 * Los inputs de ejemplo del enunciado, en un solo lugar (usados por
 * la GUI y por los tests, para no repetir el texto dos veces).
 */
public final class SampleInputs {

    private SampleInputs() {
    }

    public static final String MISSION_1 = """
            10 10
            9
            0 1 2
            1 1 2
            2 2 2 9
            3 2 1 7
            5 3 3 6 9
            6 4 0 1 2 7
            7 3 0 3 8
            8 2 7 9
            9 3 2 3 4
            0 0
            9 9
            0 0
            """;

    public static final String MISSION_2 = """
            3
            2 1 0 1
            0 1 100
            3 3 2 0
            0 1 100
            0 2 200
            1 2 50
            2 0 0 1
            """;

    public static final String MISSION_2_CASE_1 = """
            1
            2 1 0 1
            0 1 100
            """;

    public static final String MISSION_2_CASE_2 = """
            1
            3 3 2 0
            0 1 100
            0 2 200
            1 2 50
            """;

    public static final String MISSION_2_CASE_3 = """
            1
            5 9 0 4
            0 1 10
            0 2 3
            2 1 4
            1 3 2
            2 3 8
            3 4 5
            2 4 15
            1 1 1
            0 1 12
            """;

    public static final String MISSION_3 = """
            3
            5 7 0 4
            0 1 50
            0 2 10
            1 2 -30
            1 3 40
            2 1 -5
            2 3 60
            3 4 20
            4 4 0 3
            0 1 20
            1 2 30
            2 1 -10
            2 3 15
            3 3 0 2
            0 1 -40
            1 2 -25
            0 2 -80
            """;

    public static final String MISSION_4 = """
            1
            4
            5
            1 2 10
            2 3 20
            3 4 30
            4 1 40
            1 3 15
            """;
    public static final String MISSION_3_CASE_1 = """
            1
            5 7 0 4
            0 1 50
            0 2 10
            1 2 -30
            1 3 40
            2 1 -5
            2 3 60
            3 4 20
            """;

    public static final String MISSION_3_CASE_2 = """
            1
            4 4 0 3
            0 1 20
            1 2 30
            2 1 -10
            2 3 15
            """;

    public static final String MISSION_3_CASE_3 = """
            1
            3 3 0 2
            0 1 -40
            1 2 -25
            0 2 -80
            """;

    public static final String MISSION_4_DISCONNECTED = """
            1
            5
            3
            1 2 5
            2 3 8
            4 5 3
            """;

    public static final String MISSION_4_BIGGER = """
            1
            6
            10
            1 2 4
            2 3 8
            3 4 7
            4 5 9
            5 6 10
            6 1 2
            1 3 11
            3 5 14
            2 6 8
            1 2 6
            """;
}