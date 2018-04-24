package diploma;

public class DefinitionEntry implements Comparable<DefinitionEntry> {

    private String definition;
    private String explanation;

    public DefinitionEntry(String definition, String explanation) {
        this.definition = definition;
        this.explanation = explanation;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExplanation() {
        return explanation;
    }

    @Override
    public int compareTo(DefinitionEntry o) {
        if (this.definition.split(" ").length < o.definition.split(" ").length) {
            return o.definition.compareTo(this.definition);
        } else return this.definition.split(" ").length < o.definition.split(" ").length ? 1 : -1;
    }
}
