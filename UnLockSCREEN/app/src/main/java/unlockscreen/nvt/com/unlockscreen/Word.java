package unlockscreen.nvt.com.unlockscreen;

/**
 * Created by Duc Nguyen on 4/3/2017.
 */

public class Word {
    private String en;
    private String vi;

    public Word(String en, String vi) {
        this.en = en;
        this.vi = vi;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }
}
