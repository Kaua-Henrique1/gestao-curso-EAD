package ifpar.tsi3.ead.infrastructure;

import ifpar.tsi3.ead.domain.Curso;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CursoRepository {

    private final String caminhoArquivo;
    private final Gson gson;

    public CursoRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void salvar(Curso curso) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(curso, writer);
        }
    }

    public Curso carregar() throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return null;
        }

        try (FileReader reader = new FileReader(arquivo)) {
            return gson.fromJson(reader, Curso.class);
        }
    }
}