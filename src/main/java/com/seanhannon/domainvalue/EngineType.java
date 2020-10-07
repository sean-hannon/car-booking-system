package com.seanhannon.domainvalue;

public enum EngineType {
    ELECTRIC("ELECTRIC"), GAS("GAS"), DIESEL("DIESEL"), HYBIRD("HYBIRD"), UNKNOWN("UNKNOWN");

    private String engineType;

    private EngineType(String engineType){
        this.engineType = engineType;
    }

    public String getEngineType() {
        return engineType;
    }


}
