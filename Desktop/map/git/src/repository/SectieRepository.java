package repository;

import domain.Sectie;
import validators.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class SectieRepository extends FileRepository<Long, Sectie> {
    public SectieRepository(Validator<Sectie> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public void readFromFile() {
        Path path= Paths.get(getFileName());
        Stream<String> lines;
        try{
            lines= Files.lines(path);
            lines.forEach(line -> {
                try {
                    String[] fields = line.split(";");
                    if (fields.length != 3) {
                        throw new Exception("Fisier corupt!");
                    }
                    Sectie sectie = new Sectie(Long.parseLong(fields[0]), fields[1], Integer.parseInt(fields[2]));
                    super.save(sectie);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        } catch (FileNotFoundException e) {
            System.out.println("Fisierul nu a fost gasit!");
        } catch (IOException e) {
            System.out.println("Eroare la citire");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String sectieToFileString(Sectie sectie) {
        long id = sectie.getId();
        String nume = sectie.getNume();
        int numarLocuri = sectie.getNumarLocuri();
        return Long.toString(id) + ";" + nume + ";" + Integer.toString(numarLocuri);
    }

    @Override
    protected void writeToFile() {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(getFileName()))) {
            List<Sectie> list = (List<Sectie>) getAll();
            for (Sectie sectie : list) {
                bf.write(sectieToFileString(sectie));
                bf.write('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
