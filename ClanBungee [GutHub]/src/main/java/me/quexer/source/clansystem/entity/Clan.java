package me.quexer.source.clansystem.entity;

import java.util.List;

public class Clan {

    private String name;

    private String motd;

    private long createDate;

    private List<String> members;

    private String leader;

    private List<String> moderator;

    public Clan() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public List<String> getModerator() {
        return moderator;
    }

    public void setModerator(List<String> moderator) {
        this.moderator = moderator;
    }
}
