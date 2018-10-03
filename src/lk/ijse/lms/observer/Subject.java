/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.lms.observer;

/**
 *
 * @author Ilman Iqbal
 */
public interface Subject<T> {
    public void registerObserver(T observer);
    public void unregister(T observer);
    public void notyfyall(String message);
}
