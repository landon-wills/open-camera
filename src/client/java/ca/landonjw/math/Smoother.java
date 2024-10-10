package ca.landonjw.math;

import java.util.LinkedList;
import java.util.List;

public class Smoother {
    private final float timeWindow;
    private final List<Entry> values = new LinkedList<>();

    public Smoother(float timeWindow) {
        this.timeWindow = timeWindow;
    }

    public float smooth(float value, float deltaValue){
        values.add(0, new Entry(value, deltaValue));
        float totalTime = 0;
        float sum = 0;
        int index = 0;

        while(index < values.size() && totalTime <= timeWindow){
            var entry = values.get(index);
            totalTime += entry.deltaTime;
            sum += entry.value * entry.deltaTime;
            index++;
        }

        while(index < values.size()){
            values.remove(index);
        }

        if(totalTime <= 0){
            return value;
        }

        return sum/totalTime;
    }

    private static class Entry {
        float deltaTime;
        float value;

        private Entry(float value, float deltaTime){
            this.deltaTime = deltaTime;
            this.value = value;
        }
    }
}
