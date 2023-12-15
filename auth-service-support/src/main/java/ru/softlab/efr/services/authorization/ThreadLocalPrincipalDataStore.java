package ru.softlab.efr.services.authorization;

/**
 * Хранилище данных принципала на основе ThreadLocal переменной
 *
 * @author niculichev
 * @since 01.06.2018
 */
public class ThreadLocalPrincipalDataStore implements PrincipalDataStore {
    private static final ThreadLocal<PrincipalData> threadLocalScope = new ThreadLocal<>();

    @Override
    public void setPrincipalData(PrincipalData principalData) {
        threadLocalScope.set(principalData);
    }

    @Override
    public void clearPrincipalData() {
        threadLocalScope.remove();
    }

    @Override
    public PrincipalData getPrincipalData() {
        return threadLocalScope.get();
    }
}
