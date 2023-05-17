package pro.sky.jd9.careercentertask.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "socks")
public class Socks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String color;

    @Column(name = "cotton_part")
    private int cottonPart;

    public Socks() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCottonPart() {
        return cottonPart;
    }

    public void setCottonPart(int cottonPart) {
        this.cottonPart = cottonPart;
    }

    @Override
    public String toString() {
        return "Socks{" +
                "id=" + id +
                ", color='" + color + '\'' +
                ", cottonPart=" + cottonPart +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socks socks = (Socks) o;
        return id == socks.id &&
                cottonPart == socks.cottonPart &&
                Objects.equals(color, socks.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, cottonPart);
    }
}
