package moe.quill.stratumrpg.Skills;

public enum SkillType {
    MINING("Mining", 1f),
    LOGGING("Logging", .75f);


    //Declaration of skill type enum object
    private final String skillName;
    private final float difficulty;

    SkillType(String skillName, float difficulty) {
        this.skillName = skillName;
        this.difficulty = difficulty;
    }

    public String getSkillName() {
        return skillName;
    }

    public float getDifficulty() {
        return difficulty;
    }
}
