package net.copokbl.radicalInterdependency.roles;

public interface Role {
    String getId();
    String getName();
    String getDescription();

    void registerEvents();
}
