package validators;

import domain.Candidat;
import domain.Optiune;
import domain.Sectie;

public class OptiuneValidator implements Validator<Optiune> {
    private CandidatValidator candidatValidator=new CandidatValidator();
    private SectieValidator sectieValidator=new SectieValidator();

    public boolean isValid(Optiune optiune) throws Exception {
        String error = "";
        try {
            candidatValidator.isValid(optiune.getCandidat());
        } catch (Exception e) {
            error += "Invalid Candidat!\n" + e.getMessage();
        }
        try {
            sectieValidator.isValid(optiune.getSectie());
        } catch (Exception e) {
            error += "Invalid Sectie!\n" + e.getMessage();
        }
        if (error.equals(""))
            return true;
        throw new Exception(error);
    }
}
