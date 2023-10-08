package ru.cyanworld.cyan1dex.query;

import java.util.ArrayList;

public class QueryResponse {
    static byte NULL = 0;
    static byte SPACE = 20;
    private final boolean fullstat;
    private final String motd;
    private final String gameMode;
    private final String mapName;
    private final int onlinePlayers;
    private final int maxPlayers;
    private final short port;
    private final String hostname;
    private String gameID;
    private String version;
    private String plugins;
    private ArrayList<String> playerList;

    public QueryResponse(byte[] data, boolean fullstat) {
        this.fullstat = fullstat;
        data = ByteUtils.trim(data);
        byte[][] temp = ByteUtils.split(data);
        if (!fullstat) {
            this.motd = new String(ByteUtils.subarray(temp[0], 1, temp[0].length - 1));
            this.gameMode = new String(temp[1]);
            this.mapName = new String(temp[2]);
            this.onlinePlayers = Integer.parseInt(new String(temp[3]));
            this.maxPlayers = Integer.parseInt(new String(temp[4]));
            this.port = ByteUtils.bytesToShort(temp[5]);
            this.hostname = new String(ByteUtils.subarray(temp[5], 2, temp[5].length - 1));
        } else {
            this.motd = new String(temp[3]);
            this.gameMode = new String(temp[5]);
            this.mapName = new String(temp[13]);
            this.onlinePlayers = Integer.parseInt(new String(temp[15]));
            this.maxPlayers = Integer.parseInt(new String(temp[17]));
            this.port = Short.parseShort(new String(temp[19]));
            this.hostname = new String(temp[21]);
            this.gameID = new String(temp[7]);
            this.version = new String(temp[9]);
            this.plugins = new String(temp[11]);
            this.playerList = new ArrayList();
            for (int i = 25; i < temp.length; ++i) {
                this.playerList.add(new String(temp[i]));
            }
        }
    }

    public String asJSON() {
        StringBuilder json = new StringBuilder();
        json.append("'{");
        json.append("\"motd\":");
        json.append('\"').append(this.motd).append("\",");
        json.append("\"gamemode\":");
        json.append('\"').append(this.gameMode).append("\",");
        json.append("\"map\":");
        json.append('\"').append(this.mapName).append("\",");
        json.append("\"onlinePlayers\":");
        json.append(this.onlinePlayers).append(',');
        json.append("\"maxPlayers\":");
        json.append(this.maxPlayers).append(',');
        json.append("\"port\":");
        json.append(this.port).append(',');
        json.append("\"host\":");
        json.append('\"').append(this.hostname).append('\"');
        if (this.fullstat) {
            json.append(',');
            json.append("\"gameID\":");
            json.append('\"').append(this.gameID).append("\",");
            json.append("\"version\":");
            json.append('\"').append(this.version).append("\",");
            json.append("\"players\":");
            json.append('[');
            for (String player : this.playerList) {
                json.append("\"" + player + "\"");
                if (this.playerList.indexOf(player) == this.playerList.size() - 1) continue;
                json.append(',');
            }
            json.append(']');
        }
        json.append("}'");
        return json.toString();
    }

    public String toString() {
        String delimiter = ", ";
        StringBuilder str = new StringBuilder();
        str.append(this.motd);
        str.append(delimiter);
        str.append(this.gameMode);
        str.append(delimiter);
        str.append(this.mapName);
        str.append(delimiter);
        str.append(this.onlinePlayers);
        str.append(delimiter);
        str.append(this.maxPlayers);
        str.append(delimiter);
        str.append(this.port);
        str.append(delimiter);
        str.append(this.hostname);
        if (this.fullstat) {
            str.append(delimiter);
            str.append(this.gameID);
            str.append(delimiter);
            str.append(this.version);
            if (this.plugins.length() > 0) {
                str.append(delimiter);
                str.append(this.plugins);
            }
            str.append(delimiter);
            str.append("Players: ");
            str.append('[');
            for (String player : this.playerList) {
                str.append(player);
                if (this.playerList.indexOf(player) == this.playerList.size() - 1) continue;
                str.append(',');
            }
            str.append(']');
        }
        return str.toString();
    }

    public String getMOTD() {
        return this.motd;
    }

    public String getGameMode() {
        return this.gameMode;
    }

    public String getMapName() {
        return this.mapName;
    }

    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public ArrayList<String> getPlayerList() {
        return this.playerList;
    }
}
