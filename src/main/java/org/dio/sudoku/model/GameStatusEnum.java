package org.dio.sudoku.model;

public enum GameStatusEnum {

    NON_STARTED("não iniciado"),
    IMCOMPLETE("imcompleto"),
    COMPLETE("completo");

    private final String label;

    GameStatusEnum(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
