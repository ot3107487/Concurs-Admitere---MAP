package validators;

import domain.Candidat;
import domain.Optiune;
import domain.Sectie;

import java.util.ArrayList;

public class CandidatValidator implements Validator<Candidat> {
    public CandidatValidator() {
    }

    public boolean isValid(Candidat candidat) throws Exception {
        String error = "";
        String numere = "0123456789";
        if (candidat.getId() < 0)
            error += "Invalid id!\n";
        if (!candidat.getMail().contains("@"))
            error += "Invalid email!\n";
        if (candidat.getNume().length() == 0)
            error += "Name can't be null!\n";
        if (candidat.getTelefon().length() != 10)
            error += "Invalid telephone\n";
        try{
            Double.parseDouble(candidat.getTelefon());
        }catch (NumberFormatException e){
            error+="Numarul trebe sa fie din cifre";
        }
        if (error.equals(""))
            return true;
        throw new Exception(error);
    }
}
