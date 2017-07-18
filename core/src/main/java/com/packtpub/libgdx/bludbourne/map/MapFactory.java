package com.packtpub.libgdx.bludbourne.map;


import java.util.EnumMap;

public enum MapFactory {

    INSTANCE;
    //All maps for the game
    private static final EnumMap<MapType, Map> MAPS = new EnumMap<>(MapType.class);

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

    public Map getMap(MapType mapType) {
        Map map = MAPS.get(mapType);
        if (map == null) {
            map = mapType.getMap();
            MAPS.put(mapType, map);
        }
        return map;
    }
}
