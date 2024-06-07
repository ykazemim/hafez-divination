import com.fasterxml.jackson.annotation.JsonProperty;

public class Faal {

    @JsonProperty("Poem")
    private String poem;

    @JsonProperty("Interpretation")
    private String interpretation;


    public String getPoem() {
        return poem;
    }

    public String getInterpretation() {
        return interpretation;
    }
}
