package analyzer;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File path = new File(args[0]);
        File patterns = new File(args[1]);

        new Application().run(path, patterns);
    }

}
