package com.packtpub.libgdx.bludbourne.map;


import java.util.EnumMap;

public class MapFactory {
    //All maps for the game
    private static EnumMap<MapType, Map> maps = new EnumMap<>(MapType.class);

    public enum MapType {
        TOP_WORLD {
            @Override
            public Map getMap() {
                return new TopWorldMap();
            }
        },
        TOWN {
            @Override
            public Map getMap() {
                return new TownMap();
            }
        },
        CASTLE_OF_DOOM {
            @Override
            public Map getMap() {
                return new CastleDoomMap();
            }
        };

        public abstract Map getMap();
    }

    public static Map getMap(MapType mapType) {
        Map map = maps.get(mapType);
        if (map == null) {
            map = mapType.getMap();
            maps.put(mapType, map);
        }
        return map;
    }
}
