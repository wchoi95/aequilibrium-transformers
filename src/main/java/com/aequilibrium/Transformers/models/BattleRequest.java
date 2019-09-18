package com.aequilibrium.Transformers.models;

import java.util.List;

public class BattleRequest {
    private List<String> transformerIds;

    public List<String> getTransformerIds() {
        return transformerIds;
    }

    public void setTransformerIds(List<String> transformerIds) {
        this.transformerIds = transformerIds;
    }
}
