package io.yharsh.coffeemachine.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public final class Ingredient {
    private static final Logger log = LoggerFactory.getLogger(Ingredient.class);
    private String name;
    private AtomicInteger quantity;
    private ReentrantLock lock = new ReentrantLock();

    public Ingredient(String name, int quantity) {
        this.name = name;
        this.quantity = new AtomicInteger(quantity);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public boolean isAvailable(int quantity) {
        return this.quantity.get() >= quantity;
    }

    public void addQuantity(int quantity) {
        log.info("Adding {} quantity for ingredient {}", quantity, this.name);
        this.quantity.updateAndGet(q -> q + quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public boolean consumeQuantity(int quantity) {
        log.debug("Consuming {} quantity for ingredient {}", quantity, this.name);
        boolean consumed = false;
        //Do a quick reject
        if (isAvailable(quantity)) {
            try {
                lock.lock();
                if (isAvailable(quantity)) {
                    consumed = this.quantity.compareAndSet(this.getQuantity(), this.getQuantity() - quantity);
                }
            } finally {
                lock.unlock();
            }
        }
        return consumed;
    }
}
