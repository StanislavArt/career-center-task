package pro.sky.jd9.careercentertask.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;

    @OneToOne
    @JoinColumn(name = "socks_id")
    private Socks socks;

    public Stock() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Socks getSocks() {
        return socks;
    }

    public void setSocks(Socks socks) {
        this.socks = socks;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", socks=" + socks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return id == stock.id &&
                quantity == stock.quantity &&
                Objects.equals(socks, stock.socks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, socks);
    }
}
