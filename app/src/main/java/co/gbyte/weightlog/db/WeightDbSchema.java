package co.gbyte.weightlog.db;

/**
 * Created by walt on 20/10/16.
 *
 */

public class WeightDbSchema {
    public static final class WeightTable {
        public static final String NAME = "weights";

        public static final class Cols {
            public static final String UUID   = "uuid";
            public static final String TIME   = "time";
            public static final String WEIGHT = "weight";
            public static final String NOTE   = "note";
        }
    }
}
