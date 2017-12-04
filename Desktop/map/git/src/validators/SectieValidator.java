package validators;

import domain.Sectie;

public class SectieValidator implements Validator<Sectie> {
    public boolean isValid(Sectie sectie) throws Exception {
        String error = "";
        if (sectie.getId() < 0)
            error += "Invalid id!\n";
        if (sectie.getNume().length() == 0)
            error += "Name can't be null!\n";
        if (sectie.getNumarLocuri() <= 0)
            error += "Invalid nrLoc\n";
        if (sectie.getNumarLocuri() < 30 && sectie.getNumarLocuri() > 0)
            error += "No serios cu locurile!?\n";
        if (error.equals(""))
            return true;
        throw new Exception(error);
    }
}
