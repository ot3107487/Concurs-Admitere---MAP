package repository;

import domain.Candidat;
import domain.Optiune;
import domain.Sectie;
import javafx.util.Pair;
import validators.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class OptiuneRepository extends FileRepository<Pair<Long,Long>, Optiune> {
    private Repository<Long, Candidat> candidatRepository;
    private Repository<Long, Sectie> sectieRepository;

    public OptiuneRepository(Validator<Optiune> validator, String fileName, Repository<Long, Candidat> candidatRepository, Repository<Long, Sectie> sectieRepository) {
        super(validator, fileName);
        this.candidatRepository = candidatRepository;
        this.sectieRepository = sectieRepository;
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
                    if (fields.length != 2) {
                        throw new Exception("Fisier corupt!");
                    }
                    long candidatId = Long.parseLong(fields[0]);
                    long sectieId = Long.parseLong(fields[1]);
                        Candidat candidat = candidatRepository.findById(candidatId);
                        Sectie sectie = sectieRepository.findById(sectieId);
                        Optiune optiune = new Optiune(candidat, sectie);
                        super.save(optiune);
                }catch (Exception e){
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

    private String optiuneToFileString(Optiune optiune) {
        long candidatId = optiune.getCandidat().getId();
        long sectieId = optiune.getSectie().getId();
        return Long.toString(candidatId) + ";" + Long.toString(sectieId);
    }

    @Override
    protected void writeToFile() {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(getFileName()))) {
            List<Optiune> list = (List<Optiune>) getAll();
            for (Optiune optiune : list) {
                bf.write(optiuneToFileString(optiune));
                bf.write('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
