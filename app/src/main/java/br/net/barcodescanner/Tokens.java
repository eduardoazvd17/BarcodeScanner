package br.net.barcodescanner;

public enum Tokens {

    TK1("T3r_dO417gVvlaJnSB6Gug"),
    TK2("eEG_78_OR1nmKFPSYjfrHQ"),
    TK3("YTfQgI-6K0M4SPyeYLn_hQ"),
    TK4("8_zHILLrnMHZymlyRSkzeQ");

    private String token;

    Tokens(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
