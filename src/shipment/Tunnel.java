package shipment;

import shipment.ship.Ship;
import shipment.ship.types.Type;

import java.util.ArrayList;
import java.util.List;

public class Tunnel {

    private static final int MAX_SHIPS_IN_TUNNEL = 5;
    private static final int MIN_SHIPS_IN_TUNNEL = 0;

    private List<Ship> store;
    private int shipsCounter = 0;

    public Tunnel(){
        store = new ArrayList<>();
    }

    public synchronized boolean add(Ship element){
        try{
            if(shipsCounter < MAX_SHIPS_IN_TUNNEL){
                notifyAll();
                store.add(element);
                String info = String.format("%s + The ship arrived in the tunnel: %s, %s, %s",
                        store.size(), element.getType(), element.getSize(), element.getCount());
                System.out.println(info);
                shipsCounter++;
            } else{
                System.out.println(store.size() + "> There is no space for a ship in the tunnel " + Thread.currentThread().getName());
                wait();
                return false;
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return true;
    }

    public synchronized Ship get(Type shipType) throws InterruptedException {
        try {
            if (shipsCounter > MIN_SHIPS_IN_TUNNEL) {
                notifyAll();
                for (Ship ship : store) {
                    if (ship.getType() == shipType) {
                        shipsCounter--;
                        System.out.println(store.size() + "- The ship is taken from the tunnel: " + Thread.currentThread().getName());
                        store.remove(ship);
                        return ship;
                    }
                }
            }

            System.out.println("0 < There are no ships in the tunnel");
            wait();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }
}
