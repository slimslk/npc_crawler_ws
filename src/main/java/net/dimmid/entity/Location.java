package net.dimmid.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;

//public record Location(String locationId, int height, int width, Map<String, String> map) {
//    @JsonCreator
//    public Location(
//            @JsonProperty("location_id") String locationId,
//            @JsonProperty("location_size") LocationSize size,
//            @JsonProperty("location_data") Map<String, String> map) {
//
//        this(locationId, size.height(), size.width(), map);
//    }
//
//    public record LocationSize(int height, int width) {
//        @JsonCreator
//        public LocationSize(int[] size) {
//            this(size[0], size[1]);
//        }
//    }
//}

public record Location(
        @JsonProperty("location_id") String locationId,
        @JsonProperty("location_size") LocationSize locationSize,
        @JsonProperty("location_data") Map<String, String> map
) {
    @JsonCreator
    public Location(
            @JsonProperty("location_id") String locationId,
            @JsonProperty("location_size") LocationSize locationSize,
            @JsonProperty("location_data") Map<String, String> map
    ) {
        this.locationId = locationId;
        this.locationSize = locationSize;
        this.map = map;
    }

    public record LocationSize(int height, int width) {
        @JsonCreator
        public LocationSize(int[] size) {
            this(size[0], size[1]);
        }

        @JsonValue
        public int[] asArray() {
            return new int[]{height, width};
        }
    }
}

