package repository;

import domain.Candidat;
import validators.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class CandidatRepository extends FileRepository<Long, Candidat> {
    public CandidatRepository(Validator<Candidat> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public void readFromFile() {
        Path path= Paths.get(getFileName());
        Stream<String> lines;
        try{
            lines= Files.lines(path);
            lines.forEach(line ->{
                try {
                    String[] fields = line.split(";");
                    if (fields.length != 5) {
                        throw new Exception("Fisier corupt!");
                    }
                    Candidat candidat = new Candidat(Long.parseLong(fields[0]), fields[1], fields[2], fields[3], fields[4]);
                    super.save(candidat);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
            } catch(FileNotFoundException e){
                System.out.println("Fisierul nu a fost gasit!");
            } catch(IOException e){
                System.out.println("Eroare la citire");
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    private String candidatToFileString(Candidat candidat) {
        long id = candidat.getId();
        String nume = candidat.getNume();
        String telefon = candidat.getTelefon();
        String mail = candidat.getMail();
        String sex = candidat.getSex();
        return Long.toString(id) + ";" + nume + ";" + telefon + ";" + mail + ";" + sex;
    }

    @Override
    protected void writeToFile() {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(getFileName()))) {
            List<Candidat> list = (List<Candidat>) getAll();
            for (Candidat candidat : list) {
                bf.write(candidatToFileString(candidat));
                bf.write('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
